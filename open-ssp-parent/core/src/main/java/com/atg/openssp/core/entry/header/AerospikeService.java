package com.atg.openssp.core.entry.header;

import com.aerospike.client.*;
import com.aerospike.client.policy.ClientPolicy;
import com.aerospike.client.policy.WritePolicy;
import com.google.gson.Gson;
import io.freestar.ssp.aerospike.data.CookieSyncDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class AerospikeService {

    private AerospikeClient client;
    private String namespace;
    private Integer expiration;

    public AerospikeService(String host,
                            int port,
                            String user,
                            String password,
                            String namespace,
                            int expiration) {
        final ClientPolicy clientPolicy = new ClientPolicy();
        clientPolicy.user = user;
        clientPolicy.password = password;

        this.client = new AerospikeClient(clientPolicy, host, port);
        this.namespace = checkNotNull(namespace);
        this.expiration = checkNotNull(expiration);
    }

    /**
     * Retrieve a value from Aerospike. If the record is not found returns null.
     *
     * @param set the Aerospike set
     * @param key the key for the record
     * @param bin the bin that the value is found in
     * @return String value
     * @throws AerospikeException if there is an error reading from Aerospike
     */
    public CookieSyncDTO get(String set, String key, String bin) {
        final Key asKey = new Key(this.namespace, set, key);
        final Record record = client.get(null, asKey);
        Gson gson = new Gson();
        return (record != null) ? gson.fromJson((String)record.getValue((bin != null) ? bin : "bytes"), CookieSyncDTO.class) : null;
    }

    public void set(String set, String key, String bin, CookieSyncDTO dto) {
        WritePolicy policy = new WritePolicy();
        policy.expiration = this.expiration;

        final Key asKey = new Key(this.namespace, set, key);
        Gson gson = new Gson();
        final Bin asBin = new Bin(bin, gson.toJson(dto, CookieSyncDTO.class));
        client.put(policy, asKey, asBin);
    }

}
