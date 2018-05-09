package io.freestar.openssp.common.exchange.aerospike;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This handler uses cookie_sync.properties to obtain it's properties for Aerospike interaction
 */
public class PropertiesAerospikeCookieSyncHandler extends AerospikeCookieSyncHandler {
    private final static Logger LOG = LoggerFactory.getLogger(PropertiesAerospikeCookieSyncHandler.class);
    public static final String USER = "aerospike.user";
    public static final String PW = "aerospike.password";
    public static final String HOST = "aerospike.host";
    public static final String NAMESPACE = "aerospike.namespace";
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
        return (String) properties.get(USER);
    }

    @Override
    protected String getPassword() {
        return (String) properties.get(PW);
    }

    @Override
    protected String getHost() {
        return (String) properties.get(HOST);
    }

    @Override
    protected String getNamespace() {
        return (String) properties.get(NAMESPACE);
    }

    @Override
    protected String getSetName() {
        return SET_NAME;
    }

    public synchronized static PropertiesAerospikeCookieSyncHandler getInstance() {
        if (singleton == null) {
            singleton = new PropertiesAerospikeCookieSyncHandler();
        }
        return singleton;
    }
}
