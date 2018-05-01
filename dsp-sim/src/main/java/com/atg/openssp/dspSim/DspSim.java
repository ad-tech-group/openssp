package com.atg.openssp.dspSim;

import com.atg.openssp.dspSim.model.ModelException;
import com.atg.openssp.dspSim.model.ad.AdModel;
import com.atg.openssp.dspSim.model.dsp.DspModel;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.security.KeyStore;

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
            int port = Integer.parseInt(dspModel.getProperty("server-port", "8081"));
            System.out.println("starting sim on port: "+port);
            HttpsServer server = HttpsServer.create(new InetSocketAddress(port), 0);


            SSLContext sslContext = SSLContext.getInstance("TLS");

            // initialise the keystore
            char[] password = "kDKmHY8ecT9nY3WAwWhqk5ZnF4eTYX2b".toCharArray();
            KeyStore ks = KeyStore.getInstance("JKS");
            InputStream is = getClass().getClassLoader().getResourceAsStream("cert/pub.network.jks");
            ks.load(is, password);
            is.close();

            // setup the key manager factory
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, password);

            // setup the trust manager factory
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(ks);

            // setup the HTTPS context and parameters
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            server.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
                public void configure(HttpsParameters params) {
                    try {
                        // initialise the SSL context
                        SSLContext c = SSLContext.getDefault();
                        SSLEngine engine = c.createSSLEngine();
                        params.setNeedClientAuth(false);
                        params.setCipherSuites(engine.getEnabledCipherSuites());
                        params.setProtocols(engine.getEnabledProtocols());

                        // get the default parameters
                        SSLParameters defaultSSLParameters = c.getDefaultSSLParameters();
                        params.setSSLParameters(defaultSSLParameters);

                    } catch (Exception ex) {
                        System.out.println("Failed to create HTTPS port");
                    }
                }
            });


            server.createContext("/dsp-sim/admin", new ClientHandler(dspModel));
            server.createContext("/dsp-sim/DemandService", new DspHandler(dspModel));
            server.createContext("/dsp-sim/myAds", new AdServerHandler(adModel));
            server.createContext("/win", new DspWinHandler(dspModel));
            server.createContext("/user-sync", new DspUserSyncHandler(dspModel));
            server.setExecutor(null); // creates a default executor
            server.start();
        } catch (Exception e) {
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