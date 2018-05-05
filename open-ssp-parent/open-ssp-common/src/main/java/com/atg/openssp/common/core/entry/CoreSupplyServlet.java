package com.atg.openssp.common.core.entry;

import java.io.IOException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atg.openssp.common.exception.ERROR_CODE;

import com.atg.openssp.common.logadapter.TimeInfoLogProcessor;
import org.apache.http.client.HttpResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atg.openssp.common.buffer.SSPLatencyBuffer;
import com.atg.openssp.common.core.exchange.Exchange;
import com.atg.openssp.common.exception.RequestException;
import com.google.common.base.Stopwatch;

/**
 * @author André Schmer
 *
 */
public abstract class CoreSupplyServlet<T extends SessionAgent> extends HttpServlet {

	private static final Logger LOG = LoggerFactory.getLogger(CoreSupplyServlet.class);

	private static final long serialVersionUID = 1L;

	private Exchange<T> server;

	@Override
	public void init() throws ServletException {
		server = getServer();
	}

	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

		long startTS = System.currentTimeMillis();
		final Stopwatch stopwatch = Stopwatch.createStarted();
		T agent = null;
		boolean hasResult = false;
		try {
			agent = getAgent(request, response);
			hasResult = server.processExchange(agent);
			System.out.println("exchange returned result");
		} catch (final RequestException e) {
            TimeInfoLogProcessor.instance.setLogData(agent.getRequestid(), "fault-401");
			response.sendError(401, e.getMessage());
		} catch (final CancellationException e) {
            TimeInfoLogProcessor.instance.setLogData(agent.getRequestid(), "fault-200");
			response.sendError(200, "exchange timeout");
		} catch (final Exception e) {
            TimeInfoLogProcessor.instance.setLogData(agent.getRequestid(), "fault");
			LOG.error(e.getMessage(), e);
		} finally {
			stopwatch.stop();
			if (hasResult) {
				SSPLatencyBuffer.getBuffer().bufferValue(stopwatch.elapsed(TimeUnit.MILLISECONDS));
			}
			if (agent != null) {
				agent.cleanUp();
			}
			long endTS = System.currentTimeMillis();
			TimeInfoLogProcessor.instance.setLogData(agent.getRequestid(), startTS, endTS, endTS-startTS);
			agent = null;
		}
	}

	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	protected abstract T getAgent(HttpServletRequest request, HttpServletResponse response) throws RequestException;

	protected abstract Exchange<T> getServer();
}
