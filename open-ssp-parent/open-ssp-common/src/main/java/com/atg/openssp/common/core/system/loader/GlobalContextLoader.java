package com.atg.openssp.common.core.system.loader;

import com.atg.openssp.common.configuration.Context;
import com.atg.openssp.common.configuration.ContextProperties;
import com.atg.openssp.common.configuration.GlobalContext;
import com.atg.openssp.common.core.system.job.CacheTriggerController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * @author André Schmer
 * 
 */
public class GlobalContextLoader extends AbstractConfigurationLoader {
	private static final Logger log = LoggerFactory.getLogger(GlobalContextLoader.class);

	public GlobalContextLoader() {
		super(resolveEnvironment()+Context.RUNTIME_GLOBAL_XML);
	}

	public GlobalContextLoader(final CountDownLatch cdl) {
		super(resolveEnvironment()+Context.RUNTIME_GLOBAL_XML, cdl);
	}

	public static String resolveEnvironment() {
		String environment = System.getProperty("SSP_ENVIRONMENT");
		log.info("Environment: "+environment);
		if (environment != null) {
			return environment+"_";
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
