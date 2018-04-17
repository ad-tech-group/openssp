package com.atg.openssp.common.core.system;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;

import java.util.concurrent.TimeUnit;

/**
 * @author André Schmer
 *
 */
public class MetricFactory {

	public static final MetricFactory instance = new MetricFactory();

	private final MetricRegistry registry;

	private Histogram histogram;

	private Counter requests;

	private MetricFactory() {
		registry = new MetricRegistry();
	}

	public void initMetrics() {
		requests = registry.counter("requests");
		histogram = registry.histogram("histogram");
	}

	public Counter getRequestCounter() {
		return requests;
	}

	public Histogram getHistogram() {
		return histogram;
	}

	void startMetricReport() {
		// final CsvReporter reporter = CsvReporter.forRegistry(registry).formatFor(Locale.US).convertRatesTo(
		// TimeUnit.SECONDS).convertDurationsTo(TimeUnit.MILLISECONDS).build(new File("metrics.csv"));
		// final ConsoleReporter reporter = ConsoleReporter.forRegistry(registry).convertRatesTo(TimeUnit.SECONDS)
		// .convertDurationsTo(TimeUnit.MILLISECONDS).build();

		final Slf4jReporter reporter = Slf4jReporter.forRegistry(registry).outputTo(new AbstractLogger()).convertRatesTo(TimeUnit.SECONDS).convertDurationsTo(TimeUnit.MILLISECONDS)
		        .build();
		reporter.start(1, TimeUnit.SECONDS);
	}
}
