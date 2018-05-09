package com.atg.openssp.common.core.exchange.cookiesync;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * The default handler returns false when asked if it supports cookie sync, but implement the behavior in
 * a non-persistent map.
 */
public class DefaultCookieSyncHandler implements CookieSyncHandler {
    private final static Logger LOG = LoggerFactory.getLogger(DefaultCookieSyncHandler.class);
    private static DefaultCookieSyncHandler singleton;
    private HashMap<String, CookieSyncDTO> map = new HashMap<>();

    private DefaultCookieSyncHandler() {
    }

    @Override
    public CookieSyncDTO get(String key) {
        return map.get(key);
    }

    @Override
    public void set(String key, CookieSyncDTO dto) {
        map.put(key, dto);
    }

    @Override
    public boolean supportsCookieSync() {
        return false;
    }

    public synchronized static DefaultCookieSyncHandler getInstance() {
        if (singleton == null) {
            singleton = new DefaultCookieSyncHandler();
        }
        return singleton;
    }
}
