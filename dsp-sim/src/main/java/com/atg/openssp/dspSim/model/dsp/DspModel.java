package com.atg.openssp.dspSim.model.dsp;

import com.atg.openssp.dspSim.model.ModelException;
import com.atg.openssp.dspSim.model.client.ClientCommandType;
import com.atg.openssp.dspSim.model.dsp.filter.DspReturnFilter;
import com.atg.openssp.dspSim.model.dsp.filter.PassthroughFilter;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import openrtb.bidrequest.model.*;
import openrtb.bidresponse.model.Bid;
import openrtb.bidresponse.model.BidResponse;
import openrtb.bidresponse.model.SeatBid;
import openrtb.tables.ContentCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Constructor;
import java.util.*;

/**
 * @author Brian Sorensen
 */
public class DspModel {
    private static final Logger log = LoggerFactory.getLogger(DspModel.class);
    private final ArrayList<SimBidderListener> simBidderListeners = new ArrayList<SimBidderListener>();
    private final ArrayList<SimBidder> bList = new ArrayList<SimBidder>();
    private final HashMap<String, SimBidder> bMap = new LinkedHashMap<String, SimBidder>();
    private final Properties properties = new Properties();
    private final File modelFile;
    private DspReturnFilter filter;
    private ClientCommandType mode = ClientCommandType.RETURN_NORMAL;

    public DspModel() throws ModelException {
        modelFile = new File("DSP_SIM_MODEL.json");

        loadProperties();
        loadModel();
        loadFilter();
    }

    private void loadProperties() throws ModelException {
        File file = new File("dsp-sim.properties");
        if (file.exists()) {
            try {
                FileInputStream is = new FileInputStream(file);
                properties.load(is);
                is.close();
            } catch (FileNotFoundException e) {
                throw new ModelException(e);
            } catch (IOException e) {
                throw new ModelException(e);
            }
        } else {
            try {
                InputStream is = getClass().getClassLoader().getResourceAsStream(file.getName());
                if (is != null) {
                    properties.load(is);
                    is.close();
                } else {
                    throw new ModelException("properties file missing: "+file);
                }
            } catch (IOException e) {
                throw new ModelException(e);
            }
        }
    }

    private void loadModel() throws ModelException {
        GsonBuilder builder = new GsonBuilder();
        SimBidder.populateTypeAdapters(builder);

        try {
            if (modelFile.exists()) {
                FileReader fr = new FileReader(modelFile);
                List<SimBidder> buffer = builder.create().fromJson(fr, new TypeToken<List<SimBidder>>(){}.getType());
                fr.close();
                bList.addAll(buffer);
                for (SimBidder sb : bList) {
                    bMap.put(sb.getId(), sb);
                }
            }
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            System.out.println(e.getMessage());
            throw new ModelException("Could not load model from store: "+modelFile.getAbsolutePath());
        }
    }

    public void saveModel() throws ModelException {
        GsonBuilder builder = new GsonBuilder();
        SimBidder.populateTypeAdapters(builder);
        try {
            PrintWriter fw = new PrintWriter(new FileWriter(modelFile));
            String json = builder.create().toJson(bList);
            fw.println(json);
            fw.close();
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
            throw new ModelException("Could not save model to store: "+modelFile);
        }
    }

    private void loadFilter() throws ModelException {
        if (filter == null) {
            String filterClassName = this.getProperty("filter-class");
            if (filterClassName == null) {
                filter = new PassthroughFilter();
            } else {
                try {
                    Class<? extends DspReturnFilter> filterClass = (Class<? extends DspReturnFilter>) Class.forName(filterClassName);
                    Constructor<? extends DspReturnFilter> cc = filterClass.getConstructor(new Class[]{});
                    filter = cc.newInstance(new Object[]{});
                } catch (Exception e) {
                    throw new ModelException(e.getMessage());
                }
            }
        }
    }

    public synchronized void add(SimBidder sb) {
        bList.add(sb);
        bMap.put(sb.getId(), sb);
        notifySimBidderAdded(sb);
    }

    private void notifySimBidderAdded(SimBidder sb) {
        for (SimBidderListener lis : simBidderListeners) {
            lis.added(sb);
        }
    }

    public synchronized void remove(SimBidder sb) {
        bList.remove(sb);
        bMap.remove(sb.getId());
        notifySimBidderRemoved(sb);
    }

    private void notifySimBidderRemoved(SimBidder sb) {
        for (SimBidderListener lis : simBidderListeners) {
            lis.removed(sb);
        }
    }

    public BidResponse createBidResponse(String server, int port, BidRequest request) {
        BidResponse response = new BidResponse();
        response.setId(request.getId());
        response.setBidid(UUID.randomUUID().toString());

        for (Impression i : request.getImp()) {
            for (SimBidder sb : bList) {
                response.addSeatBid(fabricateSeatBid(server, port, request.getSite(), sb, i));
            }
        }
        return response;
    }

    private SeatBid fabricateSeatBid(String server, int port, Site site, SimBidder simBidder, Impression i) {
        SeatBid sb = new SeatBid();
        Bid b = new Bid();
        sb.getBid().add(b);
        b.setId(simBidder.getId());
        b.setImpid(i.getId());
        b.setPrice(simBidder.getPrice());
        b.setAdid(simBidder.getAdid());
        String nUrl = "http://"+server+":"+port+"/win?i="+simBidder.getId()+"&price=${AUCTION_PRICE}";
        b.setNurl(nUrl);
//        b.setNurl(simBidder.getNUrl());
        b.setAdm(simBidder.getAdm());
        b.setAdomain(simBidder.getAdomain());
        b.setIurl(simBidder.getIurl());
        b.setCid(simBidder.getCid());
        b.setCrid(simBidder.getCrid());
        b.addAllCats(site.getCat());
        List<ContentCategory> c2  = site.getPagecat();
        List<ContentCategory> c3  = site.getSectioncat();
        if (site.getPublisher() != null) {
            for (ContentCategory c : site.getPublisher().getCat()) {
                if (!b.getCat().contains(c)) {
                    b.addCat(c);
                }
            }
        }
        if (site.getPublisher() != null) {
            List<ContentCategory> c4 = site.getPublisher().getCat();
        }

        if (i.hasVideo()) {
            Video video = i.getVideo();
            b.setH(video.getH());
            b.setW(video.getW());
        } else if (i.hasBanner()) {
            Banner banner = i.getBanner();
            b.setH(banner.getH());
            b.setW(banner.getW());
        }
        return sb;
    }


    public synchronized void addSimBidderListener(SimBidderListener lis) {
        simBidderListeners.add(lis);
        for (SimBidder sb : bList) {
            lis.added(sb);
        }
    }

    public void removeSimBidderListener(SimBidderListener lis) {
        simBidderListeners.remove(lis);
    }

    public List<SimBidder> getBidders() {
        return bList;
    }

    public SimBidder lookupBidder(String id) {
        return bMap.get(id);
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        String v = getProperty(key);
        if (v == null) {
            return defaultValue;
        } else {
            return v;
        }
    }

    public String filterResult(BidResponse brsp) {
        return filter.filterResult(brsp);
    }

    public void setMode(ClientCommandType mode) {
        this.mode = mode;
    }

    public ClientCommandType getMode() {
        return mode;
    }
}
