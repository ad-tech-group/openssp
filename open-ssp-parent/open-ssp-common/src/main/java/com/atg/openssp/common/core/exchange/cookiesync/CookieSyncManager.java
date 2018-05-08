package com.atg.openssp.common.core.exchange.cookiesync;

import com.atg.openssp.common.configuration.GlobalContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class CookieSyncManager {
    private final static Logger log = LoggerFactory.getLogger(CookieSyncManager.class);
    private static CookieSyncManager singleton;
    private CookieSyncHandler handler;

    private CookieSyncManager() {
        System.out.println("BKS-A");
        String handlerClassName = GlobalContext.getCookieSyncHandlerClass();
        System.out.println("BKS-B "+handlerClassName);
        if (handlerClassName == null) {
            System.out.println("BKS-C");
            handler = DefaultCookieSyncHandler.getInstance();
        } else {
            try {
                System.out.println("BKS-D");
                Class handlerClass = Class.forName(handlerClassName);
                System.out.println("BKS-E");
                Method m = handlerClass.getMethod("getInstance", new Class[]{});
                System.out.println("BKS-F");
                handler = (CookieSyncHandler) m.invoke(handlerClass, null);
            } catch (Exception e) {
                System.out.println("BKS-G");
                log.error(e.getMessage(), e);
                handler = DefaultCookieSyncHandler.getInstance();
            }
            System.out.println("BKS-H");
        }
        System.out.println("BKS-I");
    }

    public CookieSyncDTO get(String uid) {
        return handler.get(uid);
    }

    public void set(String uid, CookieSyncDTO dto) {
        handler.set(uid, dto);
    }

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
