package com.atg.openssp.dataprovider.provider.model;

import com.atg.openssp.common.cache.dto.BannerAd;
import com.atg.openssp.common.cache.dto.VideoAd;
import com.atg.openssp.common.core.broker.dto.*;
import com.atg.openssp.common.demand.Supplier;
import com.atg.openssp.common.model.EurRef;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import openrtb.bidrequest.model.Pricelayer;
import openrtb.bidrequest.model.Site;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataStore {
    private static final Logger log = LoggerFactory.getLogger(DataStore.class);
//    private final static String dbURL = "jdbc:derby:/opt/tomcat/properties/OpenSSPDataServiceDB;create=true";
    private final static String dbURL1 = "jdbc:derby:OpenSSPDataServiceDB";
    private final static String dbURL2 = "jdbc:derby:OpenSSPDataServiceDB;create=true";
    private static final String TABLE_SITES = "SITES";
    private static final String TABLE_PRICELAYERS = "PRICELAYERS";
    private static final String TABLE_SUPPLIERS = "SUPPLIERS";
    private static final String TABLE_CURRENCY = "CURRENCY";
    private static final String TABLE_VIDEO_ADS = "VIDEO_ADS";
    private static final String TABLE_BANNER_ADS = "BANNER_ADS";
    private final GsonBuilder builder;
    private static DataStore singleton;

    private DataStore() throws ClassNotFoundException {
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        builder = new GsonBuilder();
        EurRef.populateTypeAdapters(builder);
        Site.populateTypeAdapters(builder);
        Pricelayer.populateTypeAdapters(builder);
        Supplier.populateTypeAdapters(builder);
    }

    public synchronized static DataStore getInstance() {
        if (singleton == null) {
            try {
                singleton = new DataStore();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return singleton;
    }

    public CurrencyDto lookupCurrency() {
        try {
            List<String> working = lookupQuerry(TABLE_CURRENCY);
            if (working.size() > 0) {
                String json = working.get(0);
                Gson gson = builder.create();
                return gson.fromJson(json, CurrencyDto.class);
            } else {
                CurrencyDto dto = new CurrencyDto();
                dto.setCurrency("EUR");
                dto.setData(new ArrayList<>());
                return dto;
            }
        } catch (SQLException e) {
            if (("Table/View '"+TABLE_CURRENCY+"' does not exist.").equals(e.getMessage())) {
                try {
                    createTable(TABLE_CURRENCY);
                } catch (SQLException e1) {
                    throw new RuntimeException(e1);
                }
                return lookupCurrency();
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    public void insert(CurrencyDto currency) {
        Gson gson = builder.create();
        insert(TABLE_CURRENCY, currency.getCurrency(), gson.toJson(currency));
    }

    public void update(CurrencyDto currency) {
        Gson gson = builder.create();
        int count = update(TABLE_CURRENCY, currency.getCurrency(), gson.toJson(currency));
        if (count == 0) {
            insert(currency);
        }
    }

    public void remove(CurrencyDto currency) {
        remove(TABLE_CURRENCY, currency.getCurrency());
    }

    public void clearCurrency() {
        clear(TABLE_CURRENCY);
    }

    public SiteDto lookupSites() {
        SiteDto dto = new SiteDto();
        dto.setSite(lookupAllSites());
        return dto;
    }

    private List<Site> lookupAllSites() {
        List<Site> list = new ArrayList<>();
        try {
            List<String> working = lookupQuerry(TABLE_SITES);
            Gson gson = builder.create();
            for (String json : working) {
                list.add(gson.fromJson(json, Site.class));
            }
            return list;
        } catch (SQLException e) {
            if (("Table/View '"+TABLE_SITES+"' does not exist.").equals(e.getMessage())) {
                try {
                    createTable(TABLE_SITES);
                } catch (SQLException e1) {
                    throw new RuntimeException(e1);
                }
                return lookupAllSites();
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    public void insert(Site site) {
        Gson gson = builder.create();
        insert(TABLE_SITES, site.getId(), gson.toJson(site));
    }

    public void update(Site site) {
        Gson gson = builder.create();
        int count = update(TABLE_SITES, site.getId(), gson.toJson(site));
        if (count == 0) {
            insert(site);
        }
    }

    public void remove(Site site) {
        remove(TABLE_SITES, site.getId());
    }

    public void clearSites() {
        clear(TABLE_SITES);
    }

    public PricelayerDto lookupPricelayers() {
        PricelayerDto dto = new PricelayerDto();
        dto.setPricelayer(lookupAllPricelayers());
        return dto;
    }

    private List<Pricelayer> lookupAllPricelayers() {
        List<Pricelayer> list = new ArrayList<>();
        try {
            List<String> working = lookupQuerry(TABLE_PRICELAYERS);
            Gson gson = builder.create();
            for (String json : working) {
                list.add(gson.fromJson(json, Pricelayer.class));
            }
        } catch (SQLException e) {
            if (("Table/View '"+TABLE_PRICELAYERS+"' does not exist.").equals(e.getMessage())) {
                try {
                    createTable(TABLE_PRICELAYERS);
                } catch (SQLException e1) {
                    throw new RuntimeException(e1);
                }
                return lookupAllPricelayers();
            } else {
                throw new RuntimeException(e);
            }
        }
        return list;
    }

    public void insert(Pricelayer pricelayer) {
        Gson gson = builder.create();
        insert(TABLE_PRICELAYERS, pricelayer.getSiteid(), gson.toJson(pricelayer));
    }

    public void update(Pricelayer pricelayer) {
        Gson gson = builder.create();
        int count = update(TABLE_PRICELAYERS, pricelayer.getSiteid(), gson.toJson(pricelayer));
        if (count == 0) {
            insert(pricelayer);
        }
    }

    public void remove(Pricelayer pricelayer) {
        remove(TABLE_PRICELAYERS, pricelayer.getSiteid());
    }

    public void clearPricelayer() {
        clear(TABLE_PRICELAYERS);
    }

    public SupplierDto lookupSuppliers() {
        SupplierDto dto = new SupplierDto();
        dto.setSupplier(lookupAllSuppliers());
        return dto;
    }

    private List<Supplier> lookupAllSuppliers() {
        List<Supplier> list = new ArrayList<>();
        try {
            List<String> working = lookupQuerry(TABLE_SUPPLIERS);
            Gson gson = builder.create();
            for (String json : working) {
                list.add(gson.fromJson(json, Supplier.class));
            }
            return list;
        } catch (SQLException e) {
            if (("Table/View '"+TABLE_SUPPLIERS+"' does not exist.").equals(e.getMessage())) {
                try {
                    createTable(TABLE_SUPPLIERS);
                } catch (SQLException e1) {
                    throw new RuntimeException(e1);
                }
                return lookupAllSuppliers();
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    public void insert(Supplier supplier) {
        Gson gson = builder.create();
        insert(TABLE_SUPPLIERS, Long.toString(supplier.getSupplierId()), gson.toJson(supplier));
    }

    public void update(Supplier supplier) {
        Gson gson = builder.create();
        int count = update(TABLE_SUPPLIERS, Long.toString(supplier.getSupplierId()), gson.toJson(supplier));
        if (count == 0) {
            insert(supplier);
        }
    }

    public void remove(Supplier supplier) {
        remove(TABLE_SUPPLIERS, Long.toString(supplier.getSupplierId()));
    }

    public void clearSupplier() {
        clear(TABLE_SUPPLIERS);
    }

    private List<String> lookupQuerry(String tableName) throws SQLException {
        List<String> list = new ArrayList<>();
        Connection c = null;
        Statement stmt = null;
        ResultSet rs = null;
        try
        {
            c = getConnection();
            stmt = getConnection().createStatement();
            rs = stmt.executeQuery("select * from "+tableName);
            while(rs.next())
            {
                list.add(rs.getString("JSON"));
            }
        }
        finally {
            close(c, stmt, rs);
        }
        return list;
    }

    private void insert(String table, String externalId, String json) {
        try {
            Connection c = null;
            Statement stmt = null;
            try
            {
                c = getConnection();
                stmt = getConnection().createStatement();
                int rs = stmt.executeUpdate("insert into "+table+" (XID, JSON) values ('"+externalId+"','"+json+"')");
                if (rs != 1) {
                    log.warn("insert failed: "+externalId);
                }
            }
            finally {
                close(c, stmt);
            }
        } catch (SQLException e) {
            if (("Table/View '"+table+"' does not exist.").equals(e.getMessage())) {
                try {
                    createTable(table);
                } catch (SQLException e1) {
                    throw new RuntimeException(e1);
                }
                insert(table, externalId, json);
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    private int update(String table, String externalId, String json) {
        try {
            Connection c = null;
            Statement stmt = null;
            try
            {
                c = getConnection();
                stmt = getConnection().createStatement();
                return stmt.executeUpdate("update " + table + " set JSON='" + json + "' where XID='" + externalId + "'");
            }
            finally {
                close(c, stmt);
            }
        } catch (SQLException e) {
            if (("Table/View '"+table+"' does not exist.").equals(e.getMessage())) {
                try {
                    createTable(table);
                } catch (SQLException e1) {
                    throw new RuntimeException(e1);
                }
                update(table, externalId, json);
            } else {
                throw new RuntimeException(e);
            }
        }
        return 0;
    }

    private void remove(String table, String externalId) {
        try {
            Connection c = null;
            Statement stmt = null;
            try
            {
                c = getConnection();
                stmt = getConnection().createStatement();
                String sql = "delete from "+table+" where XID='"+externalId+"'";
                log.debug(sql);
                stmt.executeUpdate(sql);
            }
            finally {
                close(c, stmt);
            }
        } catch (SQLException e) {
            if (("Table/View '"+table+"' does not exist.").equals(e.getMessage())) {
                try {
                    createTable(table);
                } catch (SQLException e1) {
                    throw new RuntimeException(e1);
                }
                remove(table, externalId);
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    private void clear(String table) {
        try {
            Connection c = null;
            Statement stmt = null;
            try
            {
                c = getConnection();
                stmt = getConnection().createStatement();
                String sql = "delete from "+table;
                log.debug(sql);
                stmt.executeUpdate(sql);
            }
            finally {
                close(c, stmt);
            }
        } catch (SQLException e) {
            if (("Table/View '"+table+"' does not exist.").equals(e.getMessage())) {
                try {
                    createTable(table);
                } catch (SQLException e1) {
                    throw new RuntimeException(e1);
                }
                clear(table);
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    private void createTable(String tableName) throws SQLException {
        String sql = "create table "+tableName+" (" +
                "ID INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                "XID VARCHAR(50), " +
                "JSON VARCHAR(5000), " +
                "CONSTRAINT primary_key_"+tableName+" PRIMARY KEY(ID)" +
                ")";
        Connection c = null;
        Statement stmt = null;
        try
        {
            c = getConnection();
            stmt = getConnection().createStatement();
            stmt.executeUpdate(sql);
        }
        finally {
            close(c, stmt);
        }
    }

    private void close(Connection c, Statement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (Exception e) {
            // don't care at this point
        }
        try {
            if (c != null) {
                c.close();
            }
        } catch (Exception e) {
            // don't care at this point
        }
    }

    private void close(Connection c, Statement stmt, ResultSet rs) {
        this.close(c, stmt);
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (Exception e) {
            // don't care at this point
        }
    }

    private Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(dbURL1);
        }
        catch(SQLException ex) {
            if (ex.getMessage().startsWith("Database ") && ex.getMessage().endsWith(" not found.")) {
                return DriverManager.getConnection(dbURL2);
            } else {
                throw ex;
            }
        }
    }

    public VideoAdDto lookupVideoAds() {
        VideoAdDto dto = new VideoAdDto();
        dto.setVideoAds(lookupAllVideoAds());
        return dto;
    }

    private List<VideoAd> lookupAllVideoAds() {
        List<VideoAd> list = new ArrayList<>();
        try {
            List<String> working = lookupQuerry(TABLE_VIDEO_ADS);
            Gson gson = builder.create();
            for (String json : working) {
                list.add(gson.fromJson(json, VideoAd.class));
            }
            return list;
        } catch (SQLException e) {
            if (("Table/View '"+TABLE_VIDEO_ADS+"' does not exist.").equals(e.getMessage())) {
                try {
                    createTable(TABLE_VIDEO_ADS);
                } catch (SQLException e1) {
                    throw new RuntimeException(e1);
                }
                return lookupAllVideoAds();
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    public void insert(VideoAd videoAd) {
        Gson gson = builder.create();
        insert(TABLE_VIDEO_ADS, videoAd.getId(), gson.toJson(videoAd));
    }

    public void update(VideoAd videoAd) {
        Gson gson = builder.create();
        int count = update(TABLE_VIDEO_ADS, videoAd.getId(), gson.toJson(videoAd));
        if (count == 0) {
            insert(videoAd);
        }
    }

    public void remove(VideoAd videoAd) {
        remove(TABLE_VIDEO_ADS, videoAd.getId());
    }

    public void clearVideoAds() {
        clear(TABLE_VIDEO_ADS);
    }

    public BannerAdDto lookupBannerAds() {
        BannerAdDto dto = new BannerAdDto();
        dto.setBannerAds(lookupAllBannerAds());
        return dto;
    }

    private List<BannerAd> lookupAllBannerAds() {
        List<BannerAd> list = new ArrayList<>();
        try {
            List<String> working = lookupQuerry(TABLE_BANNER_ADS);
            Gson gson = builder.create();
            for (String json : working) {
                list.add(gson.fromJson(json, BannerAd.class));
            }
            return list;
        } catch (SQLException e) {
            if (("Table/View '"+TABLE_BANNER_ADS+"' does not exist.").equals(e.getMessage())) {
                try {
                    createTable(TABLE_BANNER_ADS);
                } catch (SQLException e1) {
                    throw new RuntimeException(e1);
                }
                return lookupAllBannerAds();
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    public void insert(BannerAd bannerAd) {
        Gson gson = builder.create();
        insert(TABLE_BANNER_ADS, bannerAd.getId(), gson.toJson(bannerAd));
    }

    public void update(BannerAd bannerAd) {
        Gson gson = builder.create();
        int count = update(TABLE_BANNER_ADS, bannerAd.getId(), gson.toJson(bannerAd));
        if (count == 0) {
            insert(bannerAd);
        }
    }

    public void remove(BannerAd bannerAd) {
        remove(TABLE_BANNER_ADS, bannerAd.getId());
    }

    public void clearBannerAds() {
        clear(TABLE_BANNER_ADS);
    }

}
