package com.atg.openssp.dataprovider.system;

import com.atg.openssp.common.core.system.job.WatchdogService;
import com.atg.openssp.common.core.system.loader.ConfigLoader;
import com.atg.openssp.common.core.system.loader.GlobalContextLoader;
import com.atg.openssp.common.core.system.loader.LocalContextLoader;
import openrtb.tables.ContentCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.Timer;
import java.util.concurrent.CountDownLatch;

/**
 * Servlet implementation class AppDataService
 *
 * @author Brian Sorensen
 */
public class ServicesInit extends GenericServlet {

    private final Logger log = LoggerFactory.getLogger(ServicesInit.class);

    @Override
    public void init() throws ServletException {
        InitLogging.setSystemProperties();
        log.info("**** Initing core application ****");
        //LocalContext.setVersion(new MavenProperties().getVersion());
        //log.info("**** SSP Version: " + LocalContext.getVersion() + " ****");

        final CountDownLatch cdl = new CountDownLatch(3);
        // loading static config
        new ConfigLoader(cdl).readValues();
        // initing watchdogs for global.runtime.xml and local.runtime.xml
        WatchdogService.instance.initLoaderWatchdog(new LocalContextLoader(cdl), true).initLoaderWatchdog(new GlobalContextLoader(cdl), true).startWatchdogs();


        try {
            cdl.await();
        } catch (final InterruptedException e) {
            log.error(e.getMessage());
        }
        super.init();
        for (ContentCategory c: ContentCategory.values()) {
            // do the add
        }
    }

        @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
    }

    @Override
    public void destroy() {
        super.destroy();
    }

}
