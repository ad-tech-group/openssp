package com.atg.openssp.dspSim.channel.adserving;

public class AdservingCampaignProvider {
    private boolean isValid;
    private float price;
    private float adjustedCurrencyPrice;

    public void setIsValid(boolean isValid) {
        this.isValid = isValid;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean isValid) {
        this.isValid = isValid;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPrice() {
        return price;
    }

    public void setAdjustedCurrencyPrice(float adjustedCurrencyPrice) {
        this.adjustedCurrencyPrice = adjustedCurrencyPrice;
    }

    public float getAdjustedCurrencyPrice() {
        return adjustedCurrencyPrice;
    }
}
