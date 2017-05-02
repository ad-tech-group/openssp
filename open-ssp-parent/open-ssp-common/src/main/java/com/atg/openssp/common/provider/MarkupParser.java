package com.atg.openssp.common.provider;

import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;

/**
 * @author schmer
 * 
 */
class MarkupParser {

	/**
	 * 
	 * @param nurl
	 * @param priceValue
	 * @return the parsed value
	 */
	static String parseNurl(final String nurl, final double priceValue) {
		return parse(nurl, priceValue);
	}

	/**
	 * 
	 * @param markUp
	 * @param priceValue
	 * @return the parsed value
	 */
	public String parseMarkup(final String markUp, final double priceValue) {
		return parse(markUp, priceValue);
	}

	private static String parse(final String value, final double replace) {
		if (StringUtils.isNotEmpty(value)) {
			final Matcher m = MacroPattern.getPattern(MacroPattern.TYPE.PRICE).matcher(value);
			if (m.find()) {
				return m.replaceFirst(String.valueOf(replace));
			}
		}
		return value;
	}
}
