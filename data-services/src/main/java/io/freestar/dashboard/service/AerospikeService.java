package io.freestar.dashboard.service;

import com.aerospike.client.*;
import com.aerospike.client.policy.ClientPolicy;
import com.aerospike.client.policy.WritePolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class AerospikeService {

    private AerospikeClient client;
    private String namespace;
    private Integer expiration;

    @Autowired
    public AerospikeService(@Value("${aerospike.host}") String host,
                            @Value("${aerospike.port}") Integer port,
                            @Value("${aerospike.user}") String user,
                            @Value("${aerospike.password}") String password,
                            @Value("${aerospike.namespace}") String namespace,
                            @Value("${aerospike.expiration}") Integer expiration) {
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
    public Object get(String set, String key, String bin) {
        final Key asKey = new Key(this.namespace, set, key);
        final Record record = client.get(null, asKey);

        return (record != null) ? record.getValue((bin != null) ? bin : "bytes") : null;
    }

    public void set(String set, String key, String bin, byte[] value) {
        WritePolicy policy = new WritePolicy();
        policy.expiration = this.expiration;

        final Key asKey = new Key(this.namespace, set, key);
        final Bin asBin = new Bin(bin, value);
        client.put(policy, asKey, asBin);
    }

}
