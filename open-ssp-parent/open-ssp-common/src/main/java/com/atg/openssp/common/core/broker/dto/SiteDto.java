package com.atg.openssp.common.core.broker.dto;

import openrtb.bidrequest.model.Site;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author André Schmer
 *
 */
public class SiteDto implements Serializable {

	private static final long serialVersionUID = 6743606462533687452L;

	private List<Site> sites = new ArrayList<>();

	public SiteDto() {}

	public List<Site> getSites() {
		ArrayList list = new ArrayList();
		list.addAll(sites);
		return list;
	}

	public void setSite(final List<Site> sites) {
		this.sites.clear();
		if (sites != null) {
			this.sites.addAll(sites);
		}
	}

	@Override
	public String toString() {
		return String.format("SiteDto [sites=%s]", sites);
	}

}
