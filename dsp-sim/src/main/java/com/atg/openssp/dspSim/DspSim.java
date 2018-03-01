package com.atg.openssp.dspSim;

import com.atg.openssp.dspSim.model.ad.AdModel;
import com.atg.openssp.dspSim.model.dsp.DspModel;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class DspSim {
    private DspModel dspModel;
    private AdModel adModel;

    public DspSim() {
        dspModel = new DspModel();
        adModel = new AdModel();
    }

    public void start() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8082), 0);
            server.createContext("/dsp-sim/DemandService", new DspHandler(dspModel));
            server.createContext("/dsp-sim/myAds", new AdServerHandler(adModel));
            server.setExecutor(null); // creates a default executor
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DspSim sim = new DspSim();
        sim.start();
        while(true) {
            try {
                Thread.sleep(100000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}