package com.atg.openssp.core.cache.broker;

import java.util.Observable;

import com.atg.openssp.core.cache.broker.context.PricelayerBrokerJson;
import com.atg.openssp.core.cache.broker.context.SiteDataBrokerJson;
import com.atg.openssp.core.cache.broker.context.SupplierDataBrokerJson;

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

		// loads supplier data from supplier_db.json into the cache
		addObserver(new SupplierDataBrokerJson());

		// loads site data from site_db.json into the cache
		addObserver(new SiteDataBrokerJson());

		// loads priceinformation from price_layer.json into the cache
		addObserver(new PricelayerBrokerJson());
	}

	/**
	 * Updates the registered caches.
	 */
	public void update() {
		setChanged();
		this.notifyObservers();
	}

}
