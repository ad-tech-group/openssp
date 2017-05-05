package com.atg.openssp.core.cache.broker;

import java.util.Observable;

import com.atg.openssp.core.cache.broker.remote.RemoteVideoadDataBroker;
import com.atg.openssp.core.cache.broker.remote.RemoteWebsiteDataBroker;

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

		// TODO: activate after enables RTB and setting up supplier data store...
		// addObserver(new SupplierDataBroker());

		// TODO: activate after setting up currency data store ...
		// addObserver(new CurrencyDataBroker());

		// TODO: activate after setting up a webservice to provide additional informations to those data or an equivalent
		addObserver(new RemoteWebsiteDataBroker());
		addObserver(new RemoteVideoadDataBroker());
	}

	/**
	 * Updates the registered caches.
	 */
	public void update() {
		setChanged();
		this.notifyObservers();
	}

}
