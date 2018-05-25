package com.atg.openssp.dataprovider.provider.handler;

import com.atg.openssp.common.core.system.LocalContext;
import com.atg.openssp.common.provider.DataHandler;
import com.atg.openssp.common.provider.LoginHandler;
import com.atg.openssp.dataprovider.provider.dto.MaintenanceCommand;
import com.atg.openssp.dataprovider.provider.dto.PricelayerMaintenanceDto;
import com.atg.openssp.dataprovider.provider.dto.PricelayerResponse;
import com.atg.openssp.dataprovider.provider.dto.ResponseStatus;
import com.atg.openssp.dataprovider.provider.model.PricelayerModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import openrtb.bidrequest.model.Pricelayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author Brian Sorensen
 */
public class PricelayerDataMaintenanceHandler extends DataHandler {
    private static final Logger log = LoggerFactory.getLogger(PricelayerDataMaintenanceHandler.class);
    public static final String CONTEXT = "/maintain/pricelayer";

    public PricelayerDataMaintenanceHandler(HttpServletRequest request, HttpServletResponse response) {
        if (LocalContext.isPricelayerDataServiceEnabled()) {
            try {
                Map<String,String> parms = queryToMap(request.getQueryString());
                String t = parms.get("t");

                if (LoginHandler.TOKEN.equals(t)) {

                    GsonBuilder builder = new GsonBuilder();

                    builder.registerTypeAdapter(MaintenanceCommand.class, (JsonDeserializer<MaintenanceCommand>) (json, typeOfT, context)
                            -> MaintenanceCommand.valueOf(json.getAsString()));

                    Pricelayer.populateTypeAdapters(builder);

                    Gson gson = builder.create();

                    PricelayerMaintenanceDto dto = gson.fromJson(request.getReader(), PricelayerMaintenanceDto.class);

                    PricelayerResponse result = new PricelayerResponse();

                    if (dto.getCommand() == MaintenanceCommand.LIST) {
                        result.setStatus(ResponseStatus.SUCCESS);
                        result.setPricelayers(PricelayerModel.getInstance().lookupPricelayers().getPricelayer());
                    } else if (dto.getCommand() == MaintenanceCommand.ADD) {
                        Pricelayer s = dto.getPricelayer();
                        PricelayerModel.getInstance().insert(s);
                        result.setPricelayers(PricelayerModel.getInstance().lookupPricelayers().getPricelayer());
                        result.setStatus(ResponseStatus.SUCCESS);
                    } else if (dto.getCommand() == MaintenanceCommand.REMOVE) {
                        Pricelayer s = dto.getPricelayer();
                        PricelayerModel.getInstance().remove(s);
                        result.setPricelayers(PricelayerModel.getInstance().lookupPricelayers().getPricelayer());
                        result.setStatus(ResponseStatus.SUCCESS);
                    } else if (dto.getCommand() == MaintenanceCommand.UPDATE) {
                        Pricelayer s = dto.getPricelayer();
                        PricelayerModel.getInstance().update(s);
                        result.setPricelayers(PricelayerModel.getInstance().lookupPricelayers().getPricelayer());
                        result.setStatus(ResponseStatus.SUCCESS);
                    } else if (dto.getCommand() == MaintenanceCommand.IMPORT) {
                        PricelayerModel.getInstance().importPricelayers("price_layer_export");
                        result.setPricelayers(PricelayerModel.getInstance().lookupPricelayers().getPricelayer());
                        result.setStatus(ResponseStatus.SUCCESS);
                    } else if (dto.getCommand() == MaintenanceCommand.EXPORT) {
                        PricelayerModel.getInstance().exportPricelayers("price_layer_export");
                        result.setPricelayers(PricelayerModel.getInstance().lookupPricelayers().getPricelayer());
                        result.setStatus(ResponseStatus.SUCCESS);
                    } else if (dto.getCommand() == MaintenanceCommand.LOAD) {
                        PricelayerModel.getInstance().loadPricelayers();
                        result.setPricelayers(PricelayerModel.getInstance().lookupPricelayers().getPricelayer());
                        result.setStatus(ResponseStatus.SUCCESS);
                    } else if (dto.getCommand() == MaintenanceCommand.CLEAR) {
                        PricelayerModel.getInstance().clear();
                        result.setPricelayers(PricelayerModel.getInstance().lookupPricelayers().getPricelayer());
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
