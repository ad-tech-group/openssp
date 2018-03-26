package com.atg.openssp.core.cache.broker.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import openrtb.bidrequest.model.Site;

/**
 * 
 * @author André Schmer
 *
 */
public class SiteDto implements Serializable {

	private static final long serialVersionUID = 6743606462533687452L;

	private List<Site> sites = new ArrayList<Site>();

	public SiteDto() {}

	public List<Site> getSites() {
		return sites;
	}

	public void setSites(final List<Site> sites) {
		sites.clear();
		if (sites != null) {
			this.sites.addAll(sites);
		}
	}

	@Override
	public String toString() {
		return String.format("SiteDto [sites=%s]", sites);
	}

}
