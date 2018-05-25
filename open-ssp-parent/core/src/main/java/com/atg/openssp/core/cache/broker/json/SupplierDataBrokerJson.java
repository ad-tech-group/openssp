package com.atg.openssp.core.cache.broker.json;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.atg.openssp.common.core.broker.dto.SupplierDto;
import com.atg.openssp.common.core.cache.type.ConnectorCache;
import com.atg.openssp.common.core.exchange.channel.rtb.OpenRtbConnector;
import com.atg.openssp.common.demand.Supplier;
import com.atg.openssp.common.logadapter.DataBrokerLogProcessor;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atg.openssp.common.cache.broker.DataBrokerObserver;
import com.google.gson.Gson;
import util.properties.ProjectProperty;

import javax.xml.bind.PropertyException;

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
		long startTS = System.currentTimeMillis();
		GsonBuilder builder = new GsonBuilder();
		Supplier.populateTypeAdapters(builder);
		final Gson gson = builder.create();
		try {
			final String path = ProjectProperty.readFile("supplier_db.json").getAbsolutePath();
			final String content = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
			final SupplierDto dto = gson.fromJson(content, SupplierDto.class);
			if (dto != null) {
				long endTS = System.currentTimeMillis();
				DataBrokerLogProcessor.instance.setLogData("SupplierData", dto.getSupplier().size(), startTS, endTS, endTS-startTS);
				log.info("sizeof supplier data=" + dto.getSupplier().size());
				dto.getSupplier().forEach(supplier -> {
					final OpenRtbConnector openRtbConnector = new OpenRtbConnector(supplier);
					ConnectorCache.instance.add(openRtbConnector);
				});
				return true;
			}

			log.error("no Supplier data");
			return false;
		} catch (final IOException | PropertyException e) {
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
