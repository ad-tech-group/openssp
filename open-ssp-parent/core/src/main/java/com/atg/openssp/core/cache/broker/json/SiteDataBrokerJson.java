package com.atg.openssp.core.cache.broker.json;

import com.atg.openssp.common.cache.broker.DataBrokerObserver;
import com.atg.openssp.common.logadapter.DataBrokerLogProcessor;
import com.atg.openssp.core.cache.broker.dto.SiteDto;
import com.atg.openssp.core.cache.type.SiteDataCache;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author André Schmer
 *
 */
public class SiteDataBrokerJson extends DataBrokerObserver {

	private static final Logger log = LoggerFactory.getLogger(SiteDataBrokerJson.class);

	public SiteDataBrokerJson() {}

	@Override
	protected boolean doCaching() {
		long startTS = System.currentTimeMillis();
		final Gson gson = new Gson();
		try {
			final String content = new String(Files.readAllBytes(Paths.get("site_db.json")), StandardCharsets.UTF_8);
			final SiteDto dto = gson.fromJson(content, SiteDto.class);
			if (dto != null) {
				long endTS = System.currentTimeMillis();
				DataBrokerLogProcessor.instance.setLogData("SiteData", startTS, endTS, endTS-startTS);
				log.info("sizeof site data=" + dto.getSites().size());
				dto.getSites().forEach(site -> {
					SiteDataCache.instance.put(site.getId(), site);
				});
				return true;
			}

			log.error("no Site data");
			return false;
		} catch (final IOException e) {
			log.error(getClass() + ", " + e.getMessage());
		}

		return true;
	}

	@Override
	protected void finalWork() {
		// need to switch the intermediate cache to make the data available
		SiteDataCache.instance.switchCache();

	}

}
