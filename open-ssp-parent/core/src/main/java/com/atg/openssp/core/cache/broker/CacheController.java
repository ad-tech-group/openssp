package com.atg.openssp.core.cache.broker;

import java.util.Observable;

import com.atg.openssp.core.cache.broker.json.PricelayerBrokerJson;
import com.atg.openssp.core.cache.broker.json.SiteDataBrokerJson;
import com.atg.openssp.core.cache.broker.json.SupplierDataBrokerJson;

/**
 * 
 * CacheController manages the list of available observers to cache broker.
 * 
 * @author André Schmer
 */
public class CacheController extends Observable {

	public static CacheController instance = new CacheController();

	public CacheController() {
		initingCacheList();
	}

	private void initingCacheList() {
		deleteObservers();

		/**
		 * Use the following types of brokers to load JSON based configured data into cache.
		 */
		// loads supplier data from supplier_db.json into the cache
		addObserver(new SupplierDataBrokerJson());

		// loads site data from site_db.json into the cache
		addObserver(new SiteDataBrokerJson());

		// loads priceinformation from price_layer.json into the cache
		addObserver(new PricelayerBrokerJson());

		/**
		 * Use the following types of brokers to load remote e.g. RESTful webservice based configured data into cache.
		 */
		// loads currency data from webservice into the cache
		// addObserver(new RemoteCurrencyDataBroker());

		// loads website data from webservice into the cache
		// addObserver(new RemoteWebsiteDataBroker());

		// loads videoad data from webservice into the cache
		// addObserver(new RemoteVideoadDataBroker());

		// loads supplier data from webservice into the cache
		// addObserver(new RemoteSupplierDataBroker());
	}

	/**
	 * Updates the registered caches.
	 */
	public void update() {
		setChanged();
		this.notifyObservers();
	}

}
