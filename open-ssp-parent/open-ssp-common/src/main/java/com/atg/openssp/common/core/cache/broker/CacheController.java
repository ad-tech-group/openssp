package com.atg.openssp.common.core.cache.broker;

import com.atg.openssp.common.core.broker.remote.*;

import java.util.Observable;

/**
 * CacheController manages the list of available observers to cache broker.
 *
 * @author André Schmer
 */
public class CacheController extends Observable {

    public static final CacheController instance = new CacheController();

    public CacheController() {
        initingCacheList();
    }

    private void initingCacheList() {
        deleteObservers();

        /**
         * Use the following types of brokers to load JSON based configured data into cache.
         */
        // loads supplier data from supplier_db.json into the cache
        //addObserver(new SupplierDataBrokerJson());

        // loads site data from site_db.json into the cache
        //addObserver(new SiteDataBrokerJson());

        // loads priceinformation from price_layer.json into the cache
        //addObserver(new PricelayerBrokerJson());

        // loads priceinformation from price_layer.json into the cache
        //addObserver(new CurrencyBrokerJson());

        /**
         * Use the following types of brokers to load remote e.g. RESTful webservice based configured data into cache.
         */
        // loads currency data from webservice into the cache
        addObserver(new RemoteCurrencyDataBroker());

        // loads site data from webservice into the cache
        addObserver(new RemoteSiteDataBroker());

        // loads app data from webservice into the cache
        addObserver(new RemoteAppDataBroker());

        // loads priceinformation from webservice into the cache
        addObserver(new RemotePricelayerBroker());

        // loads website data from webservice into the cache
        // addObserver(new RemoteWebsiteDataBroker());

        // loads videoad data from webservice into the cache
        addObserver(new RemoteVideoadDataBroker());

        // loads videoad data from webservice into the cache
        addObserver(new RemoteBanneradDataBroker());

        // loads supplier data from webservice into the cache
        addObserver(new RemoteSupplierDataBroker());
    }

    /**
     * Updates the registered caches.
     */
    public void update() {
        setChanged();
        this.notifyObservers();
    }

}
