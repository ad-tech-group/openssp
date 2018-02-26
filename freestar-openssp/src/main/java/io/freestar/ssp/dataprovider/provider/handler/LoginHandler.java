package io.freestar.ssp.dataprovider.provider.handler;

import com.google.gson.Gson;
import io.freestar.ssp.dataprovider.provider.dto.TokenWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class LoginHandler extends DataHandler {
    private static final Logger log = LoggerFactory.getLogger(LoginHandler.class);
    public static final String TOKEN = "liverworst-5";
    public static final String CONTEXT = "/login/token";

    public LoginHandler(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String,String> parms;
            if ("POST".equalsIgnoreCase(request.getMethod())) {
                String body = queryFromBodyString(request.getInputStream());
                parms = attributesToMap(request);
                populateFromBody(parms, body);
            } else {
                parms = queryToMap(request.getQueryString());
            }
            String user = parms.get("u");
            String pw = parms.get("p");
            if (!isAuthorized(user, pw)) {
                response.setStatus(401);
            } else {
                TokenWrapper token = new TokenWrapper();
                token.setToken(TOKEN);
                String result = new Gson().toJson(token);
                response.setContentType("application/json; charset=UTF8");
                response.setStatus(200);
                response.setContentLength(result.length());
                OutputStream os = response.getOutputStream();
                os.write(result.getBytes());
                os.close();
            }
        } catch (IOException e) {
            response.setStatus(500);
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void cleanUp() {

    }

    private boolean isAuthorized(String user, String pw) {
        return user != null && "izod".equals(user) && pw != null && "frogs".equals(pw);
    }

}
