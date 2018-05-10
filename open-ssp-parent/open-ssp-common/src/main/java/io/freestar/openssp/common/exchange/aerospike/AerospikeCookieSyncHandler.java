package io.freestar.openssp.common.exchange.aerospike;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.policy.ClientPolicy;
import com.aerospike.client.policy.WritePolicy;
import com.atg.openssp.common.core.exchange.cookiesync.CookieSyncDTO;
import com.atg.openssp.common.core.exchange.cookiesync.CookieSyncHandler;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;

/**
 * This base class handles the interaction with Aerospikel
 */
public abstract class AerospikeCookieSyncHandler implements CookieSyncHandler {
    private final static Logger LOG = LoggerFactory.getLogger(AerospikeCookieSyncHandler.class);
    public static final String SET_NAME = "cookie_sync";

    private AerospikeClient client;

    protected AerospikeCookieSyncHandler() {
        final ClientPolicy clientPolicy = new ClientPolicy();
        String user = getUser();
        String pw = getPassword();
        if (user != null) {
            clientPolicy.user = user;
        }
        if (pw != null) {
            clientPolicy.password = pw;
        }
        this.client = new AerospikeClient(clientPolicy, getHost(), 3000);
    }

    /**
     * get the Aerospike user id
     * @return
     */
    protected abstract String getUser();

    /**
     * get the Aerospike password credential
     * @return
     */
    protected abstract String getPassword();

    /**
     * get the Aerospike host name
     * @return
     */
    protected abstract String getHost();

    @Override
    public final CookieSyncDTO get(String key) {
        try {
            final Key asKey = new Key(getNamespace(), getSetName(), key);
            final Record record = client.get(null, asKey);
            Gson gson = new Gson();
            return (record != null) ? gson.fromJson((String)record.getValue("json"), (Type) CookieSyncDTO.class) : null;
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            return null;
        }
    }

    @Override
    public final void set(String key, CookieSyncDTO dto) {
        try {
            WritePolicy policy = new WritePolicy();

            final Key asKey = new Key(getNamespace(), getSetName(), key);
            Gson gson = new Gson();
            final Bin asBin = new Bin("json", gson.toJson(dto, CookieSyncDTO.class));
            client.put(policy, asKey, asBin);
        } catch(Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    /**
     * return the Aerospike namespace
     * @return
     */
    protected abstract String getNamespace();

    /**
     * return the Aerospike set name
     * @return
     */
    protected abstract String getSetName();

    @Override
    public final boolean supportsCookieSync() {
        return true;
    }

}
