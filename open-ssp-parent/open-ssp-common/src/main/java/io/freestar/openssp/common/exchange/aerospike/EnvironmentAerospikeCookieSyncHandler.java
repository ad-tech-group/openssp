package io.freestar.openssp.common.exchange.aerospike;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This handler uses environment variables to obtain it's properties for Aerospike interaction
 */
public class EnvironmentAerospikeCookieSyncHandler extends AerospikeCookieSyncHandler {
    private final static Logger LOG = LoggerFactory.getLogger(EnvironmentAerospikeCookieSyncHandler.class);
    public static final String USER = "AEROSPIKE_USER";
    public static final String PW = "AEROSPIKE_PASSWORD";
    public static final String HOST = "AEROSPIKE_HOST";
    public static final String NAMESPACE = "AEROSPIKE_NAMESPACE_SSP";
    public static EnvironmentAerospikeCookieSyncHandler singleton;

    private EnvironmentAerospikeCookieSyncHandler() {
        LOG.info("loaded: "+getClass().getSimpleName());
    }

    @Override
    protected String getUser() {
        return System.getenv(USER);
    }

    @Override
    protected String getPassword() {
        return System.getenv(PW);
    }

    @Override
    protected String getHost() {
        return System.getenv(HOST);
    }

    @Override
    protected String getNamespace() {
        return System.getenv(NAMESPACE);
    }

    @Override
    protected String getSetName() {
        return SET_NAME;
    }

    public synchronized static EnvironmentAerospikeCookieSyncHandler getInstance() {
        if (singleton == null) {
            singleton = new EnvironmentAerospikeCookieSyncHandler();
        }
        return singleton;
    }
}
