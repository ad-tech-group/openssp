package com.atg.openssp.common.core.system;

import util.CatalinaUtil;

/**
 * @author André Schmer
 *
 */
class InitLogging {

	static void setSystemProperties() {
		System.setProperty("tomcatid", CatalinaUtil.instanceName());
		if (false == "localhost".equals(CatalinaUtil.instanceName())) {
			System.setProperty("log4j.configurationFile", CatalinaUtil.catalinaHome() + "/properties/log4j2.xml");
		}
		System.setProperty("Log4jContextSelector", "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");
		System.setProperty("pid", CatalinaUtil.pid());
	}

}
