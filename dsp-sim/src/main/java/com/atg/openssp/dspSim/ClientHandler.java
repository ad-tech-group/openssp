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
import java.util.UUID;

public class ClientHandler implements HttpHandler {
    private static final Logger log = LoggerFactory.getLogger(ClientHandler.class);
    private final DspModel model;

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
        } else if (cc.getType() == ClientCommandType.ADD) {
            SimBidder sb = model.lookupBidder(cc.getId());
            if (sb == null) {
                sb = new SimBidder(cc.getId());
                sb.setPrice(cc.getPrice());
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
                cr.setStatus(ClientResponseStatus.FAILURE);
                cr.setReason("SimBidder already exists");
            }
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
                cr.setStatus(ClientResponseStatus.FAILURE);
                cr.setReason("SimBidder not found");
            } else {
                sb.setPrice(cc.getPrice());
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
        }
        String result = new Gson().toJson(cr);
        log.info("CR<--"+result);
        httpExchange.sendResponseHeaders(200, result.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(result.getBytes());
        os.close();

    }
}
