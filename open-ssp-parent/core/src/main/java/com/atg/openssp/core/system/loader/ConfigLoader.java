package com.atg.openssp.core.system.loader;

import java.util.concurrent.CountDownLatch;

import com.atg.openssp.common.configuration.Context;
import com.atg.openssp.common.configuration.ContextProperties;
import com.atg.openssp.common.configuration.GlobalContext;

/**
 * @author André Schmer
 * 
 */
public class ConfigLoader extends AbstractConfigurationLoader {

	public ConfigLoader() {
		super(Context.CONFIG_XML);
	}

	public ConfigLoader(final CountDownLatch cdl) {
		super(Context.CONFIG_XML, cdl);
	}

	@Override
	protected void readSpecials(final ContextProperties key, final String value) {
		// noting to do
	}

	@Override
	protected void finalWork() {
		GlobalContext.refreshContext();
	}

}
