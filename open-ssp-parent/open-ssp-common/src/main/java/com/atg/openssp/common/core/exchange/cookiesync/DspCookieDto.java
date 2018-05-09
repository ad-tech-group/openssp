package com.atg.openssp.common.core.exchange.cookiesync;

public class DspCookieDto {
    private String uid;
    private String shortName;
    private transient boolean dirty;

    public void setUid(String uid) {
        this.uid = uid;
        dirty = true;
    }

    public String getUid() {
        return uid;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
        dirty = true;
    }

    public String getShortName() {
        return shortName;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void clearDirty() {
        dirty = false;
    }

    @Override
    public String toString() {
        return uid+"::"+shortName;
    }
}
