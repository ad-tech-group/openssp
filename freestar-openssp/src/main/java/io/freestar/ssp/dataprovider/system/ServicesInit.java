package io.freestar.ssp.dataprovider.system;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class ServicesInit extends GenericServlet {

    @Override
    public void init() throws ServletException {
        super.init();
    }

        @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
    }

    @Override
    public void destroy() {
        super.destroy();
    }

}
