package io.freestar.ssp.channel.adserving;

import channel.adserving.AdserverBrokerHandler;

public class FreestarAdserverBrokerHandler extends AdserverBrokerHandler {
    @Override
    public Class<FreestarAdservingCampaignProvider> getProvider() {
        return FreestarAdservingCampaignProvider.class;
    }
}
