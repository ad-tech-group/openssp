package com.atg.openssp.dataprovider.provider.handler;

import com.atg.openssp.core.cache.broker.dto.SupplierDto;
import com.atg.openssp.core.system.LocalContext;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.properties.ProjectProperty;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.PropertyException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * @author André Schmer
 */
public class SupplierDataHandler extends DataHandler {
    private static final Logger log = LoggerFactory.getLogger(SupplierDataHandler.class);
    public static final String CONTEXT = "/lookup/supplier";

    public SupplierDataHandler(HttpServletRequest request, HttpServletResponse response) {
        if (LocalContext.isSupplierDataServiceEnabled()) {
            try {
                String location;
                try {
                    location = ProjectProperty.getPropertiesResourceLocation()+"/";
                } catch (PropertyException e) {
                    log.warn("property file not found.");
                    location="";
                }
                Gson gson = new Gson();
                String content = new String(Files.readAllBytes(Paths.get(location+"supplier_db.json")), StandardCharsets.UTF_8);
                SupplierDto data = gson.fromJson(content, SupplierDto.class);

                Map<String,String> parms = queryToMap(request.getQueryString());
                String t = parms.get("t");

                if (LoginHandler.TOKEN.equals(t)) {
                    String result = new Gson().toJson(data);

                    response.setStatus(200);
                    response.setContentType("application/json; charset=UTF8");
                    OutputStream os = response.getOutputStream();
                    os.write(result.getBytes());
                    os.close();
                    log.info("<--"+result);
                } else {
                    response.setStatus(401);
                }
            } catch (IOException e) {
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
