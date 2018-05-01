package com.atg.openssp.dspSim.model;

/**
 * @author Brian Sorensen
 */
public class ModelException extends Exception {

    public ModelException(String msg) {
        super(msg);
    }

    public ModelException(Throwable t) {
        super(t);
    }
}
