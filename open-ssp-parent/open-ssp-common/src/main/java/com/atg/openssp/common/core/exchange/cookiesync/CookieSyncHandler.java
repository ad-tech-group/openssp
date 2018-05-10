package com.atg.openssp.common.core.exchange.cookiesync;

public interface CookieSyncHandler {

    /**
     * Lookup a CookieSyncDTO object using the given user id;
     * @param uid
     * @return
     */
    CookieSyncDTO get(String uid);

    /**
     * Persist a CookieSyncDTO object for the given user id;
     * @param uid
     * @param dto
     */
    void set(String uid, CookieSyncDTO dto);

    boolean supportsCookieSync();

}
