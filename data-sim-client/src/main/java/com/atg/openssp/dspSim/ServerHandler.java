package com.atg.openssp.dspSim;

import com.atg.openssp.dspSim.model.ModelException;
import com.atg.openssp.dspSim.model.client.ServerCommand;
import com.atg.openssp.dspSim.model.client.ServerCommandType;
import com.atg.openssp.dspSim.model.client.ServerResponse;
import com.atg.openssp.dspSim.model.client.ServerResponseStatus;
import com.atg.openssp.dspSim.model.dsp.DspModel;
import com.atg.openssp.dspSim.model.dsp.SimBidder;
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
        sendCommand(ServerCommandType.LIST, "", 0);
    }

    public void sendAddCommand(SimBidder sb) throws ModelException {
        sendCommand(ServerCommandType.ADD, sb.getId(), sb.getPrice());
    }

    public void sendRemoveCommand(SimBidder sb) throws ModelException {
        sendCommand(ServerCommandType.REMOVE, sb.getId(), sb.getPrice());
    }

    public void sendUpdateCommand(SimBidder sb, float newPrice) throws ModelException {
        sendCommand(ServerCommandType.UPDATE, sb.getId(), newPrice);
    }

    private void sendCommand(ServerCommandType type, String bidderId, float price) throws ModelException {
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("http://localhost:8082/dsp-sim/admin");
            ServerCommand command = new ServerCommand();
            command.setType(type);
            command.setId(bidderId);
            command.setPrice(price);
            StringEntity entity = new StringEntity(new Gson().toJson(command));
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            CloseableHttpResponse response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                String json = EntityUtils.toString(response.getEntity(), "UTF-8");
                ServerResponse sr = new Gson().fromJson(json, ServerResponse.class);
                if (sr.getStatus() == ServerResponseStatus.SUCCESS) {
                    model.handleList(sr.getBidders());
                } else {
                    throw new ModelException(type+" command failed with error: " + sr.getReason());
                }
            } else {
                throw new ModelException(type+" call failed with http error: " + response.getStatusLine().getStatusCode());
            }
            client.close();
        } catch (IOException e) {
            throw new ModelException(e.getMessage());
        }
    }

}
