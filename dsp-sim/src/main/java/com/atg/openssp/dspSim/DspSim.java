package com.atg.openssp.dspSim;

import com.atg.openssp.dspSim.model.ModelException;
import com.atg.openssp.dspSim.model.ad.AdModel;
import com.atg.openssp.dspSim.model.dsp.DspModel;
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

    public DspSim(int index) throws ModelException {
        dspModel = new DspModel(index);
        adModel = new AdModel(index);
    }

    public void start() {
        try {
            int port = Integer.parseInt(dspModel.getProperty("server-port", "8081"));
            System.out.println("starting sim on port: "+port);
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/dsp-sim/admin", new ClientHandler(dspModel));
            server.createContext("/dsp-sim/DemandService", new DspHandler(dspModel));
            server.createContext("/dsp-sim/myAds", new AdServerHandler(adModel));
            server.createContext("/win", new DspWinHandler(dspModel));
            server.createContext("/user-sync", new DspUserSyncHandler(dspModel));
            server.setExecutor(null); // creates a default executor
            server.start();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        try {
            for (int i=0; i<2; i++) {
                DspSim sim = new DspSim(i);
                sim.start();
            }
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