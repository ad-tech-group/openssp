package com.atg.openssp.common.core.system.job;

import com.atg.openssp.common.buffer.AdservingLatencyBuffer;
import com.atg.openssp.common.configuration.Context;
import com.atg.openssp.common.configuration.GlobalContext;
import com.atg.openssp.common.core.entry.RequestMonitor;
import com.atg.openssp.common.core.system.LocalContext;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.CatalinaUtil;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * @author André Schmer
 * 
 */
public class HeartBeatJob implements Job {
	private static final Logger log = LoggerFactory.getLogger(HeartBeatJob.class);

	private final static DecimalFormat df = new DecimalFormat("####0.00");

	@Override
	public void execute(final JobExecutionContext context) throws JobExecutionException {
		final long intermediateRequests = RequestMonitor.getIntermediateRequestCounter();
		if (intermediateRequests > 0) {

			final String heartBeatTime = LocalDateTime.now().format(Context.dateTimeFormatter);
			final StringBuilder sb = new StringBuilder(heartBeatTime + "#engine:" + CatalinaUtil.instanceName() + ":" + CatalinaUtil.ipSuffix());

			// avg Latencytime SSP
			// final Long[] latencyBuffer = SSPLatencyBuffer.getBuffer().getBufferedData();
			// final double sspLatencyValue = Arrays.stream(latencyBuffer).filter(a -> a != null).mapToLong(a -> a)
			// .average().orElse(0);
			// sb.append("#ssplatency:" + df.format(sspLatencyValue));

			// avg Latencytime VAST call
			// final Long[] vastResolverLatencyBuffer = VastResolverLatencyBuffer.getBuffer().getBufferedData();
			// final double vastResolverLatencyValue = Arrays.stream(vastResolverLatencyBuffer).filter(a -> a != null)
			// .mapToLong(a -> a).average().orElse(0);
			// sb.append("#resolverlatency:" + df.format(vastResolverLatencyValue));

			// avg Latencytime final Adserve call
			final Long[] adserveLatencyBuffer = AdservingLatencyBuffer.getBuffer().getBufferedData();
			final double adservingLatencyValue = Arrays.stream(adserveLatencyBuffer).filter(a -> a != null).mapToLong(a -> a).average().orElse(0);
			sb.append("#adservelatency:" + df.format(adservingLatencyValue));

			// requests des letzten abfragezeitraums
			sb.append("#reqcnt:" + intermediateRequests);

			// rtb enabled
			sb.append("#rtb:" + LocalContext.isDSPChannelEnabled());

			// adserving enabled
			sb.append("#adserving:" + LocalContext.isAdservingChannelEnabled());

			// cdl-timeout
			sb.append("#exeto:" + GlobalContext.getExecutionTimeout());

			if (LocalContext.isVerboseEnabled()) {
				log.info(sb.toString());
			}

			// TODO: log the results - messaging activation necessary
			// EnginestatLogProducer.instance().send(sb.toString());
		}
	}

}
