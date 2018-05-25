package com.atg.openssp.dataprovider.service;

import com.atg.openssp.common.buffer.SSPLatencyBuffer;
import com.atg.openssp.common.exception.RequestException;
import com.atg.openssp.common.provider.DataHandler;
import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Servlet abstract class CoreDataServlet
 *
 * @author André Schmer
 */
public abstract class CoreDataServlet<T extends DataHandler> extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(CoreDataServlet.class);

    @Override
    public void init() throws ServletException {
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

        final Stopwatch stopwatch = Stopwatch.createStarted();
        T handler = null;
        boolean hasResult = false;
        try {
            handler = getHandler(request, response);
        } catch (final RequestException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        } finally {
            stopwatch.stop();
            if (hasResult) {
                SSPLatencyBuffer.getBuffer().bufferValue(stopwatch.elapsed(TimeUnit.MILLISECONDS));
            }
            if (handler != null) {
                handler.cleanUp();
            }
            handler = null;
        }

    }

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }


    protected abstract T getHandler(HttpServletRequest request, HttpServletResponse response) throws RequestException;

}
