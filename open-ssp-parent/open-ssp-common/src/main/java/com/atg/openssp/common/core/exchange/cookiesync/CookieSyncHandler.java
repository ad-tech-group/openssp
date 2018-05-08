package com.atg.openssp.common.core.exchange.cookiesync;

public interface CookieSyncHandler {

    CookieSyncDTO get(String uid);

    void set(String uid, CookieSyncDTO dto);

    boolean supportsCookieSync();

}
