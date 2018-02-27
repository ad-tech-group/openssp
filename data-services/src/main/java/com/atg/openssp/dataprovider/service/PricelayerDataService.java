package com.atg.openssp.dataprovider.service;

import com.atg.openssp.common.exception.RequestException;
import com.atg.openssp.dataprovider.provider.handler.DataHandler;
import com.atg.openssp.dataprovider.provider.handler.PricelayerDataHandler;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class PricelayerService
 * 
 * @author Brian Sorensen
 */
@WebServlet(value = PricelayerDataHandler.CONTEXT, asyncSupported = false, name = "PricelayerData-Service")
public class PricelayerDataService extends CoreDataServlet<DataHandler> {

	private static final long serialVersionUID = 1L;

	@Override
	protected DataHandler getHandler(final HttpServletRequest request, final HttpServletResponse response) throws RequestException {
		return new PricelayerDataHandler(request, response);
	}

}
