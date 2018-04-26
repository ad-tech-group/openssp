package com.atg.openssp.dataprovider.provider.handler.currency;

import com.atg.openssp.common.core.broker.dto.CurrencyDto;
import com.atg.openssp.common.core.exchange.geo.UnavailableHandlerException;
import com.atg.openssp.common.core.system.LocalContext;
import com.atg.openssp.common.model.EurRef;
import com.atg.openssp.common.provider.DataHandler;
import com.atg.openssp.common.provider.LoginHandler;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Brian Sorensen
 */
public class EcbEuropaDataHandler extends DataHandler {
    private static final Logger log = LoggerFactory.getLogger(EcbEuropaDataHandler.class);
    private final String USER_AGENT = "Mozilla/5.0";

    public EcbEuropaDataHandler(HttpServletRequest request, HttpServletResponse response) throws UnavailableHandlerException {
        if (LocalContext.isCurrencyDataServiceEnabled()) {
            try {
                URL url = new URL("http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("User-Agent", USER_AGENT);
                int responseCode = con.getResponseCode();
                if (responseCode != 200) {
                    throw new UnavailableHandlerException("failed with response code 200");
                }
                InputStream in = con.getInputStream();

                CurrencyDto data = new CurrencyDto();
                String cc = "EUR";
                data.setCurrency(cc);
                List<EurRef> list = new ArrayList();
                data.setData(list);

                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(in);
                in.close();

                Element e0 = doc.getDocumentElement();
                if ("gesmes:Envelope".equals(e0.getTagName())) {
                    NodeList e0List = e0.getChildNodes();
                    for (int i=0; i<e0List.getLength(); i++) {
                        Node n1 = e0List.item(i);
                        if (n1 instanceof Element) {
                            Element e1 = (Element) n1;
                            if ("Cube".equals(e1.getTagName())) {
                                NodeList cubes = e1.getElementsByTagName("Cube");
                                for (int j=0; j<cubes.getLength(); j++) {
                                    Element cube = (Element) cubes.item(j);
                                    String cur = cube.getAttribute("currency");
                                    if (cur.length() == 0) {
                                        continue;
                                    }
//                                    String rateString = cube.getAttribute("rate=");
                                    EurRef ref = new EurRef();
                                    ref.setCurrency(cur);
//                                        System.out.println(cur+":"+cube.getAttribute("rate"));
                                    ref.setRate(Float.parseFloat(cube.getAttribute("rate")));
                                    list.add(ref);
                                }
                            }
                        }
                    }

                }


                Map<String,String> parms = queryToMap(request.getQueryString());
                String t = parms.get("t");

                if (LoginHandler.TOKEN.equals(t)) {
                    String result = new Gson().toJson(data);

                    response.setStatus(200);
                    response.setContentType("application/json; charset=UTF8");
                    OutputStream os = response.getOutputStream();
                    os.write(result.getBytes());
                    os.close();
                } else {
                    response.setStatus(401);
                }
            } catch (IOException e) {
                response.setStatus(500);
                log.error(e.getMessage(), e);
            } catch (ParserConfigurationException e) {
                response.setStatus(500);
                log.error(e.getMessage(), e);
            } catch (SAXException e) {
                response.setStatus(500);
                log.error(e.getMessage(), e);
            }
        } else {
            response.setStatus(404);
        }
    }

    @Override
    public void cleanUp() {

    }

}
