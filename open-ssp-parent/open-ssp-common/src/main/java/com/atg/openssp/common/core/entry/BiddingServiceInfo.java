package com.atg.openssp.common.core.entry;

import com.atg.openssp.common.core.exchange.channel.rtb.DemandBrokerFilter;
import com.atg.openssp.common.core.exchange.channel.rtb.PassthroughFilter;
import com.atg.openssp.common.demand.ParamValue;
import com.atg.openssp.common.demand.Supplier;
import com.google.gson.Gson;
import openrtb.bidrequest.model.BidRequest;
import openrtb.bidrequest.model.Site;
import openrtb.tables.AuctionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BiddingServiceInfo {
    private final Logger log = LoggerFactory.getLogger(BiddingServiceInfo.class);
    private HashMap<String, String> headers = new HashMap();
    private HashMap<String, DemandBrokerFilter> demandBrokerFilterMap = new HashMap();
    private SessionAgentType type;
    private String contentType;
    private String characterEncoding;
    private boolean activateAccessAllowOrigin;
    private AuctionType auctionType = AuctionType.SECOND_PRICE;
    private boolean sendNurlNotifications = true;
    private String loggingId;
    private List<ParamValue> parameters = new ArrayList<>();
    private Site site;

    public void setType(SessionAgentType type) {
        this.type = type;
    }

    public SessionAgentType getType() {
        return type;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers.clear();
        this.headers.putAll(headers);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getCharacterEncoding() {
        return characterEncoding;
    }

    public void setCharacterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }

    public void activateAccessAllowOrigin() {
        activateAccessAllowOrigin = true;
    }

    public boolean isAccessAllowOriginActivated() {
        return activateAccessAllowOrigin;
    }

    public AuctionType getAuctionType() {
        return auctionType;
    }

    public void setAuctionType(AuctionType auctionType) {
        this.auctionType = auctionType;
    }

    public DemandBrokerFilter getDemandBrokerFilter(Supplier supplier, Gson gson, BidRequest bidrequest) {
        DemandBrokerFilter filter = demandBrokerFilterMap.get(supplier.getShortName());
        if (filter == null) {
            String demandBrokerFilterClassName = supplier.getDemandBrokerFilterClassName();
            if (demandBrokerFilterClassName == null || "".equals(demandBrokerFilterClassName)) {
                filter = new PassthroughFilter();
                demandBrokerFilterMap.put(supplier.getShortName(), filter);
            } else {
                try {
                    Class<? extends DemandBrokerFilter> c = (Class<? extends DemandBrokerFilter>) Class.forName(demandBrokerFilterClassName);
                    Constructor<? extends DemandBrokerFilter> cc = c.getConstructor(new Class[]{});
                    filter = cc.newInstance(new Object[]{});
                    demandBrokerFilterMap.put(supplier.getShortName(), filter);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return filter;
    }


    public boolean sendNurlNotifications() {
        return sendNurlNotifications;
    }

    public void disableSendNurlNotifications() {
        this.sendNurlNotifications = false;
    }

    public String getLoggingId() {
        return loggingId;
    }

    public void setLoggingId(String logginId) {
        this.loggingId = logginId;
    }

    public void setParameter(ParamValue paramValue) {
        site = paramValue.getSite();
    }

    public Site getSite()
    {
        return site;
    }
}

