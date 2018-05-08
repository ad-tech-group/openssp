package io.freestar.openssp.common.exchange.aerospike;

import com.atg.openssp.common.core.exchange.cookiesync.CookieSyncDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnvironmentAerospikeCookieSyncHandler extends AerospikeCookieSyncHandler {
    private final static Logger LOG = LoggerFactory.getLogger(EnvironmentAerospikeCookieSyncHandler.class);
    private static EnvironmentAerospikeCookieSyncHandler singleton;

    private EnvironmentAerospikeCookieSyncHandler() {
        LOG.info("loaded: "+getClass().getSimpleName());
    }

    @Override
    protected String getUser() {
        return System.getenv("AEROSPIKE_USER");
    }

    @Override
    protected String getPassword() {
        return System.getenv("AEROSPIKE_PASSWORD");
    }

    @Override
    protected String getHost() {
        return System.getenv("AEROSPIKE_HOST");
    }

    @Override
    protected String getNamespace() {
        return System.getenv("AEROSPIKE_NAMESPACE_SSP");
    }

    @Override
    protected String getSetName() {
        return "cookie_sync";
    }

    @Override
    protected Class getGsonConversionClass() {
        return CookieSyncDTO.class;
    }

    public synchronized static EnvironmentAerospikeCookieSyncHandler getInstance() {
        if (singleton == null) {
            singleton = new EnvironmentAerospikeCookieSyncHandler();
        }
        return singleton;
    }
}
