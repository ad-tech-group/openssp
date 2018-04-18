package com.atg.openssp.core.cache.broker.json;

import com.atg.openssp.common.cache.CurrencyCache;
import com.atg.openssp.common.cache.broker.DataBrokerObserver;
import com.atg.openssp.common.core.broker.dto.CurrencyDto;
import com.atg.openssp.common.logadapter.DataBrokerLogProcessor;
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
public final class CurrencyBrokerJson extends DataBrokerObserver {

	private static final Logger log = LoggerFactory.getLogger(CurrencyBrokerJson.class);

	public CurrencyBrokerJson() {}

	@Override
	protected boolean doCaching() {
		long startTS = System.currentTimeMillis();
		final Gson gson = new Gson();
		try {
			final String content = new String(Files.readAllBytes(Paths.get("currency_db.json")), StandardCharsets.UTF_8);
			final CurrencyDto dto = gson.fromJson(content, CurrencyDto.class);
			if (dto != null) {
				long endTS = System.currentTimeMillis();
				DataBrokerLogProcessor.instance.setLogData("Currency", dto.getData().size(), startTS, endTS, endTS-startTS);
			    CurrencyCache.instance.setBaseCurrency(dto.getCurrency());
				log.info("sizeof Currency data=" + dto.getData().size());
				dto.getData().forEach(c -> CurrencyCache.instance.put(c.getCurrency(), c.getRate()));
				return true;
			}
			log.error("no Currency data");
		} catch (final IOException e) {
			log.error(getClass() + ", " + e.getMessage());
		}
		return false;
	}

	@Override
	protected void finalWork() {
		// need to switch the intermediate cache to make the data available
		CurrencyCache.instance.switchCache();

	}

}
