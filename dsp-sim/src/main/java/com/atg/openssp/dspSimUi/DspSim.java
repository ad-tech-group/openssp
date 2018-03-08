package com.atg.openssp.dspSimUi;

import com.atg.openssp.dspSimUi.model.ModelException;
import com.atg.openssp.dspSimUi.model.ad.AdModel;
import com.atg.openssp.dspSimUi.model.dsp.DspModel;
import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author Brian Sorensen
 */
public class DspSim {
    private static final Logger log = LoggerFactory.getLogger(DspSim.class);
    private DspModel dspModel;
    private AdModel adModel;

    public DspSim() throws ModelException {
        dspModel = new DspModel();
        adModel = new AdModel();
    }

    public void start() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8082), 0);
            server.createContext("/dsp-sim/admin", new ClientHandler(dspModel));
            server.createContext("/dsp-sim/DemandService", new DspHandler(dspModel));
            server.createContext("/dsp-sim/myAds", new AdServerHandler(adModel));
            server.setExecutor(null); // creates a default executor
            server.start();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

    }

    public static void main(String[] args) {
        try {
            DspSim sim = new DspSim();
            sim.start();
            while(true) {
                try {
                    Thread.sleep(100000);
                } catch (InterruptedException e) {
                }
            }
        } catch (ModelException e) {
            log.error(e.getMessage(), e);
        }
    }

}