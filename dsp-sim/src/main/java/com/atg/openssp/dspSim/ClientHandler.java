package com.atg.openssp.dspSim;

import com.atg.openssp.dspSim.model.ModelException;
import com.atg.openssp.dspSim.model.client.ClientCommand;
import com.atg.openssp.dspSim.model.client.ClientCommandType;
import com.atg.openssp.dspSim.model.client.ClientResponse;
import com.atg.openssp.dspSim.model.client.ClientResponseStatus;
import com.atg.openssp.dspSim.model.dsp.DspModel;
import com.atg.openssp.dspSim.model.dsp.SimBidder;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Brian Sorensen
 */
public class ClientHandler extends TimerTask implements HttpHandler {
    private static final Logger log = LoggerFactory.getLogger(ClientHandler.class);
    private final DspModel model;
    private int exitCode;

    public ClientHandler(DspModel model) {
        this.model = model;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        ClientCommand cc = new Gson().fromJson(new InputStreamReader(httpExchange.getRequestBody()), ClientCommand.class);
        log.info("CC-->"+new Gson().toJson(cc));
        ClientResponse cr = new ClientResponse();
        if (cc == null) {
            cr.setStatus(ClientResponseStatus.FAILURE);
            cr.setReason("Command not found");
            cr.setBidders(model.getBidders());
        } else if (cc.getType() == ClientCommandType.LIST) {
            cr.setStatus(ClientResponseStatus.SUCCESS);
            cr.setBidders(model.getBidders());
        } else if (cc.getType() == ClientCommandType.REMOVE) {
            SimBidder sb = model.lookupBidder(cc.getId());
            if (sb == null) {
                cr.setStatus(ClientResponseStatus.FAILURE);
                cr.setReason("SimBidder does not exists");
            } else {
                model.remove(sb);
                try {
                    model.saveModel();
                    cr.setStatus(ClientResponseStatus.SUCCESS);
                } catch (ModelException e) {
                    log.error(e.getMessage(), e);
                    cr.setStatus(ClientResponseStatus.FAILURE);
                    cr.setReason("remove save failed: "+e.getMessage());
                }
            }
            cr.setBidders(model.getBidders());
        } else if (cc.getType() == ClientCommandType.UPDATE) {
            SimBidder sb = model.lookupBidder(cc.getId());
            if (sb == null) {
                // does not exist, need to add it
                sb = new SimBidder(cc.getId());
                sb.populate(cc.getSimBidder());
                model.add(sb);
                try {
                    model.saveModel();
                    cr.setStatus(ClientResponseStatus.SUCCESS);
                } catch (ModelException e) {
                    log.error(e.getMessage(), e);
                    cr.setStatus(ClientResponseStatus.FAILURE);
                    cr.setReason("add save failed: "+e.getMessage());
                }
            } else {
                sb.populate(cc.getSimBidder());
                cr.setStatus(ClientResponseStatus.SUCCESS);
                try {
                    model.saveModel();
                } catch (ModelException e) {
                    log.error(e.getMessage(), e);
                    cr.setStatus(ClientResponseStatus.FAILURE);
                    cr.setReason("update save failed: "+e.getMessage());
                }
            }
            cr.setBidders(model.getBidders());
        } else if (cc.getType() == ClientCommandType.RESTART) {
            cr.setStatus(ClientResponseStatus.SUCCESS);
            exitCode = 5;
            Timer t = new Timer();
            t.schedule(this, 2000);
            cr.setStatus(ClientResponseStatus.SUCCESS);
        } else if (cc.getType() == ClientCommandType.SHUTDOWN) {
            exitCode = 0;
            Timer t = new Timer();
            t.schedule(this, 2000);
            cr.setStatus(ClientResponseStatus.SUCCESS);
        } else if (cc.getType() == ClientCommandType.RETURN_NORMAL) {
            model.setMode(cc.getType());
            cr.setStatus(ClientResponseStatus.SUCCESS);
        } else if (cc.getType() == ClientCommandType.RETURN_NONE) {
            model.setMode(cc.getType());
            cr.setStatus(ClientResponseStatus.SUCCESS);
        } else if (cc.getType() == ClientCommandType.ONLY_400) {
            model.setMode(cc.getType());
            cr.setStatus(ClientResponseStatus.SUCCESS);
        } else if (cc.getType() == ClientCommandType.ONLY_500) {
            model.setMode(cc.getType());
            cr.setStatus(ClientResponseStatus.SUCCESS);
        }

        cr.setMode(model.getMode());
        String result = new Gson().toJson(cr);
        log.info("CR<--"+result);
        httpExchange.sendResponseHeaders(200, result.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(result.getBytes());
        os.close();

    }

    @Override
    public void run() {
        System.exit(exitCode);
    }
}
