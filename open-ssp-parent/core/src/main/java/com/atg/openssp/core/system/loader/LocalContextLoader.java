package com.atg.openssp.core.system.loader;

import java.util.concurrent.CountDownLatch;

import org.apache.logging.log4j.Level;

import com.atg.openssp.common.configuration.Context;
import com.atg.openssp.common.configuration.ContextProperties;
import com.atg.openssp.core.system.LocalContext;
import com.atg.service.LogFacade;

/**
 * @author André Schmer
 * 
 */
public class LocalContextLoader extends AbstractConfigurationLoader {

	public LocalContextLoader() {
		super(Context.RUNTIME_LOCAL_XML);
	}

	public LocalContextLoader(final CountDownLatch cdl) {
		super(Context.RUNTIME_LOCAL_XML, cdl);
	}

	@Override
	protected void readSpecials(final ContextProperties key, final String value) {
		if (ContextProperties.DEBUG == key) {
			if ("true".equals(value)) {
				LogFacade.initLogging(Level.DEBUG);
			} else {
				LogFacade.initLogging(Level.INFO);
			}
		}
	}

	@Override
	protected void finalWork() {
		LocalContext.refreshContext();
	}

}
