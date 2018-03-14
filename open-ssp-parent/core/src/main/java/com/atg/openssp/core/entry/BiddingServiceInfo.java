package com.atg.openssp.core.entry;

import java.util.HashMap;
import java.util.Map;

public class BiddingServiceInfo {
    private HashMap<String, String> headers = new HashMap();
    private SessionAgentType type;
    private String contentType;
    private String characterEncoding;
    private boolean activateAccessAllowOrigin;
    private boolean useSecondBest = true;

    public void setType(SessionAgentType type) {
        this.type = type;
    }

    public SessionAgentType getType() {
        return type;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers.clear();
        this.headers.putAll(headers);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getCharacterEncoding() {
        return characterEncoding;
    }

    public void setCharacterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }

    public void activateAccessAllowOrigin() {
        activateAccessAllowOrigin = true;
    }

    public boolean isAccessAllowOriginActivated() {
        return activateAccessAllowOrigin;
    }

    public boolean useSecondBest() {
        return useSecondBest;
    }

    public void disableSecondBest() {
        useSecondBest = false;
    }
}
