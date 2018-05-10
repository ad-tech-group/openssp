package com.atg.openssp.dataprovider.provider.handler;

import com.atg.openssp.common.core.broker.dto.SiteDto;
import com.atg.openssp.common.core.system.LocalContext;
import com.atg.openssp.common.provider.DataHandler;
import com.atg.openssp.common.provider.LoginHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.freestar.service.AerospikeSiteCacheTask;
import openrtb.bidrequest.model.Site;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class AerospikeSiteDataHandler extends DataHandler {
    private static final Logger log = LoggerFactory.getLogger(SiteDataHandler.class);
    public static final String CONTEXT = "/lookup/site";
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * returns the serialized site list from the cache.
     * @param request
     * @param response
     */
    public AerospikeSiteDataHandler(HttpServletRequest request, HttpServletResponse response) {
        if (LocalContext.isSiteDataServiceEnabled()) {
            try {
                final List<Site> cache = AerospikeSiteCacheTask.getInstance().getCache();
                List<Site> data;
                synchronized (cache) {
                    data = cache;
                }

                final SiteDto siteDto = new SiteDto();
                siteDto.setSite(data);

                Map<String, String> parms = queryToMap(request.getQueryString());
                String t = parms.get("t");

                if (LoginHandler.TOKEN.equals(t)) {
                    String result = objectMapper.writeValueAsString(siteDto);
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
