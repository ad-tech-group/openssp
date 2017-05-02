package com.atg.openssp.common.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

/**
 * This class is also known as <code>factual category</code>
 * 
 * @author André Schmer
 * 
 */
public class Category implements Serializable {

	private static final long serialVersionUID = -4580169816336361287L;

	private int id;

	private String name;

	private String name_de;

	@SerializedName("factual-id")
	private int factual_id;

	@SerializedName("factual-parent-id")
	private int factual_parent_id;

	public Category() {}

	public int getId() {
		return id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getName_de() {
		return name_de;
	}

	public void setName_de(final String name_de) {
		this.name_de = name_de;
	}

	public int getFactual_id() {
		return factual_id;
	}

	public void setFactual_id(final int factual_id) {
		this.factual_id = factual_id;
	}

	public int getFactual_parent_id() {
		return factual_parent_id;
	}

	public void setFactual_parent_id(final int factual_parent_id) {
		this.factual_parent_id = factual_parent_id;
	}

	@Override
	public String toString() {
		return String.format("Category [id=%s, name=%s, name_de=%s, factual_id=%s, factual_parent_id=%s]", id, name, name_de, factual_id, factual_parent_id);
	}

}
