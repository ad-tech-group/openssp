package com.atg.openssp.dspSimUi.model;

import java.awt.*;

public enum MessageStatus {
    NOMINAL(Color.WHITE), FAULT(Color.RED), WARNING(Color.YELLOW);

    private final Color c;

    MessageStatus(Color c) {
        this.c = c;
    }

    public Color getColor()
    {
        return c;
    }
}
