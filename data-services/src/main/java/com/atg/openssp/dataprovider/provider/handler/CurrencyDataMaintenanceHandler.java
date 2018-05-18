package com.atg.openssp.dataprovider.provider.handler;

import com.atg.openssp.common.core.broker.dto.CurrencyDto;
import com.atg.openssp.common.core.system.LocalContext;
import com.atg.openssp.common.provider.DataHandler;
import com.atg.openssp.common.provider.LoginHandler;
import com.atg.openssp.dataprovider.provider.dto.*;
import com.atg.openssp.dataprovider.provider.model.CurrencyModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author Brian Sorensen
 */
public class CurrencyDataMaintenanceHandler extends DataHandler {
    private static final Logger log = LoggerFactory.getLogger(CurrencyDataMaintenanceHandler.class);
    public static final String CONTEXT = "/maintain/eurref";

    public CurrencyDataMaintenanceHandler(HttpServletRequest request, HttpServletResponse response) {
        if (LocalContext.isCurrencyDataServiceEnabled()) {
            try {
                Map<String,String> parms = queryToMap(request.getQueryString());
                String t = parms.get("t");

                if (LoginHandler.TOKEN.equals(t)) {

                    GsonBuilder builder = new GsonBuilder();

                    builder.registerTypeAdapter(MaintenanceCommand.class, (JsonDeserializer<MaintenanceCommand>) (json, typeOfT, context)
                            -> MaintenanceCommand.valueOf(json.getAsString()));

                    Gson gson = builder.create();

                    CurrencyMaintenanceDto dto = gson.fromJson(request.getReader(), CurrencyMaintenanceDto.class);

                    CurrencyResponse result = new CurrencyResponse();

                    if (dto.getCommand() == MaintenanceCommand.LIST) {
                        result.setStatus(ResponseStatus.SUCCESS);
                        result.setDto(CurrencyModel.getInstance().lookupCurrency());
                    } else if (dto.getCommand() == MaintenanceCommand.ADD) {
                        CurrencyDto s = dto.getDto();
                        CurrencyModel.getInstance().insert(s);
                        result.setDto(CurrencyModel.getInstance().lookupCurrency());
                        result.setStatus(ResponseStatus.SUCCESS);
                    } else if (dto.getCommand() == MaintenanceCommand.REMOVE) {
                        CurrencyDto s = dto.getDto();
                        CurrencyModel.getInstance().remove(s);
                        result.setDto(CurrencyModel.getInstance().lookupCurrency());
                        result.setStatus(ResponseStatus.SUCCESS);
                    } else if (dto.getCommand() == MaintenanceCommand.UPDATE) {
                        CurrencyDto s = dto.getDto();
                        CurrencyModel.getInstance().update(s);
                        result.setDto(CurrencyModel.getInstance().lookupCurrency());
                        result.setStatus(ResponseStatus.SUCCESS);
                    } else if (dto.getCommand() == MaintenanceCommand.IMPORT) {
                        CurrencyModel.getInstance().importCurrency("currency_export_db");
                        result.setDto(CurrencyModel.getInstance().lookupCurrency());
                        result.setStatus(ResponseStatus.SUCCESS);
                    } else if (dto.getCommand() == MaintenanceCommand.EXPORT) {
                        CurrencyModel.getInstance().exportCurrency("currency_export_db");
                        result.setDto(CurrencyModel.getInstance().lookupCurrency());
                        result.setStatus(ResponseStatus.SUCCESS);
                    } else if (dto.getCommand() == MaintenanceCommand.LOAD) {
                        CurrencyModel.getInstance().loadCurrency();
                        result.setDto(CurrencyModel.getInstance().lookupCurrency());
                        result.setStatus(ResponseStatus.SUCCESS);
                    } else if (dto.getCommand() == MaintenanceCommand.CLEAR) {
                        CurrencyModel.getInstance().clear();
                        result.setDto(CurrencyModel.getInstance().lookupCurrency());
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
