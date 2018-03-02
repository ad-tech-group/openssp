package com.atg.openssp.dspSim.model.dsp;

/**
 * @author Brian Sorensen
 */
public class SimBidder {
    private final String id;
    private float price;

    public SimBidder(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return id + " - ("+price+")";
    }
}
