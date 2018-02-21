package com.atg.openssp.core.entry;

import javax.servlet.http.HttpServletRequest;

import com.atg.openssp.common.configuration.GlobalContext;
import com.atg.openssp.common.demand.ParamValue;
import com.atg.openssp.common.exception.RequestException;

/**
 * The kind and diversity of request parameters my be vary and depends on the different conditions. See also at {@link ParamValue} for the kind aof params.
 * 
 * @author André Schmer
 *
 */
public class EntryValidator {

    private EntryValidatorHandler handler;

    public EntryValidator() {
	    String handlerClassName = GlobalContext.getEntryValidatorHandlerClass();
	    if (handlerClassName != null && !"".equals(handlerClassName)) {
            try {
                Class c = Class.forName(handlerClassName);
                handler = (EntryValidatorHandler) c.getConstructor(null).newInstance(null);
            } catch (Exception e) {
                handler = new TestEntryValidatorHandler();
                e.printStackTrace();
            }
        } else {
	        handler = new TestEntryValidatorHandler();
        }
	}

	/**
	 * Extracts and validates the request parameters.
	 * 
	 * @param request
	 * @throws RequestException
	 *             if params are mandatory and empty and cannot be replaced by an alternative value.
	 * @return {@link ParamValue}
	 */
	public ParamValue validateEntryParams(final HttpServletRequest request) throws RequestException {
	    return handler.validateEntryParams(request);
	}

	// private static String checkValue(final String toCheck, final ERROR_CODE code, final String message) throws RequestException {
	// if (StringUtils.isEmpty(toCheck)) {
	// throw new RequestException(code, message);
	// }
	// return toCheck;
	// }

	// private static String checkValue(final String toCheck, final String alternative) {
	// if (StringUtils.isEmpty(toCheck)) {
	// return alternative;
	// }
	// return toCheck;
	// }

	// private static List<Integer> convertProtocolValues(final String sProtocols) {
	// final List<Integer> protocols = new ArrayList<>();
	// if (!StringUtils.isEmpty(sProtocols)) {
	// final String[] protos = sProtocols.split(",");
	// for (final String protocol : protos) {
	// protocols.add(VideoBidResponseProtocol.convertFromString(protocol));
	// }
	// } else {
	// protocols.add(VideoBidResponseProtocol.VAST_3_0.getValue());
	// }
	// return protocols;
	// }

	// private static List<String> convertMimes(final String mimeTypes) {
	// List<String> mimeTypeList = new ArrayList<>();
	// if (!StringUtils.isEmpty(mimeTypes)) {
	// mimeTypeList = Arrays.asList(mimeTypes.split(","));
	// return mimeTypeList;
	// }
	// // default
	// mimeTypeList.add("video/mp4");
	// return mimeTypeList;
	// }
}
