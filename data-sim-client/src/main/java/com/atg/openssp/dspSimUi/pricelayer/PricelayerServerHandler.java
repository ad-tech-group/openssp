package com.atg.openssp.dspSimUi.pricelayer;

import com.atg.openssp.common.provider.LoginHandler;
import com.atg.openssp.dspSimUi.model.ModelException;
import com.atg.openssp.dspSimUi.model.client.ResponseStatus;
import com.atg.openssp.dspSimUi.model.client.PricelayerCommand;
import com.atg.openssp.dspSimUi.model.client.PricelayerCommandType;
import com.atg.openssp.dspSimUi.model.client.PricelayerResponse;
import com.atg.openssp.dspSimUi.model.pricelayer.PricelayerModel;
import com.google.gson.Gson;
import openrtb.bidrequest.model.Pricelayer;
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
public class PricelayerServerHandler implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(PricelayerServerHandler.class);
    public static final String SITE_HOST = "pricelayer-host";
    public static final String SITE_PORT = "pricelayer-port";
    private final Thread t = new Thread(this);
    private final PricelayerModel model;
    private boolean running;

    public PricelayerServerHandler(PricelayerModel model) {
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
        sendCommand(PricelayerCommandType.LIST);
    }

    public void sendAddCommand(Pricelayer sb) throws ModelException {
        sendCommand(PricelayerCommandType.ADD, sb);
    }

    public void sendUpdateCommand(Pricelayer sb) throws ModelException {
        sendCommand(PricelayerCommandType.UPDATE, sb);
    }

    public void sendRemoveCommand(String id) throws ModelException {
        Pricelayer s = new Pricelayer();
        s.setSiteid(id);
        sendCommand(PricelayerCommandType.REMOVE, s);
    }

    public void sendLoadCommand() throws ModelException {
        sendCommand(PricelayerCommandType.LOAD);
    }

    public void sendImportCommand() throws ModelException {
        sendCommand(PricelayerCommandType.IMPORT);
    }

    public void sendExportCommand() throws ModelException {
        sendCommand(PricelayerCommandType.EXPORT);
    }

    public void sendClearCommand() throws ModelException {
        sendCommand(PricelayerCommandType.CLEAR);
    }

    private void sendCommand(PricelayerCommandType type) throws ModelException {
        sendCommand(type, null);
    }

    private void sendCommand(PricelayerCommandType type, Pricelayer sb) throws ModelException {
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("http://"+model.lookupProperty(SITE_HOST, "localhost")+":"+model.lookupProperty(SITE_PORT, "9090")+"/ssp-services/maintain/pricelayer?t="+ LoginHandler.TOKEN);
            System.out.println(httpPost);
            PricelayerCommand command = new PricelayerCommand();
            command.setCommand(type);
            command.setPricelayer(sb);
            StringEntity entity = new StringEntity(new Gson().toJson(command));
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            CloseableHttpResponse response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                String json = EntityUtils.toString(response.getEntity(), "UTF-8");
                PricelayerResponse sr = new Gson().fromJson(json, PricelayerResponse.class);
                if (sr.getStatus() == ResponseStatus.SUCCESS) {
                    model.handleList(sr.getPricelayers());
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
