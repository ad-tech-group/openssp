package com.atg.openssp.common.core.broker.dto;

import openrtb.bidrequest.model.App;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author André Schmer
 *
 */
public class AppDto implements Serializable {

	private static final long serialVersionUID = 6743606462533687452L;

	private List<App> apps;

	public AppDto() {}

	public List<App> getApps() {
		return apps;
	}

	public void setSupplier(final List<App> apps) {
		this.apps = apps;
	}

	@Override
	public String toString() {
		return String.format("AppDto [apps=%s]", apps);
	}

}
