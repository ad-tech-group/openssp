package com.atg.openssp.common.model;

import java.io.Serializable;

/**
 * This class is deprecated, use {@see Productcategory} instead
 * 
 * @author André Schmer
 */
@Deprecated
public class Taxonomy implements Serializable {

	private static final long serialVersionUID = 6485744089881502165L;

	private String description;

	private String parent;

	public Taxonomy() {
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(final String parent) {
		this.parent = parent;
	}

}
