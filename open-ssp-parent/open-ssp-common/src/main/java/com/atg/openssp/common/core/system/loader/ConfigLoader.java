package com.atg.openssp.common.core.system.loader;

import com.atg.openssp.common.configuration.Context;
import com.atg.openssp.common.configuration.ContextProperties;
import com.atg.openssp.common.configuration.GlobalContext;

import java.util.concurrent.CountDownLatch;

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
