package com.atg.openssp.dspSimUi.model.dsp;

import com.atg.openssp.dspSimUi.model.client.ServerCommandType;

public interface ModeChangeListener {
    void updateMode(ServerCommandType mode);
}
