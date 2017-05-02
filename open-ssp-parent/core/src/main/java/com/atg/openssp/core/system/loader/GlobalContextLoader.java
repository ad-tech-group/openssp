package com.atg.openssp.core.system.loader;

import java.util.concurrent.CountDownLatch;

import com.atg.openssp.common.configuration.Context;
import com.atg.openssp.common.configuration.ContextProperties;
import com.atg.openssp.common.configuration.GlobalContext;
import com.atg.openssp.core.system.job.CacheTriggerController;

/**
 * @author André Schmer
 * 
 */
public class GlobalContextLoader extends AbstractConfigurationLoader {

	public GlobalContextLoader() {
		super(Context.RUNTIME_GLOBAL_XML);
	}

	public GlobalContextLoader(final CountDownLatch cdl) {
		super(Context.RUNTIME_GLOBAL_XML, cdl);
	}

	@Override
	protected void readSpecials(final ContextProperties key, final String value) {
		if (ContextProperties.TRIGGER_EXPESSION == key) {
			CacheTriggerController.checkForExpressionUpdate(value, "CoreCacheLoaderJob");
		}
	}

	@Override
	protected void finalWork() {
		GlobalContext.refreshContext();
	}

}
