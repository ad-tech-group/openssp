package com.atg.openssp.dspSimUi.currency;

import com.atg.openssp.common.core.broker.dto.CurrencyDto;
import com.atg.openssp.common.model.EurRef;
import com.atg.openssp.common.provider.LoginHandler;
import com.atg.openssp.dspSimUi.model.ModelException;
import com.atg.openssp.dspSimUi.model.client.ResponseStatus;
import com.atg.openssp.dspSimUi.model.client.CurrencyCommand;
import com.atg.openssp.dspSimUi.model.client.CurrencyCommandType;
import com.atg.openssp.dspSimUi.model.client.CurrencyResponse;
import com.atg.openssp.dspSimUi.model.currency.CurrencyModel;
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
public class CurrencyServerHandler implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(CurrencyServerHandler.class);
    public static final String SITE_HOST = "currency-host";
    public static final String SITE_PORT = "currency-port";
    private final Thread t = new Thread(this);
    private final CurrencyModel model;
    private boolean running;

    public CurrencyServerHandler(CurrencyModel model) {
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
        sendCommand(CurrencyCommandType.LIST);
    }

    public void sendAddCommand(CurrencyDto sb) throws ModelException {
        sendCommand(CurrencyCommandType.ADD, sb);
    }

    public void sendUpdateCommand(CurrencyDto sb) throws ModelException {
        sendCommand(CurrencyCommandType.UPDATE, sb);
    }

    public void sendRemoveCommand(String currency) throws ModelException {
        CurrencyDto s = new CurrencyDto();
        s.setCurrency(currency);

        sendCommand(CurrencyCommandType.REMOVE, s);
    }

    public void sendLoadCommand() throws ModelException {
        sendCommand(CurrencyCommandType.LOAD);
    }

    public void sendImportCommand() throws ModelException {
        sendCommand(CurrencyCommandType.IMPORT);
    }

    public void sendExportCommand() throws ModelException {
        sendCommand(CurrencyCommandType.EXPORT);
    }

    public void sendClearCommand() throws ModelException {
        sendCommand(CurrencyCommandType.CLEAR);
    }

    private void sendCommand(CurrencyCommandType type) throws ModelException {
        sendCommand(type, null);
    }

    private void sendCommand(CurrencyCommandType type, CurrencyDto sb) throws ModelException {
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("http://"+model.lookupProperty(SITE_HOST, "localhost")+":"+model.lookupProperty(SITE_PORT, "9090")+"/ssp-services/maintain/eurref?t="+ LoginHandler.TOKEN);
            System.out.println(httpPost);
            CurrencyCommand command = new CurrencyCommand();
            command.setCommand(type);
            command.setCurrency(sb);
            StringEntity entity = new StringEntity(new Gson().toJson(command));
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            CloseableHttpResponse response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                String json = EntityUtils.toString(response.getEntity(), "UTF-8");
                CurrencyResponse sr = new Gson().fromJson(json, CurrencyResponse.class);
                if (sr.getStatus() == ResponseStatus.SUCCESS) {
                    model.handleList(sr.getDto());
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
