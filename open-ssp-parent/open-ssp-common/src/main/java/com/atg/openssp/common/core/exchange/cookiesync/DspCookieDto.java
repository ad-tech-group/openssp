package com.atg.openssp.common.core.exchange.cookiesync;

/**
 * This class is used to store the DSP's id for a given DSP
 */
public class DspCookieDto {
    /**
     * DSP's id for a given user
     */
    private String uid;
    /**
     * The name of the DSP
     */
    private String shortName;
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
