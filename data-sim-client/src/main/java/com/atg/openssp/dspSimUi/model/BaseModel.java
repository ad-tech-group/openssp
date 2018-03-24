package com.atg.openssp.dspSimUi.model;

import java.util.ArrayList;

public abstract class BaseModel {
    private final ArrayList<MessageNotificationListener> messageListeners = new ArrayList();

    public void setMessage(String m) {
        notifyMessageListeners(MessageStatus.NOMINAL, m);
    }

    public void setMessageAsWarning(String m) {
        notifyMessageListeners(MessageStatus.WARNING, m);
    }

    public void setMessageAsFault(String m) {
        notifyMessageListeners(MessageStatus.FAULT, m);
    }

    private void notifyMessageListeners(MessageStatus s, String m) {
        for (MessageNotificationListener lis : messageListeners) {
            lis.sendMessage(s, m);
        }
    }

    public void addMessageNotificationListener(MessageNotificationListener lis) {
        messageListeners.add(lis);
    }

    public void removeMessageNotificationListener(MessageNotificationListener lis) {
        messageListeners.remove(lis);
    }

}
