package com.atg.openssp.common.model;

import java.io.Serializable;

/**
 * This class is deprecated use {@link Category} instead
 * 
 * @author André Schmer
 *
 */
@Deprecated
public class FactualCategory implements Serializable {

	private static final long serialVersionUID = 503204889599125772L;

	private int categoryID;

	private String parent;

	private int abstr;

	private String en;

	private String ge;

	private String es;

	private String fr;

	private String it;

	public FactualCategory() {
	}

	public int getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(final int categoryID) {
		this.categoryID = categoryID;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(final String parent) {
		this.parent = parent;
	}

	public int getAbstr() {
		return abstr;
	}

	public void setAbstr(final int abstr) {
		this.abstr = abstr;
	}

	public String getEn() {
		return en;
	}

	public void setEn(final String en) {
		this.en = en;
	}

	public String getGe() {
		return ge;
	}

	public void setGe(final String ge) {
		this.ge = ge;
	}

	public String getEs() {
		return es;
	}

	public void setEs(final String es) {
		this.es = es;
	}

	public String getFr() {
		return fr;
	}

	public void setFr(final String fr) {
		this.fr = fr;
	}

	public String getIt() {
		return it;
	}

	public void setIt(final String it) {
		this.it = it;
	}

	@Override
	public String toString() {
		return String.format("FactualCategory [categoryID=%s, parents=%s, abstr=%s, en=%s, ge=%s, es=%s, fr=%s, it=%s]",
				categoryID, parent, abstr, en, ge, es, fr, it);
	}

}
