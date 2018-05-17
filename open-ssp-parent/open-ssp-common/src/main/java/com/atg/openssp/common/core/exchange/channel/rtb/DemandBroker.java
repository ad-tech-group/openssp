package com.atg.openssp.common.core.exchange.channel.rtb;

import com.atg.openssp.common.core.broker.AbstractBroker;
import com.atg.openssp.common.core.connector.JsonPostConnector;
import com.atg.openssp.common.core.entry.BiddingServiceInfo;
import com.atg.openssp.common.core.entry.SessionAgent;
import com.atg.openssp.common.core.exchange.cookiesync.CookieSyncDTO;
import com.atg.openssp.common.core.exchange.cookiesync.CookieSyncManager;
import com.atg.openssp.common.core.exchange.cookiesync.DspCookieDto;
import com.atg.openssp.common.demand.ResponseContainer;
import com.atg.openssp.common.demand.Supplier;
import com.atg.openssp.common.exception.BidProcessingException;
import com.atg.openssp.common.logadapter.DspCookieSyncLogProcessor;
import com.atg.openssp.common.logadapter.RtbRequestLogProcessor;
import com.atg.openssp.common.logadapter.RtbResponseLogProcessor;
import com.atg.openssp.common.logadapter.TimeInfoLogProcessor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import openrtb.bidrequest.model.BidRequest;
import openrtb.bidrequest.model.User;
import openrtb.bidresponse.model.BidResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.util.concurrent.Callable;

/**
 * This class acts as Broker to a connector used in demand (OpenRTB) context. It represents one Demand side (DSP).
 * 
 * @author André Schmer
 *
 */
public final class DemandBroker extends AbstractBroker implements Callable<ResponseContainer> {

	private static final Logger LOG = LoggerFactory.getLogger(DemandBroker.class);

	private static final String SCHEME = "http";

	private final BiddingServiceInfo info;

	private final Supplier supplier;

	private final OpenRtbConnector connector;

	private final Header[] headers;

	private Gson gson;

	private BidRequest bidrequest;

	public DemandBroker(BiddingServiceInfo info, final Supplier supplier, final OpenRtbConnector connector, final SessionAgent agent) {
		super(agent);
		this.info = info;
		this.supplier = supplier;
		this.connector = connector;

		headers = new Header[2];
		headers[0] = new BasicHeader("x-openrtb-version", supplier.getOpenRtbVersion());
		headers[1] = new BasicHeader("ContentType", supplier.getContentType());
		// headers[2] = new BasicHeader("Accept-Encoding", supplier.getAcceptEncoding());
		// headers[3] = new BasicHeader("Content-Encoding", supplier.getContentEncoding());

		try {
			gson = new GsonBuilder().setVersion(Double.valueOf(supplier.getOpenRtbVersion())).create();
		} catch (Throwable t) {
			LOG.error(t.getMessage(), t);
		}
	}

	@Override
	public ResponseContainer call() throws Exception {
		if (bidrequest == null) {
			return null;
		}
		BidRequest workingBidRequest = bidrequest.clone();
		long startTS = System.currentTimeMillis();


		try {
            User user = workingBidRequest.getUser();
            try {

                long csBegin = System.currentTimeMillis();
                if (CookieSyncManager.getInstance().supportsCookieSync()) {
                    String userId = user.getId();
                    if (userId != null && !"".equals(userId)) {
                        CookieSyncDTO cookieSyncDTO = CookieSyncManager.getInstance().get(userId);
                        if (cookieSyncDTO != null) {
                            DspCookieDto dspDto = cookieSyncDTO.lookup(supplier.getShortName());
                            if (dspDto != null) {
                                String buyerId = dspDto.getUid();
                                user.setBuyeruid(buyerId);
                                DspCookieSyncLogProcessor.instance.setLogData("include-buyer-id", userId, Long.toString(supplier.getSupplierId()), supplier.getShortName(), buyerId);
                            }
                        }
                        long csEnd = System.currentTimeMillis();
                        LOG.info(supplier.getShortName()+" Cookie Sync Update time: "+(csEnd-csBegin));
                    }
                }
            } catch (Exception ex) {
                LOG.error("Error on cookie sync lookup", ex);
            }

			DemandBrokerFilter brokerFilter = info.getDemandBrokerFilter(supplier, gson, workingBidRequest);

            final String jsonBidrequest = brokerFilter.filterRequest(gson, workingBidRequest);

			LOG.debug(supplier.getShortName()+" bidrequest: " + jsonBidrequest);
            System.out.println(supplier.getShortName()+" bidrequest: " + jsonBidrequest);
			RtbRequestLogProcessor.instance.setLogData(jsonBidrequest, "bidrequest", supplier.getShortName());

			final String result = connector.connect(jsonBidrequest, headers);
			if (!StringUtils.isEmpty(result)) {
				if (JsonPostConnector.NO_CONTENT.equals(result)) {
                    LOG.debug(supplier.getShortName()+" bidresponse: no content");
                    System.out.println(supplier.getShortName()+" bidresponse: no content");
					RtbResponseLogProcessor.instance.setLogData("no content", "bidresponse", supplier.getShortName());
				} else {
					LOG.debug(supplier.getShortName()+" bidresponse: " + result);
                    System.out.println(supplier.getShortName()+" bidresponse: " + result);
					RtbResponseLogProcessor.instance.setLogData(result, "bidresponse", supplier.getShortName());
					final BidResponse bidResponse = brokerFilter.filterResponse(gson, result);

					Supplier s = supplier.clone();
					ResponseContainer container =  new ResponseContainer(s, bidResponse);

                    String cookieSync = s.getCookieSync();
                    if (cookieSync != null && !"".equals(cookieSync)) {
                        LOG.debug(supplier.getShortName()+" set cookie sync value");
                        System.out.println(supplier.getShortName()+".. set cookie sync value");
                        StringBuilder sspRedirUrl = new StringBuilder();
                        String uid = workingBidRequest.getUser().getId();
                        //TODO: BKS
                        String addr = "openssp.pub.network";
                        System.out.println("forgetmenot: "+getSessionAgent().getHttpRequest().getLocalName());
						String context = getSessionAgent().getHttpRequest().getContextPath();
                        sspRedirUrl.append(SCHEME + "://" + addr + "/"+context+"/cookiesync?fsuid=" + uid + "&dsp=" + s.getShortName() + "&dsp_uid={UID}");
                        s.setCookieSync(URLEncoder.encode(cookieSync.replace("{SSP_REDIR_URL}", sspRedirUrl.toString()), "UTF-8"));
                    }
					return container;
				}
			} else {
                LOG.debug(supplier.getShortName()+" bidresponse: is null");
                System.out.println(supplier.getShortName()+" bidresponse: is null");
                RtbResponseLogProcessor.instance.setLogData("is null", "bidresponse", supplier.getShortName());
            }
		} catch (final BidProcessingException e) {
			LOG.error(getClass().getSimpleName() + " " + ""+e.getMessage(), e);
            TimeInfoLogProcessor.instance.setLogData(info.getLoggingId(), supplier.getSupplierId()+" fault ("+e.getMessage()+")");
			throw e;
		} catch (final Exception e) {
			LOG.error(getClass().getSimpleName() + " " + e.getMessage(), e);
            TimeInfoLogProcessor.instance.setLogData(info.getLoggingId(), supplier.getSupplierId()+" fault ("+e.getMessage()+")");
			//throw e;
		} finally {
            long endTS = System.currentTimeMillis();

            TimeInfoLogProcessor.instance.setLogData(info.getLoggingId(), workingBidRequest.getId(), workingBidRequest.getUser().getId(), supplier.getSupplierId(), supplier.getShortName(), startTS, endTS, endTS-startTS);
		}
		return null;
	}

	public void setBidRequest(final BidRequest bidrequest) {
		this.bidrequest = bidrequest;
	}

}
