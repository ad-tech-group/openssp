package com.atg.openssp.dspSimUi;

import com.atg.openssp.dspSimUi.model.ModelException;
import com.atg.openssp.dspSimUi.model.client.ServerCommand;
import com.atg.openssp.dspSimUi.model.client.ServerCommandType;
import com.atg.openssp.dspSimUi.model.client.ServerResponse;
import com.atg.openssp.dspSimUi.model.client.ResponseStatus;
import com.atg.openssp.dspSimUi.model.dsp.DspModel;
import com.atg.openssp.dspSimUi.model.dsp.SimBidder;
import com.google.gson.Gson;
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
public class ServerHandler implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(ServerHandler.class);
    public static final String SERVER_HOST = "server-host";
    public static final String SERVER_PORT = "server-port";
    private final Thread t = new Thread(this);
    private final DspModel model;
    private boolean running;

    public ServerHandler(DspModel model) {
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
        sendCommand(ServerCommandType.LIST);
    }

    public void sendRemoveCommand(String id) throws ModelException {
        sendCommand(ServerCommandType.REMOVE, id);
    }

    public void sendUpdateCommand(SimBidder sb) throws ModelException {
        sendCommand(ServerCommandType.UPDATE, sb.getId(), sb);
    }

    private void sendCommand(ServerCommandType type) throws ModelException {
        sendCommand(type, null, null);
    }

    private void sendCommand(ServerCommandType type, String id) throws ModelException {
        sendCommand(type, id, null);
    }

    private void sendCommand(ServerCommandType type, String id, SimBidder sb) throws ModelException {
        try {
            CloseableHttpClient client = HttpClients.createDefault();
//            HttpPost httpPost = new HttpPost("https://"+model.lookupProperty(SERVER_HOST, "localhost")+":"+model.lookupProperty(SERVER_PORT, "8081")+"/dsp-sim/admin");
            HttpPost httpPost = new HttpPost("http://"+model.lookupProperty(SERVER_HOST, "localhost")+":"+model.lookupProperty(SERVER_PORT, "8081")+"/dsp-sim/admin");
            ServerCommand command = new ServerCommand();
            command.setType(type);
            command.setId(id);
            command.setSimBidder(sb);
            StringEntity entity = new StringEntity(new Gson().toJson(command));
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            CloseableHttpResponse response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                String json = EntityUtils.toString(response.getEntity(), "UTF-8");
                ServerResponse sr = new Gson().fromJson(json, ServerResponse.class);
                if (sr.getStatus() == ResponseStatus.SUCCESS) {
                    model.handleList(sr.getBidders());
                    model.handleMode(sr.getMode());
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

    public void sendShutdownCommand() throws ModelException {
        sendCommand(ServerCommandType.SHUTDOWN);
    }

    public void sendRestartCommand() throws ModelException {
        sendCommand(ServerCommandType.RESTART);
    }

    public void sendNormalCommand() throws ModelException {
        sendCommand(ServerCommandType.RETURN_NORMAL);
    }

    public void sendReturnNoneCommand() throws ModelException {
        sendCommand(ServerCommandType.RETURN_NONE);
    }

    public void send400Command() throws ModelException {
        sendCommand(ServerCommandType.ONLY_400);
    }

    public void send500Command() throws ModelException {
        sendCommand(ServerCommandType.ONLY_500);
    }

}
