package com.atg.openssp.dataprovider.provider.handler;

import com.atg.openssp.common.cache.dto.VideoAd;
import com.atg.openssp.common.core.system.LocalContext;
import com.atg.openssp.common.provider.DataHandler;
import com.atg.openssp.common.provider.LoginHandler;
import com.atg.openssp.dataprovider.provider.dto.VideoAdMaintenanceDto;
import com.atg.openssp.dataprovider.provider.dto.VideoAdResponse;
import com.atg.openssp.dataprovider.provider.dto.MaintenanceCommand;
import com.atg.openssp.dataprovider.provider.dto.ResponseStatus;
import com.atg.openssp.dataprovider.provider.model.VideoAdModel;
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
public class VideoAdDataMaintenanceHandler extends DataHandler {
    private static final Logger log = LoggerFactory.getLogger(VideoAdDataMaintenanceHandler.class);
    public static final String CONTEXT = "/maintain/videoAds";

    public VideoAdDataMaintenanceHandler(HttpServletRequest request, HttpServletResponse response) {
        if (LocalContext.isVideoAdDataServiceEnabled()) {
            try {
                Map<String,String> parms = queryToMap(request.getQueryString());
                String t = parms.get("t");

                if (LoginHandler.TOKEN.equals(t)) {

                    GsonBuilder builder = new GsonBuilder();

                    builder.registerTypeAdapter(MaintenanceCommand.class, (JsonDeserializer<MaintenanceCommand>) (json, typeOfT, context)
                            -> MaintenanceCommand.valueOf(json.getAsString()));

                    Gson gson = builder.create();

                    VideoAdMaintenanceDto dto = gson.fromJson(request.getReader(), VideoAdMaintenanceDto.class);

                    VideoAdResponse result = new VideoAdResponse();

                    if (dto.getCommand() == MaintenanceCommand.LIST) {
                        result.setStatus(ResponseStatus.SUCCESS);
                        result.setVideoAds(VideoAdModel.getInstance().lookupDto().getVideoAds());
                    } else if (dto.getCommand() == MaintenanceCommand.ADD) {
                        VideoAd s = dto.getVideoAd();
                        VideoAdModel.getInstance().insert(s);
                        result.setVideoAds(VideoAdModel.getInstance().lookupDto().getVideoAds());
                        result.setStatus(ResponseStatus.SUCCESS);
                    } else if (dto.getCommand() == MaintenanceCommand.REMOVE) {
                        VideoAd s = dto.getVideoAd();
                        VideoAdModel.getInstance().remove(s);
                        result.setVideoAds(VideoAdModel.getInstance().lookupDto().getVideoAds());
                        result.setStatus(ResponseStatus.SUCCESS);
                    } else if (dto.getCommand() == MaintenanceCommand.UPDATE) {
                        VideoAd s = dto.getVideoAd();
                        VideoAdModel.getInstance().update(s);
                        result.setVideoAds(VideoAdModel.getInstance().lookupDto().getVideoAds());
                        result.setStatus(ResponseStatus.SUCCESS);
                    } else if (dto.getCommand() == MaintenanceCommand.IMPORT) {
                        VideoAdModel.getInstance().importVideoAds("videoAds_export_db");
                        result.setVideoAds(VideoAdModel.getInstance().lookupDto().getVideoAds());
                        result.setStatus(ResponseStatus.SUCCESS);
                    } else if (dto.getCommand() == MaintenanceCommand.EXPORT) {
                        VideoAdModel.getInstance().exportVideoAds("videoAds_export_db");
                        result.setVideoAds(VideoAdModel.getInstance().lookupDto().getVideoAds());
                        result.setStatus(ResponseStatus.SUCCESS);
                    } else if (dto.getCommand() == MaintenanceCommand.LOAD) {
                        VideoAdModel.getInstance().loadVideoAds();
                        result.setVideoAds(VideoAdModel.getInstance().lookupDto().getVideoAds());
                        result.setStatus(ResponseStatus.SUCCESS);
                    } else if (dto.getCommand() == MaintenanceCommand.CLEAR) {
                        VideoAdModel.getInstance().clear();
                        result.setVideoAds(VideoAdModel.getInstance().lookupDto().getVideoAds());
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
