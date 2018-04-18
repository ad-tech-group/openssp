package com.atg.openssp.common.core.broker.dto;

import openrtb.bidrequest.model.Pricelayer;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author André Schmer
 *
 */
public class PricelayerDto implements Serializable {

	private static final long serialVersionUID = -7348030785810292621L;

	private List<Pricelayer> pricelayer;

	public PricelayerDto() {}

	public List<Pricelayer> getPricelayer() {
		return pricelayer;
	}

	public void setPricelayer(final List<Pricelayer> pricelayer) {
		this.pricelayer = pricelayer;
	}

	@Override
	public String toString() {
		return String.format("PricelayerDto [pricelayer=%s]", pricelayer);
	}

}
