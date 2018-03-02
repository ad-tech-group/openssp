package com.atg.openssp.dspSim.model.ad;

import com.atg.openssp.dspSim.model.BaseModel;

import java.util.Properties; /**
 * @author Brian Sorensen
 */
public class AdModel extends BaseModel {
    private final Properties props;

    public AdModel(Properties props) {
        this.props = props;
    }

    public String lookupProperty(String key) {
        return props.getProperty(key);
    }
}
