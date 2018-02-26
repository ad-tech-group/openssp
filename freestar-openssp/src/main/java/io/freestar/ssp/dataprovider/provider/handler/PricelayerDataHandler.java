package io.freestar.ssp.dataprovider.provider.handler;

import com.atg.openssp.core.cache.broker.dto.PricelayerDto;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class PricelayerDataHandler extends DataHandler {
    private static final Logger log = LoggerFactory.getLogger(PricelayerDataHandler.class);
    public static final String CONTEXT = "/lookup/pricelayer";

    public PricelayerDataHandler(HttpServletRequest request, HttpServletResponse response) {
        try {
            Gson gson = new Gson();
            String content = new String(Files.readAllBytes(Paths.get("price_layer.json")), StandardCharsets.UTF_8);
            PricelayerDto data = gson.fromJson(content, PricelayerDto.class);

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
        }
    }

    @Override
    public void cleanUp() {

    }

}
