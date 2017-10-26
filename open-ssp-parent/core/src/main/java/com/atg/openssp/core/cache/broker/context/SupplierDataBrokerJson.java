package com.atg.openssp.core.cache.broker.context;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atg.openssp.common.cache.broker.DataBrokerObserver;
import com.atg.openssp.common.demand.Supplier;
import com.atg.openssp.core.cache.type.ConnectorCache;
import com.atg.openssp.core.exchange.channel.rtb.OpenRtbConnector;
import com.google.gson.Gson;

/**
 * 
 * This special data broker loads the {@see SupplierDto} data from a file based store which holds the data for the supplier, also known as DSP. and put into a
 * special cache resp. key-value store {@see ConnectorCache}. It uses a {@see PathBuilder} object to store information about the endpoint which is used by the
 * generic {@see AbstractDataBroker} to connect to the remote.
 * 
 * @author André Schmer
 *
 */
public final class SupplierDataBrokerJson extends DataBrokerObserver {

	private static final Logger log = LoggerFactory.getLogger(SupplierDataBrokerJson.class);

	public SupplierDataBrokerJson() {}

	/**
	 * Reads the supplier configuration located in spplier_db.json
	 */
	@Override
	public boolean doCaching() {
		final Gson gson = new Gson();
		try {
			final String content = new String(Files.readAllBytes(Paths.get("supplier_db.json")), StandardCharsets.UTF_8);
			final SupplierDto dto = gson.fromJson(content, SupplierDto.class);
			if (dto != null) {
				log.info("sizeof supplier data=" + dto.getSupplier().size());
				dto.getSupplier().forEach(new Consumer<Supplier>() {
					@Override
					public void accept(final Supplier supplier) {
						final OpenRtbConnector openRtbConnector = new OpenRtbConnector(supplier);
						ConnectorCache.instance.add(openRtbConnector);
					}
				});
				return true;
			}

			log.error("no Supplier data");
			return false;
		} catch (final IOException e) {
			log.error(getClass() + ", " + e.getMessage());
		}

		return true;
	}

	@Override
	protected void finalWork() {
		// need to switch the intermediate cache to make the data available
		ConnectorCache.instance.switchCache();
	}

}
