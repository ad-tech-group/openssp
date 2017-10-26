package com.atg.openssp.common.demand;

import java.io.Serializable;

/**
 * @author André Schmer
 *
 */
public class Supplier implements Serializable {

	private static final long serialVersionUID = 4638985536819833964L;

	private String shortName;

	private String endPoint;

	private boolean connectionKeepAlive;

	private String openRtbVersion;

	private String contentType;

	private String acceptEncoding;// gzip

	private String contentEncoding;// gzip

	private Long supplierId;

	private String currency;

	private int underTest;

	private int active;

	public Supplier() {}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(final String shortName) {
		this.shortName = shortName;
	}

	public String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(final String endPoint) {
		this.endPoint = endPoint;
	}

	public boolean isConnectionKeepAlive() {
		return connectionKeepAlive;
	}

	public String getOpenRtbVersion() {
		return openRtbVersion;
	}

	public void setOpenRtbVersion(final String openRtbVersion) {
		this.openRtbVersion = openRtbVersion;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(final String contentType) {
		this.contentType = contentType;
	}

	public String getAcceptEncoding() {
		return acceptEncoding;
	}

	public void setAcceptEncoding(final String acceptEncoding) {
		this.acceptEncoding = acceptEncoding;
	}

	public String getContentEncoding() {
		return contentEncoding;
	}

	public void setContentEncoding(final String contentEncoding) {
		this.contentEncoding = contentEncoding;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(final Long supplierId) {
		this.supplierId = supplierId;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(final String currency) {
		this.currency = currency;
	}

	public int getUnderTest() {
		return underTest;
	}

	public void setUnderTest(final int underTest) {
		this.underTest = underTest;
	}

	public int getActive() {
		return active;
	}

	public void setActive(final int active) {
		this.active = active;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (connectionKeepAlive ? 1231 : 1237);
		result = prime * result + ((contentType == null) ? 0 : contentType.hashCode());
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		result = prime * result + ((endPoint == null) ? 0 : endPoint.hashCode());
		result = prime * result + ((openRtbVersion == null) ? 0 : openRtbVersion.hashCode());
		result = prime * result + ((shortName == null) ? 0 : shortName.hashCode());
		result = prime * result + ((supplierId == null) ? 0 : supplierId.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Supplier other = (Supplier) obj;
		if (connectionKeepAlive != other.connectionKeepAlive) {
			return false;
		}
		if (contentType == null) {
			if (other.contentType != null) {
				return false;
			}
		} else if (!contentType.equals(other.contentType)) {
			return false;
		}
		if (currency == null) {
			if (other.currency != null) {
				return false;
			}
		} else if (!currency.equals(other.currency)) {
			return false;
		}
		if (endPoint == null) {
			if (other.endPoint != null) {
				return false;
			}
		} else if (!endPoint.equals(other.endPoint)) {
			return false;
		}
		if (!openRtbVersion.equals(other.openRtbVersion)) {
			return false;
		}
		if (shortName == null) {
			if (other.shortName != null) {
				return false;
			}
		} else if (!shortName.equals(other.shortName)) {
			return false;
		}
		if (supplierId == null) {
			if (other.supplierId != null) {
				return false;
			}
		} else if (!supplierId.equals(other.supplierId)) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		return String.format("Supplier [shortName=%s, endPoint=%s, openRtbVersion=%s]", shortName, endPoint, openRtbVersion);
	}

}
