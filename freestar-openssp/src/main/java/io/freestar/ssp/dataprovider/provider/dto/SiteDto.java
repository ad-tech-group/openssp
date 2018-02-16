package io.freestar.ssp.dataprovider.provider.dto;

import openrtb.bidrequest.model.Site;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author André Schmer
 *
 */
public class SiteDto implements Serializable {

	private static final long serialVersionUID = 6743606462533687452L;

	private List<Site> sites;

	public SiteDto() {}

	public List<Site> getSites() {
		return sites;
	}

	public void setSupplier(final List<Site> sites) {
		this.sites = sites;
	}

	@Override
	public String toString() {
		return String.format("SiteDto [sites=%s]", sites);
	}

}
