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

public abstract class AerospikeCookieSyncHandler implements CookieSyncHandler {
    private final static Logger log = LoggerFactory.getLogger(AerospikeCookieSyncHandler.class);

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

    protected abstract String getUser();

    protected abstract String getPassword();

    protected abstract String getHost();

    @Override
    public final CookieSyncDTO get(String key) {
        final Key asKey = new Key(getNamespace(), getSetName(), key);
        final Record record = client.get(null, asKey);
        Gson gson = new Gson();
        return (record != null) ? gson.fromJson((String)record.getValue("json"), (Type) getGsonConversionClass()) : null;
    }

    @Override
    public final void set(String key, CookieSyncDTO dto) {
        WritePolicy policy = new WritePolicy();

        final Key asKey = new Key(getNamespace(), getSetName(), key);
        Gson gson = new Gson();
        final Bin asBin = new Bin("json", gson.toJson(dto, getGsonConversionClass()));
        client.put(policy, asKey, asBin);
    }

    protected abstract String getNamespace();

    protected abstract String getSetName();

    protected abstract Class getGsonConversionClass();

    @Override
    public final boolean supportsCookieSync() {
        return true;
    }

}
