package com.atg.openssp.common.watchdog;

/**
 * @author André Schmer
 *
 */
public interface DynamicLoadable {

	void readValues();

	String getResourceFilename();

	String getResourceLocation();
}
