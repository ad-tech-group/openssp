package com.atg.openssp.dspSimUi.bannerAd;

import com.atg.openssp.common.cache.dto.BannerAd;
import com.atg.openssp.common.provider.LoginHandler;
import com.atg.openssp.dspSimUi.model.ModelException;
import com.atg.openssp.dspSimUi.model.client.ResponseStatus;
import com.atg.openssp.dspSimUi.model.client.BannerAdCommand;
import com.atg.openssp.dspSimUi.model.client.BannerAdCommandType;
import com.atg.openssp.dspSimUi.model.client.BannerAdResponse;
import com.atg.openssp.dspSimUi.model.ad.banner.BannerAdModel;
import com.google.gson.GsonBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Brian Sorensen
 */
public class BannerAdServerHandler implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(BannerAdServerHandler.class);
    public static final String SITE_HOST = "bannerAd-host";
    public static final String SITE_PORT = "bannerAd-port";
    private final Thread t = new Thread(this);
    private final BannerAdModel model;
    private final GsonBuilder builder;
    private boolean running;

    public BannerAdServerHandler(BannerAdModel model) {
        this.model = model;
        t.setName("ServerHandler");
        t.setDaemon(true);

        builder = new GsonBuilder();
    }

    public void start() {
        if (!running) {
            t.start();
        }
    }

    @Override
    public void run() {
        running = true;
        while(running) {
            try {
                sendListCommand();
            } catch (ModelException e) {
                log.warn(e.getMessage(), e);
            }
            try {
                Thread.sleep(90000);
            } catch (InterruptedException e) {
                running = false;
            }
            Thread.yield();
        }
    }

    private void sendListCommand() throws ModelException {
        sendCommand(BannerAdCommandType.LIST);
    }

    public void sendAddCommand(BannerAd sb) throws ModelException {
        sendCommand(BannerAdCommandType.ADD, sb);
    }

    public void sendUpdateCommand(BannerAd sb) throws ModelException {
        sendCommand(BannerAdCommandType.UPDATE, sb);
    }

    public void sendRemoveCommand(String id) throws ModelException {
        BannerAd s = new BannerAd();
        s.setId(id);
        sendCommand(BannerAdCommandType.REMOVE, s);
    }

    public void sendLoadCommand() throws ModelException {
        sendCommand(BannerAdCommandType.LOAD);
    }

    public void sendImportCommand() throws ModelException {
        sendCommand(BannerAdCommandType.IMPORT);
    }

    public void sendExportCommand() throws ModelException {
        sendCommand(BannerAdCommandType.EXPORT);
    }

    public void sendClearCommand() throws ModelException {
        sendCommand(BannerAdCommandType.CLEAR);
    }

    private void sendCommand(BannerAdCommandType type) throws ModelException {
        sendCommand(type, null);
    }

    private void sendCommand(BannerAdCommandType type, BannerAd sb) throws ModelException {
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("http://"+model.lookupProperty(SITE_HOST, "localhost")+":"+model.lookupProperty(SITE_PORT, "9090")+"/ssp-services/maintain/bannerAds?t="+ LoginHandler.TOKEN);
            System.out.println(httpPost);
            BannerAdCommand command = new BannerAdCommand();
            command.setCommand(type);
            command.setBannerAd(sb);
            String jsonOut = builder.create().toJson(command);
            System.out.println(jsonOut);
            StringEntity entity = new StringEntity(jsonOut);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            CloseableHttpResponse response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                String json = EntityUtils.toString(response.getEntity(), "UTF-8");
                BannerAdResponse sr = builder.create().fromJson(json, BannerAdResponse.class);
                if (sr.getStatus() == ResponseStatus.SUCCESS) {
                    model.handleList(sr.getBannerAds());
                } else {
                    String m = type+" command failed with error: " + sr.getReason();
                    model.setMessageAsFault(m);
                    throw new ModelException(m);
                }
            } else {
                String m = type+" call failed with http error: " + response.getStatusLine().getStatusCode();
                model.setMessageAsFault(m);
                throw new ModelException(m);
            }
            client.close();
        } catch (IOException e) {
            model.setMessageAsFault("Could not access server: "+e.getMessage());
            throw new ModelException(e.getMessage());
        }
    }

}
