package com.atg.openssp.dataprovider.provider.handler;

import com.atg.openssp.common.demand.Supplier;
import com.atg.openssp.core.cache.broker.dto.SiteDto;
import com.atg.openssp.core.system.LocalContext;
import com.atg.openssp.dataprovider.provider.dto.MaintenanceCommand;
import com.atg.openssp.dataprovider.provider.dto.SiteMaintenanceDto;
import com.google.gson.*;
import openrtb.bidrequest.model.Site;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.properties.ProjectProperty;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.PropertyException;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author André Schmer
 */
public class SiteDataMaintenanceHandler extends DataHandler {
    private static final Logger log = LoggerFactory.getLogger(SiteDataMaintenanceHandler.class);
    public static final String CONTEXT = "/maintain/site";

    public SiteDataMaintenanceHandler(HttpServletRequest request, HttpServletResponse response) {
        if (LocalContext.isSiteDataServiceEnabled()) {
            try {
                String location;
                try {
                    location = ProjectProperty.getPropertiesResourceLocation()+"/";
                } catch (PropertyException e) {
                    log.warn("property file not found.");
                    location="";
                }
                Map<String,String> parms = queryToMap(request.getQueryString());
                String t = parms.get("t");

                if (LoginHandler.TOKEN.equals(t)) {

                    GsonBuilder builder = new GsonBuilder();

                    builder.registerTypeAdapter(MaintenanceCommand.class, (JsonDeserializer<MaintenanceCommand>) (json, typeOfT, context)
                            -> MaintenanceCommand.valueOf(json.getAsString()));

                    Site.populateTypeAdapters(builder);

                    Gson gson = builder.create();


                    SiteMaintenanceDto dto = gson.fromJson(request.getReader(), SiteMaintenanceDto.class);
                    dto.getCommand();

                    Path path = Paths.get(location+"site_db.json");
                    System.out.println(path);
                    String content = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
                    SiteDto data = gson.fromJson(content, SiteDto.class);

                    String result;
                    if (dto.getCommand() == MaintenanceCommand.ADD) {
                        Site s = dto.getSite();
                        data.getSites().add(s);
                        save(gson, path, data);
                        result = gson.toJson(data);
                    } else if (dto.getCommand() == MaintenanceCommand.REMOVE) {
                        Site s = dto.getSite();
                        remove(data, s);
                        save(gson, path, data);
                        result = gson.toJson(data);
                    } else if (dto.getCommand() == MaintenanceCommand.UPDATE) {
                        Site s = dto.getSite();
                        remove(data, s);
                        data.getSites().add(s);
                        save(gson, path, data);
                        result = gson.toJson(data);
                    } else {
                        result = "{}";
                    }
                    response.setStatus(200);
                    response.setContentType("application/json; charset=UTF8");
                    OutputStream os = response.getOutputStream();
                    os.write(result.getBytes());
                    os.close();
                } else {
                    response.setStatus(401);
                }
            } catch (IOException e) {
                response.setStatus(400);
                log.error(e.getMessage(), e);
            }
        } else {
            response.setStatus(404);
        }
    }

    private void remove(SiteDto data, Site s) {
        ArrayList<Site> working = new ArrayList();
        working.addAll(data.getSites());
        for (Site ss : working) {
            if (ss.getId().equals(s.getId())) {
                data.getSites().remove(ss);
            }
        }

    }

    private void save(Gson gson, Path path, SiteDto data) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(path.toFile()));
        pw.println(gson.toJson(data));
        pw.close();
    }

    @Override
    public void cleanUp() {

    }

}
