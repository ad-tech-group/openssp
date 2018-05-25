package com.atg.openssp.dspSimUi.model.currency;

import com.atg.openssp.common.core.broker.dto.CurrencyDto;
import com.atg.openssp.common.model.EurRef;
import com.atg.openssp.dspSimUi.model.BaseModel;
import com.atg.openssp.dspSimUi.model.ModelException;
import com.atg.openssp.dspSimUi.currency.CurrencyServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author Brian Sorensen
 */
public class CurrencyModel extends BaseModel {
    private static final Logger log = LoggerFactory.getLogger(CurrencyModel.class);
    private final ResourceBundle bundle;
    private final Properties props = new Properties();
    private DefaultListModel<CurrencyDto> mCurrency = new DefaultListModel<>();
    private final CurrencyServerHandler serverHandler;

    public CurrencyModel() throws ModelException {
        bundle = ResourceBundle.getBundle("CurrencyDisplayTemplate");
        loadProperties();
        serverHandler = new CurrencyServerHandler(this);
    }

    private void loadProperties() {
        try {
            File file = new File("currency-client.properties");
            InputStream is;
            if (file.exists()) {
                is = new FileInputStream(file);
            } else {
                is = getClass().getClassLoader().getSystemResourceAsStream(file.getName());
            }
            props.load(is);
            is.close();
        } catch (IOException e) {
            log.warn("Could not load properties file.", e);
        }
    }

    public Properties getProperties() {
        return props;
    }

    public String lookupProperty(String key) {
        return props.getProperty(key);
    }

    public String lookupProperty(String key, String defaultValue) {
        String v = lookupProperty(key);
        if (v == null) {
            return defaultValue;
        } else {
            return v;
        }
    }

    private HashMap<String, CurrencyDto> getCurrency() {
        HashMap<String, CurrencyDto> map = new HashMap<>();
        for (int i = 0; i < mCurrency.size(); i++) {
            map.put(mCurrency.get(i).getCurrency(), mCurrency.get(i));
        }
        return map;
    }

    public DefaultListModel<CurrencyDto> getCurrencyModel() {
        return mCurrency;
    }

    public void start() {
        serverHandler.start();
    }

    public void handleList(CurrencyDto dto) {
        HashMap<String, CurrencyDto> map = getCurrency();
        CurrencyDto check = map.get(dto.getCurrency());
        if (check == null) {
            mCurrency.addElement(dto);
        } else {
            map.remove(dto);
            check.setCurrency(dto.getCurrency());
            check.setData(dto.getData());
        }
        // if any are left, remove them as extras
        for (Map.Entry<String, CurrencyDto> tCurrency : map.entrySet()) {
            mCurrency.removeElement(tCurrency.getValue());
        }
    }

    public void sendAddCommand(CurrencyDto sb) throws ModelException {
        serverHandler.sendAddCommand(sb);
    }

    public void sendUpdateCommand(CurrencyDto sb) throws ModelException {
        serverHandler.sendUpdateCommand(sb);
    }

    public void sendRemoveCommand(String id) throws ModelException {
        serverHandler.sendRemoveCommand(id);
    }

    public void sendLoadCommand() throws ModelException {
        serverHandler.sendLoadCommand();
    }

    public void sendExportCommand() throws ModelException {
        serverHandler.sendExportCommand();
    }

    public void sendImportCommand() throws ModelException {
        serverHandler.sendImportCommand();
    }

    public void sendClearCommand() throws ModelException {
        serverHandler.sendClearCommand();
    }

    public String getTemplateText(String tag) {
        return bundle.getString(tag);
    }
}
