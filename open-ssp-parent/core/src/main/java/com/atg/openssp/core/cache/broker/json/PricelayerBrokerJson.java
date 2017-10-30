package com.atg.openssp.core.cache.broker.json;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atg.openssp.common.cache.broker.DataBrokerObserver;
import com.atg.openssp.core.cache.broker.dto.PricelayerDto;
import com.atg.openssp.core.cache.type.PricelayerCache;
import com.google.gson.Gson;

import openrtb.bidrequest.model.Pricelayer;

/**
 * @author André Schmer
 *
 */
public class PricelayerBrokerJson extends DataBrokerObserver {

	private static final Logger log = LoggerFactory.getLogger(PricelayerBrokerJson.class);

	public PricelayerBrokerJson() {}

	@Override
	protected boolean doCaching() {
		final Gson gson = new Gson();
		try {
			final String content = new String(Files.readAllBytes(Paths.get("price_layer.json")), StandardCharsets.UTF_8);
			final PricelayerDto dto = gson.fromJson(content, PricelayerDto.class);
			if (dto != null) {
				log.info("sizeof pricelayer data=" + dto.getPricelayer().size());
				dto.getPricelayer().forEach(new Consumer<Pricelayer>() {
					@Override
					public void accept(final Pricelayer pricelayer) {
						PricelayerCache.instance.put(pricelayer.getSiteid(), pricelayer);
					}
				});
				return true;
			}

			log.error("no price data");
			return false;
		} catch (final IOException e) {
			log.error(getClass() + ", " + e.getMessage());
		}

		return true;
	}

	@Override
	protected void finalWork() {
		PricelayerCache.instance.switchCache();
	}

}
