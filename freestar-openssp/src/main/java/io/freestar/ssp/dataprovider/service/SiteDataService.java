package io.freestar.ssp.dataprovider.service;

import com.atg.openssp.common.exception.RequestException;
import io.freestar.ssp.dataprovider.provider.handler.DataHandler;
import io.freestar.ssp.dataprovider.provider.handler.SiteDataHandler;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SupplyVideoService
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
