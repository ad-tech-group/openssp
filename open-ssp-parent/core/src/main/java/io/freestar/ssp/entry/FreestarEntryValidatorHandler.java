package io.freestar.ssp.entry;

import com.atg.openssp.common.demand.ParamValue;
import com.atg.openssp.common.exception.EmptyCacheException;
import com.atg.openssp.common.exception.RequestException;
import com.atg.openssp.core.cache.type.AppDataCache;
import com.atg.openssp.core.cache.type.SiteDataCache;
import com.atg.openssp.core.entry.EntryValidatorHandler;
import io.freestar.ssp.common.demand.FreestarParamValue;

import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FreestarEntryValidatorHandler extends EntryValidatorHandler {
    @Override
    public ParamValue validateEntryParams(HttpServletRequest request) throws RequestException {
        final FreestarParamValue pm = new FreestarParamValue();

        System.out.println("###################################################################################");
        Cookie[] cList = request.getCookies();
        if (cList != null) {
            System.out.println(cList.length);
            for (Cookie c : cList) {
                System.out.println("cookie: "+c.getName());
            }
        } else {
            System.out.println("no cookies");
        }
        // Note:
        // You may define your individual parameter or payloadto work with.
        // Neither the "ParamValue" - object nor the list of params may fit to your requirements out of the box.

        // geo data could be solved by a geo lookup service and ipaddress

        if (request.getContentLength() > 0) {
            byte[] buffer = new byte[request.getContentLength()];
            try {
                ServletInputStream is = request.getInputStream();
                is.read(buffer);
                String json = new String(buffer);
                System.out.println("I got json content!!!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        final String siteid = request.getParameter("site");
        final String appid = request.getParameter("app");
        try {
            pm.setSite(SiteDataCache.instance.get(siteid));
        } catch (final EmptyCacheException e) {
            try {
                pm.setApp(AppDataCache.instance.get(appid));
            } catch (final EmptyCacheException e1) {
                throw new RequestException(e1.getMessage());
            }
        }

        // pm.setDomain(checkValue(request.getParameter("domain"), ERROR_CODE.E906, "Domain"));
        // pm.setH(checkValue(request.getParameter("h"), ERROR_CODE.E906, "Height"));
        // pm.setW(checkValue(request.getParameter("w"), ERROR_CODE.E906, "Width"));
        // pm.setMimes(convertMimes(request.getParameter("mimes")));
        // pm.setPage(checkValue(request.getParameter("page"), pm.getDomain()));
        // pm.setStartdelay(Integer.valueOf(checkValue(request.getParameter("sd"), "0")));
        // pm.setProtocols(convertProtocolValues(request.getParameter("prot")));

        return pm;
    }
}
