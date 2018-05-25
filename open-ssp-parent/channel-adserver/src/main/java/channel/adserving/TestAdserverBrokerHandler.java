package channel.adserving;

public class TestAdserverBrokerHandler extends AdserverBrokerHandler {
    @Override
    public Class<TestAdservingCampaignProvider> getProvider() {
        return TestAdservingCampaignProvider.class;
    }
}
