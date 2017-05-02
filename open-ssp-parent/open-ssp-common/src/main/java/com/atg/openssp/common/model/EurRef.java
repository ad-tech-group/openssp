package com.atg.openssp.common.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author André Schmer
 *
 */
@XmlRootElement
public class EurRef implements Serializable {

	private static final long serialVersionUID = 3042644082029166306L;

	private String currency;

	private float rate;

	public EurRef() {
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(final String currency) {
		this.currency = currency;
	}

	public float getRate() {
		return rate;
	}

	public void setRate(final float rate) {
		this.rate = rate;
	}

}
