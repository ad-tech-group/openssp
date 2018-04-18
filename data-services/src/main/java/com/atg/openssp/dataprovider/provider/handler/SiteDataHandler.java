package com.atg.openssp.dataprovider.provider.handler;

import com.atg.openssp.common.core.broker.dto.SiteDto;
import com.atg.openssp.common.core.system.LocalContext;
import com.atg.openssp.dataprovider.provider.DataStore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import openrtb.bidrequest.model.Site;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.properties.ProjectProperty;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.PropertyException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * @author André Schmer
 */
public class SiteDataHandler extends DataHandler {
    private static final Logger log = LoggerFactory.getLogger(SiteDataHandler.class);
    public static final String CONTEXT = "/lookup/site";

    public SiteDataHandler(HttpServletRequest request, HttpServletResponse response) {
        if (LocalContext.isSiteDataServiceEnabled()) {
            try {
                String location;
                try {
                    location = ProjectProperty.getPropertiesResourceLocation()+"/";
                } catch (PropertyException e) {
                    log.warn("property file not found.");
                    location="";
                }
                SiteDto data = DataStore.getInstance().lookupSites();
                if (DataStore.getInstance().wasSitesCreated()) {
                    GsonBuilder builder = new GsonBuilder();
                    Site.populateTypeAdapters(builder);
                    Gson gson = builder.create();
                    String content = new String(Files.readAllBytes(Paths.get(location + "site_db.json")), StandardCharsets.UTF_8);
                    SiteDto newData = gson.fromJson(content, SiteDto.class);
                    for (Site s : newData.getSites()) {
                        DataStore.getInstance().insert(s);
                    }
                    DataStore.getInstance().clearSitesCreatedFlag();
                    data = DataStore.getInstance().lookupSites();
                }

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
                    log.info("<--"+result);
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
