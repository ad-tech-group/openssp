package com.atg.openssp.dataprovider.provider.handler;

import com.atg.openssp.common.core.broker.dto.SupplierDto;
import com.atg.openssp.common.core.system.LocalContext;
import com.atg.openssp.common.demand.Supplier;
import com.atg.openssp.dataprovider.provider.dto.MaintenanceCommand;
import com.atg.openssp.dataprovider.provider.dto.ResponseStatus;
import com.atg.openssp.dataprovider.provider.dto.SupplierMaintenanceDto;
import com.atg.openssp.dataprovider.provider.dto.SupplierResponse;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.properties.ProjectProperty;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.PropertyException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
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
public class SupplierDataMaintenanceHandler extends DataHandler {
    private static final Logger log = LoggerFactory.getLogger(SupplierDataMaintenanceHandler.class);
    public static final String CONTEXT = "/maintain/supplier";

    public SupplierDataMaintenanceHandler(HttpServletRequest request, HttpServletResponse response) {
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

                    builder.registerTypeAdapter(MaintenanceCommand.class, (JsonDeserializer<MaintenanceCommand>) (json, typeOfT, context) -> MaintenanceCommand.valueOf(json.getAsString()));

                    Supplier.populateTypeAdapters(builder);
                    Gson gson = builder.create();

                    SupplierMaintenanceDto dto = gson.fromJson(request.getReader(), SupplierMaintenanceDto.class);

                    Path path = Paths.get(location+"supplier_db.json");
                    String content = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
                    SupplierDto data = gson.fromJson(content, SupplierDto.class);
                    SupplierResponse result = new SupplierResponse();

                    if (dto.getCommand() == MaintenanceCommand.LIST) {
                        result.setStatus(ResponseStatus.SUCCESS);
                        result.setSupplier(data.getSupplier());
                    } else if (dto.getCommand() == MaintenanceCommand.ADD) {
                        Supplier s = dto.getSupplier();
                        data.getSupplier().add(s);
                        save(gson, path, data);
                        result.setSupplier(data.getSupplier());
                        result.setStatus(ResponseStatus.SUCCESS);
                    } else if (dto.getCommand() == MaintenanceCommand.REMOVE) {
                        Supplier s = dto.getSupplier();
                        remove(data, s);
                        save(gson, path, data);
                        result.setSupplier(data.getSupplier());
                        result.setStatus(ResponseStatus.SUCCESS);
                    } else if (dto.getCommand() == MaintenanceCommand.UPDATE) {
                        Supplier s = dto.getSupplier();
                        remove(data, s);
                        data.getSupplier().add(s);
                        save(gson, path, data);
                        result.setSupplier(data.getSupplier());
                        result.setStatus(ResponseStatus.SUCCESS);
                    } else {
                        result.setReason("No request data given");
                        result.setStatus(ResponseStatus.FAILURE);
                    }
                    response.setStatus(200);
                    response.setContentType("application/json; charset=UTF8");
                    OutputStream os = response.getOutputStream();
                    os.write(gson.toJson(result).getBytes());
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

    private void remove(SupplierDto data, Supplier s) {
        ArrayList<Supplier> working = new ArrayList();
        working.addAll(data.getSupplier());
        for (Supplier ss : working) {
            if (ss.getSupplierId() == s.getSupplierId()) {
                data.getSupplier().remove(ss);
            }
        }

    }

    private void save(Gson gson, Path path, SupplierDto data) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(path.toFile()));
        pw.println(gson.toJson(data));
        pw.close();
    }

    @Override
    public void cleanUp() {

    }

}
