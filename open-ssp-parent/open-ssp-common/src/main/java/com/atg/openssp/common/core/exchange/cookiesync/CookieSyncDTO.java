package com.atg.openssp.common.core.exchange.cookiesync;

import java.util.LinkedHashMap;
import java.util.Map;

public class CookieSyncDTO {
    private LinkedHashMap<String, DspCookieDto> dsp_uids = new LinkedHashMap<>();
    private String fsuid;
    private transient boolean dirty;

    public void setFsuid(String fsuid) {
        this.fsuid = fsuid;
        dirty = true;
    }

    public String getFsuid() {
        return fsuid;
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
}
