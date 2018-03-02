package com.atg.openssp.dspSim.model.dsp;

/**
 * @author Brian Sorensen
 */
public interface SimBidderListener {
    void added(SimBidder sb);

    void removed(SimBidder sb);
}
