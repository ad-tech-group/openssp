package io.freestar.ssp.dataprovider.service;

import com.atg.openssp.common.exception.RequestException;
import io.freestar.ssp.dataprovider.provider.handler.DataHandler;
import io.freestar.ssp.dataprovider.provider.handler.PricelayerDataHandler;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SupplyVideoService
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
