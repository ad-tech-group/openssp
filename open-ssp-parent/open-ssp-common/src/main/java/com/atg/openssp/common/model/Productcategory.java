package com.atg.openssp.common.model;

/**
 * 
 * http://www.google.com/basepages/producttype/taxonomy.en-US.txt
 * 
 * @author André Schmer
 *
 */
public class Productcategory {

	private int id;

	private int gpid;// google product id, als referenz in campaign.productcategory.id

	private int parent_id;

	private int lft;// id muss >lft sein und <rght

	private int rght;

	private String name;

	private boolean enabled;

	public Productcategory() {
	}

	public int getId() {
		return id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public int getGpid() {
		return gpid;
	}

	public void setGpid(final int gpid) {
		this.gpid = gpid;
	}

	public int getParent_id() {
		return parent_id;
	}

	public void setParent_id(final int parent_id) {
		this.parent_id = parent_id;
	}

	public int getLft() {
		return lft;
	}

	public void setLft(final int lft) {
		this.lft = lft;
	}

	public int getRght() {
		return rght;
	}

	public void setRght(final int rght) {
		this.rght = rght;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		return String.format("Productcategory [id=%s, gpid=%s, parent_id=%s, lft=%s, rght=%s, name=%s, enabled=%s]", id,
				gpid, parent_id, lft, rght, name, enabled);
	}

}
