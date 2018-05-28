package com.atg.openssp.dataprovider.provider.handler;

import com.atg.openssp.common.core.broker.dto.BannerAdDto;
import com.atg.openssp.common.core.system.LocalContext;
import com.atg.openssp.common.provider.DataHandler;
import com.atg.openssp.common.provider.LoginHandler;
import com.atg.openssp.dataprovider.provider.model.BannerAdModel;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author Brian Sorensen
 */
public class BannerAdDataHandler extends DataHandler {
    private static final Logger log = LoggerFactory.getLogger(BannerAdDataHandler.class);
    public static final String CONTEXT = "/ads/banner";

    public BannerAdDataHandler(HttpServletRequest request, HttpServletResponse response) {
        if (LocalContext.isAdservingChannelEnabled()) {
            try {
                BannerAdDto data = BannerAdModel.getInstance().lookupDto();
                Map<String,String> parms = queryToMap(request.getQueryString());
                String t = parms.get("t");

                if (LoginHandler.TOKEN.equals(t)) {
                    String result = new Gson().toJson(data);
                    response.setStatus(200);
                    response.setContentType("application/json; charset=UTF8");
                    OutputStream os = response.getOutputStream();
                    os.write(result.getBytes());
                    os.flush();
                    os.close();
                    log.info("<--"+result.replaceAll("\n", ""));
                } else {
                    response.setStatus(401);
                }
            } catch (Exception e) {
                response.setStatus(500);
                log.error(e.getMessage(), e);
            }
        } else {
            response.setStatus(404);
        }
    }

    @Override
    public void cleanUp() {

    }
}
