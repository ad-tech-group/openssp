package com.atg.openssp.dataprovider.provider.handler;

import com.atg.openssp.common.core.system.LocalContext;
import com.atg.openssp.common.provider.DataHandler;
import com.atg.openssp.common.provider.LoginHandler;
import com.atg.openssp.dataprovider.provider.dto.MaintenanceCommand;
import com.atg.openssp.dataprovider.provider.dto.ResponseStatus;
import com.atg.openssp.dataprovider.provider.dto.SiteMaintenanceDto;
import com.atg.openssp.dataprovider.provider.dto.SiteResponse;
import com.atg.openssp.dataprovider.provider.model.SiteModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import openrtb.bidrequest.model.Site;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author Brian Sorensen
 */
public class SiteDataMaintenanceHandler extends DataHandler {
    private static final Logger log = LoggerFactory.getLogger(SiteDataMaintenanceHandler.class);
    public static final String CONTEXT = "/maintain/site";

    public SiteDataMaintenanceHandler(HttpServletRequest request, HttpServletResponse response) {
        if (LocalContext.isSiteDataServiceEnabled()) {
            try {
                Map<String,String> parms = queryToMap(request.getQueryString());
                String t = parms.get("t");

                if (LoginHandler.TOKEN.equals(t)) {

                    GsonBuilder builder = new GsonBuilder();

                    builder.registerTypeAdapter(MaintenanceCommand.class, (JsonDeserializer<MaintenanceCommand>) (json, typeOfT, context)
                            -> MaintenanceCommand.valueOf(json.getAsString()));

                    Site.populateTypeAdapters(builder);

                    Gson gson = builder.create();

                    SiteMaintenanceDto dto = gson.fromJson(request.getReader(), SiteMaintenanceDto.class);

                    SiteResponse result = new SiteResponse();

                    if (dto.getCommand() == MaintenanceCommand.LIST) {
                        result.setStatus(ResponseStatus.SUCCESS);
                        result.setSites(SiteModel.getInstance().lookupSites().getSites());
                    } else if (dto.getCommand() == MaintenanceCommand.ADD) {
                        Site s = dto.getSite();
                        SiteModel.getInstance().insert(s);
                        result.setSites(SiteModel.getInstance().lookupSites().getSites());
                        result.setStatus(ResponseStatus.SUCCESS);
                    } else if (dto.getCommand() == MaintenanceCommand.REMOVE) {
                        Site s = dto.getSite();
                        SiteModel.getInstance().remove(s);
                        result.setSites(SiteModel.getInstance().lookupSites().getSites());
                        result.setStatus(ResponseStatus.SUCCESS);
                    } else if (dto.getCommand() == MaintenanceCommand.UPDATE) {
                        Site s = dto.getSite();
                        SiteModel.getInstance().update(s);
                        result.setSites(SiteModel.getInstance().lookupSites().getSites());
                        result.setStatus(ResponseStatus.SUCCESS);
                    } else if (dto.getCommand() == MaintenanceCommand.IMPORT) {
                        SiteModel.getInstance().importSites("site_export_db");
                        result.setSites(SiteModel.getInstance().lookupSites().getSites());
                        result.setStatus(ResponseStatus.SUCCESS);
                    } else if (dto.getCommand() == MaintenanceCommand.EXPORT) {
                        SiteModel.getInstance().exportSites("site_export_db");
                        result.setSites(SiteModel.getInstance().lookupSites().getSites());
                        result.setStatus(ResponseStatus.SUCCESS);
                    } else if (dto.getCommand() == MaintenanceCommand.LOAD) {
                        SiteModel.getInstance().loadSites();
                        result.setSites(SiteModel.getInstance().lookupSites().getSites());
                        result.setStatus(ResponseStatus.SUCCESS);
                    } else if (dto.getCommand() == MaintenanceCommand.CLEAR) {
                        SiteModel.getInstance().clear();
                        result.setSites(SiteModel.getInstance().lookupSites().getSites());
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
