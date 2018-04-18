package com.atg.openssp.dataprovider.provider;

import com.atg.openssp.common.core.broker.dto.CurrencyDto;
import com.atg.openssp.common.core.broker.dto.PricelayerDto;
import com.atg.openssp.common.core.broker.dto.SiteDto;
import com.atg.openssp.common.core.broker.dto.SupplierDto;
import com.atg.openssp.common.demand.Supplier;
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
    private final static String dbURL = "jdbc:derby:OpenSSPDataServiceDB;create=true";
    private static final String TABLE_SITES = "SITES";
    private static final String TABLE_PRICELAYERS = "PRICELAYERS";
    private static final String TABLE_SUPPLIERS = "SUPPLIERS";
    private static final String TABLE_CURRENCY = "CURRENCY";
    private final GsonBuilder builder;
    private ArrayList<String> tablesCreated = new ArrayList<>();
    private static DataStore singleton;

    private DataStore() throws ClassNotFoundException {
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        builder = new GsonBuilder();
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
        String json = gson.toJson(currency);
        try {
            insert(TABLE_CURRENCY, currency.getCurrency(), json);
        } catch (SQLException e) {
            if (("Table/View '"+TABLE_CURRENCY+"' does not exist.").equals(e.getMessage())) {
                try {
                    createTable(TABLE_CURRENCY);
                } catch (SQLException e1) {
                    throw new RuntimeException(e1);
                }
                insert(currency);
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    public void update(CurrencyDto currency) {
        Gson gson = builder.create();
        String json = gson.toJson(currency);
        try {
            update(TABLE_CURRENCY, currency.getCurrency(), json);
        } catch (SQLException e) {
            if (("Table/View '"+TABLE_CURRENCY+"' does not exist.").equals(e.getMessage())) {
                try {
                    createTable(TABLE_CURRENCY);
                } catch (SQLException e1) {
                    throw new RuntimeException(e1);
                }
                update(currency);
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    public void remove(CurrencyDto currency) {
        try {
            remove(TABLE_CURRENCY, currency.getCurrency());
        } catch (SQLException e) {
            if (("Table/View '"+TABLE_CURRENCY+"' does not exist.").equals(e.getMessage())) {
                try {
                    createTable(TABLE_SUPPLIERS);
                } catch (SQLException e1) {
                    throw new RuntimeException(e1);
                }
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean wasCurrencyCreated() {
        return this.tablesCreated.contains(TABLE_CURRENCY);
    }

    public void clearCurrencyCreatedFlag() {
        tablesCreated.remove(TABLE_CURRENCY);
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
        String json = gson.toJson(site);
        try {
            insert(TABLE_SITES, site.getId(), json);
        } catch (SQLException e) {
            if (("Table/View '"+TABLE_SITES+"' does not exist.").equals(e.getMessage())) {
                try {
                    createTable(TABLE_SITES);
                } catch (SQLException e1) {
                    throw new RuntimeException(e1);
                }
                insert(site);
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    public void update(Site site) {
        Gson gson = builder.create();
        String json = gson.toJson(site);
        try {
            update(TABLE_SITES, site.getId(), json);
        } catch (SQLException e) {
            if (("Table/View '"+TABLE_SITES+"' does not exist.").equals(e.getMessage())) {
                try {
                    createTable(TABLE_SITES);
                } catch (SQLException e1) {
                    throw new RuntimeException(e1);
                }
                update(site);
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    public void remove(Site site) {
        try {
            remove(TABLE_SITES, site.getId());
        } catch (SQLException e) {
            if (("Table/View '"+TABLE_SITES+"' does not exist.").equals(e.getMessage())) {
                try {
                    createTable(TABLE_SITES);
                } catch (SQLException e1) {
                    throw new RuntimeException(e1);
                }
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean wasSitesCreated() {
        return this.tablesCreated.contains(TABLE_SITES);
    }

    public void clearSitesCreatedFlag() {
        tablesCreated.remove(TABLE_SITES);
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
        String json = gson.toJson(pricelayer);
        try {
            insert(TABLE_PRICELAYERS, pricelayer.getSiteid(), json);
        } catch (SQLException e) {
            if (("Table/View '"+TABLE_PRICELAYERS+"' does not exist.").equals(e.getMessage())) {
                try {
                    createTable(TABLE_PRICELAYERS);
                } catch (SQLException e1) {
                    throw new RuntimeException(e1);
                }
                insert(pricelayer);
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    public void update(Pricelayer pricelayer) {
        Gson gson = builder.create();
        String json = gson.toJson(pricelayer);
        try {
            update(TABLE_PRICELAYERS, pricelayer.getSiteid(), json);
        } catch (SQLException e) {
            if (("Table/View '"+TABLE_PRICELAYERS+"' does not exist.").equals(e.getMessage())) {
                try {
                    createTable(TABLE_PRICELAYERS);
                } catch (SQLException e1) {
                    throw new RuntimeException(e1);
                }
                update(pricelayer);
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    public void remove(Pricelayer pricelayer) {
        try {
            remove(TABLE_PRICELAYERS, pricelayer.getSiteid());
        } catch (SQLException e) {
            if (("Table/View '"+TABLE_PRICELAYERS+"' does not exist.").equals(e.getMessage())) {
                try {
                    createTable(TABLE_PRICELAYERS);
                } catch (SQLException e1) {
                    throw new RuntimeException(e1);
                }
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean wasPricelayersCreated() {
        return this.tablesCreated.contains(TABLE_PRICELAYERS);
    }

    public void clearPricelayersCreatedFlag() {
        tablesCreated.remove(TABLE_PRICELAYERS);
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
        String json = gson.toJson(supplier);
        try {
            insert(TABLE_SUPPLIERS, Long.toString(supplier.getSupplierId()), json);
        } catch (SQLException e) {
            if (("Table/View '"+TABLE_SUPPLIERS+"' does not exist.").equals(e.getMessage())) {
                try {
                    createTable(TABLE_SUPPLIERS);
                } catch (SQLException e1) {
                    throw new RuntimeException(e1);
                }
                insert(supplier);
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    public void update(Supplier supplier) {
        Gson gson = builder.create();
        String json = gson.toJson(supplier);
        try {
            update(TABLE_SUPPLIERS, Long.toString(supplier.getSupplierId()), json);
        } catch (SQLException e) {
            if (("Table/View '"+TABLE_SUPPLIERS+"' does not exist.").equals(e.getMessage())) {
                try {
                    createTable(TABLE_SUPPLIERS);
                } catch (SQLException e1) {
                    throw new RuntimeException(e1);
                }
                update(supplier);
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    public void remove(Supplier supplier) {
        try {
            remove(TABLE_SUPPLIERS, Long.toString(supplier.getSupplierId()));
        } catch (SQLException e) {
            if (("Table/View '"+TABLE_SUPPLIERS+"' does not exist.").equals(e.getMessage())) {
                try {
                    createTable(TABLE_SUPPLIERS);
                } catch (SQLException e1) {
                    throw new RuntimeException(e1);
                }
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean wasSuppliersCreated() {
        return this.tablesCreated.contains(TABLE_SUPPLIERS);
    }

    public void clearSuppliersCreatedFlag() {
        tablesCreated.remove(TABLE_SUPPLIERS);
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

    private void insert(String table, String externalId, String json) throws SQLException {
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
    }

    private void update(String table, String externalId, String json) throws SQLException {
        Connection c = null;
        Statement stmt = null;

        try
        {
            c = getConnection();
            stmt = getConnection().createStatement();
            int i = stmt.executeUpdate("update " + table + " set JSON='" + json + "' where XID='" + externalId + "'");
        }
        finally {
            close(c, stmt);
        }
    }

    private void remove(String table, String externalId) throws SQLException {
        Connection c = null;
        Statement stmt = null;
        try
        {
            c = getConnection();
            stmt = getConnection().createStatement();
            String sql = "delete from "+table+" where XID='"+externalId+"'";
            System.out.println(sql);
            stmt.executeUpdate(sql);
        } finally {
            close(c, stmt);
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
            tablesCreated.add(tableName);
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
        return DriverManager.getConnection(dbURL);
    }

}
