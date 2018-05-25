package com.atg.openssp.dspSimUi.site;

import com.atg.openssp.common.provider.LoginHandler;
import com.atg.openssp.dspSimUi.model.ModelException;
import com.atg.openssp.dspSimUi.model.client.*;
import com.atg.openssp.dspSimUi.model.site.SiteModel;
import com.google.gson.Gson;
import openrtb.bidrequest.model.Site;
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
public class SiteServerHandler implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(SiteServerHandler.class);
    public static final String SITE_HOST = "site-host";
    public static final String SITE_PORT = "site-port";
    private final Thread t = new Thread(this);
    private final SiteModel model;
    private boolean running;

    public SiteServerHandler(SiteModel model) {
        this.model = model;
        t.setName("ServerHandler");
        t.setDaemon(true);
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
        sendCommand(SiteCommandType.LIST);
    }

    public void sendAddCommand(Site sb) throws ModelException {
        sendCommand(SiteCommandType.ADD, sb);
    }

    public void sendUpdateCommand(Site sb) throws ModelException {
        sendCommand(SiteCommandType.UPDATE, sb);
    }

    public void sendRemoveCommand(String id) throws ModelException {
        Site s = new Site();
        s.setId(id);
        sendCommand(SiteCommandType.REMOVE, s);
    }

    public void sendLoadCommand() throws ModelException {
        sendCommand(SiteCommandType.LOAD);
    }

    public void sendImportCommand() throws ModelException {
        sendCommand(SiteCommandType.IMPORT);
    }

    public void sendExportCommand() throws ModelException {
        sendCommand(SiteCommandType.EXPORT);
    }

    public void sendClearCommand() throws ModelException {
        sendCommand(SiteCommandType.CLEAR);
    }

    private void sendCommand(SiteCommandType type) throws ModelException {
        sendCommand(type, null);
    }

    private void sendCommand(SiteCommandType type, Site sb) throws ModelException {
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("http://"+model.lookupProperty(SITE_HOST, "localhost")+":"+model.lookupProperty(SITE_PORT, "9090")+"/ssp-services/maintain/site?t="+ LoginHandler.TOKEN);
            System.out.println(httpPost);
            SiteCommand command = new SiteCommand();
            command.setCommand(type);
            command.setSite(sb);
            StringEntity entity = new StringEntity(new Gson().toJson(command));
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            CloseableHttpResponse response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                String json = EntityUtils.toString(response.getEntity(), "UTF-8");
                SiteResponse sr = new Gson().fromJson(json, SiteResponse.class);
                if (sr.getStatus() == ResponseStatus.SUCCESS) {
                    model.handleList(sr.getSites());
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
