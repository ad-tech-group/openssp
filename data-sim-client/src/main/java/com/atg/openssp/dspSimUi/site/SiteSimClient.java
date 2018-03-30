package com.atg.openssp.dspSimUi.site;

import com.atg.openssp.dspSimUi.model.ModelException;
import com.atg.openssp.dspSimUi.model.site.SiteModel;
import com.atg.openssp.dspSimUi.view.site.SiteView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Brian Sorensen
 */
public class SiteSimClient {
    private static final Logger log = LoggerFactory.getLogger(SiteSimClient.class);
    private final SiteView siteView;
    private SiteModel siteModel;

    public SiteSimClient() throws ModelException {
        siteModel = new SiteModel();
        siteView = new SiteView(siteModel);
    }

    public void start() {
        siteView.start();
        siteModel.start();
    }

    public static void main(String[] args) {
        try {
            SiteSimClient sim = new SiteSimClient();
            sim.start();
        } catch (ModelException e) {
            log.error(e.getMessage(), e);
        }
    }

}