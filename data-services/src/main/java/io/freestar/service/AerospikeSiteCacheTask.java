package io.freestar.service;

import com.google.common.collect.Lists;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.ProtocolStringList;
import io.freestar.sites.api.interfaces.SitesProto;
import openrtb.bidrequest.model.Publisher;
import openrtb.bidrequest.model.Site;
import openrtb.tables.ContentCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;

public class AerospikeSiteCacheTask extends TimerTask {
    private static final Logger log = LoggerFactory.getLogger(AerospikeSiteCacheTask.class);
    private final List<Site> cache = new ArrayList<>();
    private static AerospikeSiteCacheTask instance;

    @Override
    public void run() {
        fillCache();
    }

    public List<Site> getCache() {
        return cache;
    }

    private void fillCache() {
        final byte[] siteListBytes = (byte[]) AerospikeService.getInstance().get("iab-category", "site-list", "bytes");
        final String siteListString = new String(siteListBytes);
        final List<String> siteIdList = Arrays.asList(siteListString.split(","));
        final List<Site> newCache = new ArrayList<>();

        for (String siteId : siteIdList) {
            byte[] protoBytes = (byte[]) AerospikeService.getInstance().get("iab-category", siteId, "iabIds");
            if (protoBytes != null) {
                try {
                    final SitesProto.Site site = SitesProto.Site.parseFrom(protoBytes);
                    Site site1 = new Site();
                    site1.setId(Integer.toString(site.getId()));
                    site1.setName(site.getName());
                    site1.setDomain(site.getDomain());
                    site1.setCat(toContentCategoryList(site.getCatList()));
                    site1.setPublisher(toPublisher(site.getPublisher()));

                    newCache.add(site1);

                } catch (InvalidProtocolBufferException e) {
                    log.error("Error parsing site protobuf", e);
                }
            }
        }

        synchronized (cache) {
            cache.clear();
            cache.addAll(newCache);
        }
    }

    private Publisher toPublisher(SitesProto.Publisher publisher) {
        final Publisher publisher1 = new Publisher();
        publisher1.setId(Integer.toString(publisher.getId()));
        publisher1.setCat(toContentCategoryList(publisher.getCatList()));
        publisher1.setDomain(publisher.getDomain());
        publisher1.setExt(publisher.getExt());

        return publisher1;
    }

    private List<ContentCategory> toContentCategoryList(ProtocolStringList catList) {
        final List<ContentCategory> list = Lists.newArrayList();

        for (String cat : catList) {
            final ContentCategory contentCategory = ContentCategory.convertValue(cat);

            if (contentCategory != null) {
                list.add(contentCategory);
            }
        }
        return list;
    }

    public static AerospikeSiteCacheTask getInstance() {
        if (instance == null) {
            instance = new AerospikeSiteCacheTask();
        }

        return instance;
    }
}
