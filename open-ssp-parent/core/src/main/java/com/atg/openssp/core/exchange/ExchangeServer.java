package com.atg.openssp.core.exchange;

import com.atg.openssp.common.core.entry.SessionAgent;
import com.atg.openssp.common.core.exchange.Exchange;
import com.atg.openssp.common.core.exchange.ExchangeExecutorServiceFacade;
import com.atg.openssp.common.provider.AdProviderReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.math.FloatComparator;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * This is the server which is mainly responsible to start the bidprocess, collect the result and build a reponse for the client.
 * 
 * @author André Schmer
 *
 */
public class ExchangeServer implements Exchange<RequestSessionAgent> {

	private static final Logger log = LoggerFactory.getLogger(ExchangeServer.class);

	/**
	 * Starts the process to exchange and build a response if a {@code VideoResult} can be expected.
	 * <p>
	 * Principle of work is the following:
	 * <ul>
	 * <li>fetch a list of channels</li>
	 * <li>invoke the callables due to the {@link ExchangeExecutorServiceFacade}</li>
	 * <li>Evaluates a winner from the list of futures by making a simple price comparison where the highest price wins</li>
	 * <li>If a valid winner is evaluated, the response is build and post bid operations such as winningnotifying can be processed</li>
	 * </ul>
	 * 
	 * @param {@link
	 *            RequestSessionAgent}
	 * 
	 * @return true if a provider, {@link AdProviderReader}, exists and building a response is successful, false otherwise
	 */
	@Override
	public boolean processExchange(final RequestSessionAgent agent) {
		final AdProviderReader winner = execute(agent);
		return evaluateResponse(agent, winner);
	}

	private AdProviderReader execute(final SessionAgent agent) {
		try {
			final List<Callable<AdProviderReader>> callables = ChannelFactory.createListOfChannels(agent);
			final List<Future<AdProviderReader>> futures = ExchangeExecutorServiceFacade.instance.invokeAll(callables);
			final Future<AdProviderReader> winnerFuture = futures.stream().reduce(ExchangeServer::validate).orElse(null);
			if (winnerFuture != null) {
				try {
					return winnerFuture.get();
				} catch (final ExecutionException e) {
					log.error(e.getMessage(), e);
				}
			} else {
				log.error("no winner detected");
			}
		} catch (final InterruptedException e) {
			log.error(e.getMessage());
		}
		return null;
	}

	private static Future<AdProviderReader> validate(final Future<AdProviderReader> a, final Future<AdProviderReader> b) {
		try {
			if (b.get() == null) {
				return a;
			}
			if (a.get() == null) {
				return b;
			}

			if (FloatComparator.greaterThanWithPrecision(a.get().getAdjustedCurrencyPrice(), b.get().getAdjustedCurrencyPrice())) {
				return a;
			}
		} catch (final InterruptedException e) {
			log.error(e.getMessage());
		} catch (final CancellationException e) {
			log.error(e.getMessage());
		} catch (final ExecutionException e) {
			log.error(e.getMessage(), e);
		}

		return b;
	}

	private boolean evaluateResponse(final SessionAgent agent, final AdProviderReader winner) {
		try (Writer out = agent.getHttpResponse().getWriter()) {
			if (winner != null && winner.isValid()) {

				final String responseData;
				if (winner instanceof Auction.AuctionResult) {
					responseData = ((Auction.AuctionResult)winner).buildHeaderBidResponse();

				} else {
					responseData = winner.buildResponse();
				}
				out.append(responseData);

				agent.getHttpResponse().setContentType("application/javascript");
				winner.perform(agent);
				out.flush();
				return true;
				/*
			} else {
				// remove this in production environmant
				if (agent.getParamValues().getIsTest().equals("1")) {
					agent.getHttpResponse().setContentType("application/json");
					final String responseData = "{\"result\":\"Success\", \"message\":\"OpenSSP is working.\"}";
					out.append(responseData);
					out.flush();
					return true;
				}
				*/
			}
		} catch (final IOException e) {
			log.error(e.getMessage(), e);
		}
		return false;
	}

}
