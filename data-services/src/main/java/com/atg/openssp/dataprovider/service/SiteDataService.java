package com.atg.openssp.dataprovider.service;

import com.atg.openssp.common.exception.RequestException;
import com.atg.openssp.dataprovider.provider.handler.DataHandler;
import com.atg.openssp.dataprovider.provider.handler.SiteDataHandler;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SiteDataService
 * 
 * @author Brian Sorensen
 */
@WebServlet(value = SiteDataHandler.CONTEXT, asyncSupported = false, name = "SiteData-Service")
public class SiteDataService extends CoreDataServlet<DataHandler> {

	private static final long serialVersionUID = 1L;

	@Override
	protected DataHandler getHandler(final HttpServletRequest request, final HttpServletResponse response) throws RequestException {
		return new SiteDataHandler(request, response);
	}

}
