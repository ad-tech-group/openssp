package com.atg.openssp.dataprovider.service;

import com.atg.openssp.common.exception.RequestException;
import com.atg.openssp.dataprovider.provider.handler.AppDataHandler;
import com.atg.openssp.dataprovider.provider.handler.DataHandler;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AppDataHandler
 * 
 * @author Brian Sorensen
 */
@WebServlet(value = AppDataHandler.CONTEXT, asyncSupported = false, name = "AppData-Service")
public class AppDataService extends CoreDataServlet<DataHandler> {

	private static final long serialVersionUID = 1L;

	@Override
	protected DataHandler getHandler(final HttpServletRequest request, final HttpServletResponse response) throws RequestException {
		return new AppDataHandler(request, response);
	}

}
