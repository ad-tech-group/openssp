package com.atg.openssp.common.core.system.loader;

import com.atg.openssp.common.configuration.Context;
import com.atg.openssp.common.configuration.ContextProperties;
import com.atg.openssp.common.configuration.GlobalContext;
import com.atg.openssp.common.core.system.job.CacheTriggerController;

import java.util.concurrent.CountDownLatch;

/**
 * @author André Schmer
 * 
 */
public class GlobalContextLoader extends AbstractConfigurationLoader {

	public GlobalContextLoader() {
		super(resolveEnvironment()+Context.RUNTIME_GLOBAL_XML);
	}

	public GlobalContextLoader(final CountDownLatch cdl) {
		super(resolveEnvironment()+Context.RUNTIME_GLOBAL_XML, cdl);
	}

	public static String resolveEnvironment() {
		String environment = System.getProperty("SSP_ENVIRONMENT");
		System.out.println("****** ENVIRONMENT ******* "+environment);
		if (environment != null) {
			return environment+"/";
		} else {
			return "";
		}
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
