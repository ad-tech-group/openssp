package com.atg.openssp.common.provider;

import com.atg.openssp.common.configuration.ContextCache;
import com.atg.openssp.common.configuration.ContextProperties;
import com.atg.openssp.common.configuration.GlobalContext;
import com.atg.openssp.common.core.system.LocalContext;
import com.atg.openssp.common.provider.dto.TokenWrapper;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.UUID;

/**
 * @author André Schmer
 */
public class LoginHandler extends DataHandler {
    private static final Logger log = LoggerFactory.getLogger(LoginHandler.class);
    public static final String TOKEN = "Kermit-Says-Yes";
    public static final String CONTEXT = "/login/token";
    private final String user;
    private final String pw;

    public LoginHandler(HttpServletRequest request, HttpServletResponse response) {
        user = ContextCache.instance.get(ContextProperties.MASTER_USER);
        pw = ContextCache.instance.get(ContextProperties.MASTER_PW);

        if (LocalContext.isLoginServiceEnabled()) {
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
        } else {
            response.setStatus(404);
        }
    }

    @Override
    public void cleanUp() {

    }

    private boolean isAuthorized(String user, String pw) {
        return user != null && this.user.equals(user) && pw != null && this.pw.equals(pw);
    }

}
