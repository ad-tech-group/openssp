package com.atg.openssp.common.core.exchange.cookiesync;

import com.atg.openssp.common.configuration.GlobalContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * This class is used to interact with the cookie sync persistence mechanism if it is supported
 */
public class CookieSyncManager implements CookieSyncHandler {
    private final static Logger LOG = LoggerFactory.getLogger(CookieSyncManager.class);
    private static CookieSyncManager singleton;
    private CookieSyncHandler handler;

    private CookieSyncManager() {
        String handlerClassName = GlobalContext.getCookieSyncHandlerClass();
        if (handlerClassName == null) {
            handler = DefaultCookieSyncHandler.getInstance();
        } else {
            try {
                Class handlerClass = Class.forName(handlerClassName);
                Method m = handlerClass.getMethod("getInstance", new Class[]{});
                handler = (CookieSyncHandler) m.invoke(handlerClass, null);
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
                handler = DefaultCookieSyncHandler.getInstance();
            }
        }
    }

    @Override
    public CookieSyncDTO get(String uid) {
        CookieSyncDTO result = handler.get(uid);
        LOG.info("csm.get: "+uid+":"+result);
        return result;
    }

    @Override
    public void set(String uid, CookieSyncDTO dto) {
        LOG.info("csm.set: "+uid+":"+dto);
        handler.set(uid, dto);
    }

    @Override
    public boolean supportsCookieSync() {
        return handler.supportsCookieSync();
    }

    public synchronized static CookieSyncManager getInstance() {
        if (singleton == null) {
            singleton = new CookieSyncManager();
        }
        return singleton;
    }

}
