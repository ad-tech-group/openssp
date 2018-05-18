package com.atg.openssp.dataprovider.provider.handler;

import com.atg.openssp.common.core.broker.dto.SupplierDto;
import com.atg.openssp.common.core.system.LocalContext;
import com.atg.openssp.common.core.system.loader.GlobalContextLoader;
import com.atg.openssp.common.demand.Supplier;
import com.atg.openssp.common.provider.DataHandler;
import com.atg.openssp.common.provider.LoginHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
 * @author Brian Sorensen
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
                    GsonBuilder builder = new GsonBuilder();
                    Supplier.populateTypeAdapters(builder);
                    Gson gson = builder.create();
                    String content = new String(Files.readAllBytes(Paths.get(location+"supplier_db.json")), StandardCharsets.UTF_8);
                Map<String,String> parms = queryToMap(request.getQueryString());
                String t = parms.get("t");

                if (LoginHandler.TOKEN.equals(t)) {
//                    GsonBuilder builder = new GsonBuilder();
                    Supplier.populateTypeAdapters(builder);
                    String result = content; //builder.create().toJson(data);
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
