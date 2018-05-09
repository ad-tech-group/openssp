package com.atg.openssp.common.core.exchange.cookiesync;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class is used to store the map of DSP cookie information mappings
 */
public class CookieSyncDTO {
    private LinkedHashMap<String, DspCookieDto> dsp_uids = new LinkedHashMap<>();
    /**
     * The uid
     */
    private String uid;
    /**
     * Flag used to indicate persistence is required
     */
    private transient boolean dirty;

    public void setUid(String uid) {
        this.uid = uid;
        dirty = true;
    }

    public String getUid() {
        return uid;
    }

    public DspCookieDto lookup(String dspName) {
        return dsp_uids.get(dspName);
    }

    public void add(DspCookieDto dto) {
        dsp_uids.put(dto.getShortName(), dto);
        dirty = true;
    }

    public boolean isDirty() {
        for (Map.Entry<String, DspCookieDto> e : dsp_uids.entrySet()) {
            dirty = dirty || e.getValue().isDirty();
        }
        return dirty;
    }

    public void clearDirty() {
        for (Map.Entry<String, DspCookieDto> e : dsp_uids.entrySet()) {
            e.getValue().clearDirty();
        }
        dirty = false;
    }

    @Override
    public String toString() {
        return uid+"::"+dsp_uids;
    }
}
