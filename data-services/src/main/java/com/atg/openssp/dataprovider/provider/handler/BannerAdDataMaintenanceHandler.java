package com.atg.openssp.dataprovider.provider.handler;

import com.atg.openssp.common.cache.dto.BannerAd;
import com.atg.openssp.common.core.system.LocalContext;
import com.atg.openssp.common.provider.DataHandler;
import com.atg.openssp.common.provider.LoginHandler;
import com.atg.openssp.dataprovider.provider.dto.MaintenanceCommand;
import com.atg.openssp.dataprovider.provider.dto.ResponseStatus;
import com.atg.openssp.dataprovider.provider.dto.BannerAdMaintenanceDto;
import com.atg.openssp.dataprovider.provider.dto.BannerAdResponse;
import com.atg.openssp.dataprovider.provider.model.BannerAdModel;
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
public class BannerAdDataMaintenanceHandler extends DataHandler {
    private static final Logger log = LoggerFactory.getLogger(BannerAdDataMaintenanceHandler.class);
    public static final String CONTEXT = "/maintain/bannerAds";

    public BannerAdDataMaintenanceHandler(HttpServletRequest request, HttpServletResponse response) {
        if (LocalContext.isBannerAdDataServiceEnabled()) {
            try {
                Map<String,String> parms = queryToMap(request.getQueryString());
                String t = parms.get("t");

                if (LoginHandler.TOKEN.equals(t)) {

                    GsonBuilder builder = new GsonBuilder();

                    builder.registerTypeAdapter(MaintenanceCommand.class, (JsonDeserializer<MaintenanceCommand>) (json, typeOfT, context)
                            -> MaintenanceCommand.valueOf(json.getAsString()));

                    Gson gson = builder.create();

                    BannerAdMaintenanceDto dto = gson.fromJson(request.getReader(), BannerAdMaintenanceDto.class);

                    BannerAdResponse result = new BannerAdResponse();

                    if (dto.getCommand() == MaintenanceCommand.LIST) {
                        result.setStatus(ResponseStatus.SUCCESS);
                        result.setBannerAds(BannerAdModel.getInstance().lookupDto().getBannerAds());
                    } else if (dto.getCommand() == MaintenanceCommand.ADD) {
                        BannerAd s = dto.getBannerAd();
                        BannerAdModel.getInstance().insert(s);
                        result.setBannerAds(BannerAdModel.getInstance().lookupDto().getBannerAds());
                        result.setStatus(ResponseStatus.SUCCESS);
                    } else if (dto.getCommand() == MaintenanceCommand.REMOVE) {
                        BannerAd s = dto.getBannerAd();
                        BannerAdModel.getInstance().remove(s);
                        result.setBannerAds(BannerAdModel.getInstance().lookupDto().getBannerAds());
                        result.setStatus(ResponseStatus.SUCCESS);
                    } else if (dto.getCommand() == MaintenanceCommand.UPDATE) {
                        BannerAd s = dto.getBannerAd();
                        BannerAdModel.getInstance().update(s);
                        result.setBannerAds(BannerAdModel.getInstance().lookupDto().getBannerAds());
                        result.setStatus(ResponseStatus.SUCCESS);
                    } else if (dto.getCommand() == MaintenanceCommand.IMPORT) {
                        BannerAdModel.getInstance().importBannerAds("bannerAds_export_db");
                        result.setBannerAds(BannerAdModel.getInstance().lookupDto().getBannerAds());
                        result.setStatus(ResponseStatus.SUCCESS);
                    } else if (dto.getCommand() == MaintenanceCommand.EXPORT) {
                        BannerAdModel.getInstance().exportBannerAds("bannerAds_export_db");
                        result.setBannerAds(BannerAdModel.getInstance().lookupDto().getBannerAds());
                        result.setStatus(ResponseStatus.SUCCESS);
                    } else if (dto.getCommand() == MaintenanceCommand.LOAD) {
                        BannerAdModel.getInstance().loadBannerAds();
                        result.setBannerAds(BannerAdModel.getInstance().lookupDto().getBannerAds());
                        result.setStatus(ResponseStatus.SUCCESS);
                    } else if (dto.getCommand() == MaintenanceCommand.CLEAR) {
                        BannerAdModel.getInstance().clear();
                        result.setBannerAds(BannerAdModel.getInstance().lookupDto().getBannerAds());
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
