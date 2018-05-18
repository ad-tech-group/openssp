package com.atg.openssp.dspSimUi.supplier;

import com.atg.openssp.common.demand.Supplier;
import com.atg.openssp.common.provider.LoginHandler;
import com.atg.openssp.dspSimUi.model.ModelException;
import com.atg.openssp.dspSimUi.model.client.*;
import com.atg.openssp.dspSimUi.model.supplier.SupplierModel;
import com.google.gson.Gson;
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
public class SupplierServerHandler implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(SupplierServerHandler.class);
    public static final String SUPPLIER_HOST = "supplier-host";
    public static final String SUPPLIER_PORT = "supplier-port";
    private final Thread t = new Thread(this);
    private final SupplierModel model;
    private boolean running;

    public SupplierServerHandler(SupplierModel model) {
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

    public void sendListCommand() throws ModelException {
        sendCommand(SupplierCommandType.LIST);
    }

    public void sendAddCommand(Supplier sb) throws ModelException {
        sendCommand(SupplierCommandType.ADD, sb);
    }

    public void sendUpdateCommand(Supplier sb) throws ModelException {
        sendCommand(SupplierCommandType.UPDATE, sb);
    }

    public void sendRemoveCommand(Long id) throws ModelException {
        Supplier s = new Supplier();
        s.setSupplierId(id);
        sendCommand(SupplierCommandType.REMOVE, s);
    }

    public void sendLoadCommand() throws ModelException {
        sendCommand(SupplierCommandType.LOAD);
    }

    public void sendImportCommand() throws ModelException {
        sendCommand(SupplierCommandType.IMPORT);
    }

    public void sendExportCommand() throws ModelException {
        sendCommand(SupplierCommandType.EXPORT);
    }

    public void sendClearCommand() throws ModelException {
        sendCommand(SupplierCommandType.CLEAR);
    }

    private void sendCommand(SupplierCommandType type) throws ModelException {
        sendCommand(type, null);
    }

    private void sendCommand(SupplierCommandType type, Supplier sb) throws ModelException {
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("http://"+model.lookupProperty(SUPPLIER_HOST, "localhost")+":"+model.lookupProperty(SUPPLIER_PORT, "9090")+"/ssp-services/maintain/supplier?t="+ LoginHandler.TOKEN);
            SupplierCommand command = new SupplierCommand();
            command.setCommand(type);
            command.setSupplier(sb);
            StringEntity entity = new StringEntity(new Gson().toJson(command));
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            CloseableHttpResponse response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                String json = EntityUtils.toString(response.getEntity(), "UTF-8");

                GsonBuilder builder = new GsonBuilder();
                Supplier.populateTypeAdapters(builder);
                SupplierResponse sr = builder.create().fromJson(json, SupplierResponse.class);
                if (sr.getStatus() == ResponseStatus.SUCCESS) {
                    model.handleList(sr.getSuppliers());
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
