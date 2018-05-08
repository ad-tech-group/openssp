package io.freestar.openssp.common.exchange.aerospike;

import com.atg.openssp.common.core.exchange.cookiesync.CookieSyncDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesAerospikeCookieSyncHandler extends AerospikeCookieSyncHandler {
    private final static Logger LOG = LoggerFactory.getLogger(PropertiesAerospikeCookieSyncHandler.class);
    private static PropertiesAerospikeCookieSyncHandler singleton;
    private static Properties properties = new Properties();
    static {
        try {
            InputStream is = PropertiesAerospikeCookieSyncHandler.class.getClassLoader().getResourceAsStream("cookie_sync.properties");
            if (is != null) {

                properties.load(is);
                is.close();
            }
        } catch (IOException e) {
            LOG.error("cookie_sync.properties", e);
        }
    }

    private PropertiesAerospikeCookieSyncHandler() {
        LOG.info("loaded: "+getClass().getSimpleName());
    }

    @Override
    protected String getUser() {
        return (String) properties.get("aerospike.user");
    }

    @Override
    protected String getPassword() {
        return (String) properties.get("aerospike.password");
    }

    @Override
    protected String getHost() {
        return (String) properties.get("aerospike.host");
    }

    @Override
    protected String getNamespace() {
        return (String) properties.get("aerospike.namespace");
    }

    @Override
    protected String getSetName() {
        return "cookie_sync";
    }

    @Override
    protected Class getGsonConversionClass() {
        return CookieSyncDTO.class;
    }

    public synchronized static PropertiesAerospikeCookieSyncHandler getInstance() {
        if (singleton == null) {
            singleton = new PropertiesAerospikeCookieSyncHandler();
        }
        return singleton;
    }
}
