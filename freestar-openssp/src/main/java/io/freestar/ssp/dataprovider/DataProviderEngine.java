package io.freestar.ssp.dataprovider;

import com.sun.net.httpserver.HttpServer;
import io.freestar.ssp.dataprovider.provider.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;

public class DataProviderEngine {
    private ArrayList<DataProvider> providers = new ArrayList();

    public DataProviderEngine() {
    }

    private void init(DataProvider p) {
        providers.add(p);
    }

    public void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(2112), 0);
        init(new CurrencyDataProvider(server));
        init(new SupplierDataProvider(server));
        init(new SiteDataProvider(server));
        init(new PricelayerDataProvider(server));
        init(new LoginService(server));
        for (DataProvider p : providers) {
            p.start();
        }
        server.start();
    }

    public static void main(String[] args) {
        DataProviderEngine sim = new DataProviderEngine();
        try {
            sim.start();
            while(true) {
                try {
                    Thread.sleep(100000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}