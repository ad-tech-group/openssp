package com.atg.openssp.common.configuration;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @author André Schmer
 *
 */
public class Context {

	/**
	 * yyyy-MM-dd HH:mm
	 */
	public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	/**
	 * yyyy-MM-dd
	 */
	public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	/**
	 * "Europe/Berlin"
	 */
	public static final ZoneId zoneId = ZoneId.of("Europe/Berlin");

	/**
	 * {@value #RUNTIME_LOCAL_XML}
	 */
	public static final String RUNTIME_LOCAL_XML = "local.runtime.xml";

	/**
	 * {@value #RUNTIME_GLOBAL_XML}
	 */
	public static final String RUNTIME_GLOBAL_XML = "global.runtime.xml";

	/**
	 * {@value #SOCKET_TO}
	 */
	public static final int SOCKET_TO = 2000;

}
