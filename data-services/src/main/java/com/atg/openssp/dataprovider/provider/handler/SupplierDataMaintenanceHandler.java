package com.atg.openssp.dataprovider.provider.handler;

import com.atg.openssp.common.core.system.LocalContext;
import com.atg.openssp.common.demand.Supplier;
import com.atg.openssp.common.provider.DataHandler;
import com.atg.openssp.common.provider.LoginHandler;
import com.atg.openssp.dataprovider.provider.dto.MaintenanceCommand;
import com.atg.openssp.dataprovider.provider.dto.ResponseStatus;
import com.atg.openssp.dataprovider.provider.dto.SupplierMaintenanceDto;
import com.atg.openssp.dataprovider.provider.dto.SupplierResponse;
import com.atg.openssp.dataprovider.provider.model.SupplierModel;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author Brian Sorensen
 */
public class SupplierDataMaintenanceHandler extends DataHandler {
    private static final Logger log = LoggerFactory.getLogger(SupplierDataMaintenanceHandler.class);
    public static final String CONTEXT = "/maintain/supplier";

    public SupplierDataMaintenanceHandler(HttpServletRequest request, HttpServletResponse response) {
        if (LocalContext.isSupplierDataServiceEnabled()) {
            try {
                Map<String,String> parms = queryToMap(request.getQueryString());
                String t = parms.get("t");

                if (LoginHandler.TOKEN.equals(t)) {

                    GsonBuilder builder = new GsonBuilder();

                    builder.registerTypeAdapter(MaintenanceCommand.class, (JsonDeserializer<MaintenanceCommand>) (json, typeOfT, context) -> MaintenanceCommand.valueOf(json.getAsString()));

                    Supplier.populateTypeAdapters(builder);
                    Gson gson = builder.create();

                    SupplierMaintenanceDto dto = gson.fromJson(request.getReader(), SupplierMaintenanceDto.class);

                    SupplierResponse result = new SupplierResponse();

                    if (dto.getCommand() == MaintenanceCommand.LIST) {
                        result.setStatus(ResponseStatus.SUCCESS);
                        result.setSupplier(SupplierModel.getInstance().lookupSuppliers().getSupplier());
                    } else if (dto.getCommand() == MaintenanceCommand.ADD) {
                        Supplier s = dto.getSupplier();
                        SupplierModel.getInstance().insert(s);
                        result.setSupplier(SupplierModel.getInstance().lookupSuppliers().getSupplier());
                        result.setStatus(ResponseStatus.SUCCESS);
                    } else if (dto.getCommand() == MaintenanceCommand.REMOVE) {
                        Supplier s = dto.getSupplier();
                        SupplierModel.getInstance().remove(s);
                        result.setSupplier(SupplierModel.getInstance().lookupSuppliers().getSupplier());
                        result.setStatus(ResponseStatus.SUCCESS);
                    } else if (dto.getCommand() == MaintenanceCommand.UPDATE) {
                        Supplier s = dto.getSupplier();
                        SupplierModel.getInstance().update(s);
                        result.setSupplier(SupplierModel.getInstance().lookupSuppliers().getSupplier());
                        result.setStatus(ResponseStatus.SUCCESS);
                    } else if (dto.getCommand() == MaintenanceCommand.IMPORT) {
                        SupplierModel.getInstance().importSuppliers("supplier_export_db");
                        result.setSupplier(SupplierModel.getInstance().lookupSuppliers().getSupplier());
                        result.setStatus(ResponseStatus.SUCCESS);
                    } else if (dto.getCommand() == MaintenanceCommand.EXPORT) {
                        SupplierModel.getInstance().exportSuppliers("supplier_export_db");
                        result.setSupplier(SupplierModel.getInstance().lookupSuppliers().getSupplier());
                        result.setStatus(ResponseStatus.SUCCESS);
                    } else if (dto.getCommand() == MaintenanceCommand.LOAD) {
                        SupplierModel.getInstance().loadSuppliers();
                        result.setSupplier(SupplierModel.getInstance().lookupSuppliers().getSupplier());
                        result.setStatus(ResponseStatus.SUCCESS);
                    } else if (dto.getCommand() == MaintenanceCommand.CLEAR) {
                        SupplierModel.getInstance().clear();
                        result.setSupplier(SupplierModel.getInstance().lookupSuppliers().getSupplier());
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
            } catch (Exception e) {
                response.setStatus(400);
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
