package com.atg.openssp.core.entry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.atg.openssp.common.demand.ParamValue;
import com.atg.openssp.core.cache.type.VideoAdDataCache;
import com.atg.openssp.core.cache.type.WebsiteDataCache;
import com.atg.openssp.core.cache.type.ZoneDataCache;

import common.ERROR_CODE;
import common.EmptyCacheException;
import common.RequestException;
import openrtb.tables.VideoBidResponseProtocol;

/**
 * The kind and diversity of request parameters my be vary and depends on the different conditions. See also at {@link ParamValue} for the kind aof params.
 * 
 * @author André Schmer
 *
 */
public class EntryValidator {

	/**
	 * Extracts and validates the request parameters.
	 * 
	 * @param request
	 * @throws RequestException
	 *             if params are mandatory and empty and cannot be replaced by an alternative value.
	 * @return {@link ParamValue}
	 */
	public ParamValue validateEntryParams(final HttpServletRequest request) throws RequestException {

		final ParamValue pm = new ParamValue();
		try {
			final int zoneid = Integer.valueOf(request.getParameter("zone"));
			pm.setZone(ZoneDataCache.instance.get(zoneid));
		} catch (final NumberFormatException e) {
			throw new RequestException(ERROR_CODE.E906, "Zone");
		} catch (final EmptyCacheException e) {
			throw new RequestException(ERROR_CODE.E908, e.getMessage());
		}

		try {
			final int adid = Integer.valueOf(request.getParameter("ad"));
			pm.setVideoad(VideoAdDataCache.instance.get(adid));
		} catch (final NumberFormatException e) {
			throw new RequestException(ERROR_CODE.E909, "VideoAd");
		} catch (final EmptyCacheException e1) {
			throw new RequestException(ERROR_CODE.E909, e1.getMessage());
		}

		try {
			pm.setWebsite(WebsiteDataCache.instance.get(pm.getZone().getWebsiteId()));
		} catch (final EmptyCacheException e) {
			throw new RequestException(ERROR_CODE.E907, e.getMessage());
		}

		pm.setDomain(checkValue(request.getParameter("domain"), ERROR_CODE.E906, "Domain"));
		pm.setH(checkValue(request.getParameter("h"), ERROR_CODE.E906, "Height"));
		pm.setW(checkValue(request.getParameter("w"), ERROR_CODE.E906, "Width"));
		pm.setMimes(convertMimes(request.getParameter("mimes")));
		pm.setPage(checkValue(request.getParameter("page"), pm.getDomain()));
		pm.setStartdelay(Integer.valueOf(checkValue(request.getParameter("sd"), "0")));
		pm.setPublisherid(checkValue(request.getParameter("pub"), String.valueOf(pm.getZone().getWebsiteId())));
		pm.setProtocols(convertProtocolValues(request.getParameter("prot")));

		return pm;
	}

	private static String checkValue(final String toCheck, final ERROR_CODE code, final String message) throws RequestException {
		if (StringUtils.isEmpty(toCheck)) {
			throw new RequestException(code, message);
		}
		return toCheck;
	}

	private static String checkValue(final String toCheck, final String alternative) {
		if (StringUtils.isEmpty(toCheck)) {
			return alternative;
		}
		return toCheck;
	}

	private static List<Integer> convertProtocolValues(final String sProtocols) {
		final List<Integer> protocols = new ArrayList<>();
		if (!StringUtils.isEmpty(sProtocols)) {
			final String[] protos = sProtocols.split(",");
			for (final String protocol : protos) {
				protocols.add(VideoBidResponseProtocol.convertFromString(protocol));
			}
		} else {
			protocols.add(VideoBidResponseProtocol.VAST_3_0.getValue());
		}
		return protocols;
	}

	private static List<String> convertMimes(final String mimeTypes) {
		List<String> mimeTypeList = new ArrayList<>();
		if (!StringUtils.isEmpty(mimeTypes)) {
			mimeTypeList = Arrays.asList(mimeTypes.split(","));
			return mimeTypeList;
		}
		// default
		mimeTypeList.add("video/mp4");
		return mimeTypeList;
	}
}
