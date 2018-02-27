package com.atg.openssp.dspSim;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class DspSim {

    public DspSim() {
    }

    public void start() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8082), 0);
            server.createContext("/dsp-sim/DemandService", new DspHandler());
            server.createContext("/dsp-sim/myAds", new AdServerHandler());
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