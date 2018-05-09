package io.freestar.service;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.AerospikeException;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.policy.ClientPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

public class AerospikeService {
    private static final Logger log = LoggerFactory.getLogger(AerospikeService.class);

    private static AerospikeService instance;

    private final AerospikeClient client;
    private final String namespace;

    public AerospikeService(String host,
                            Integer port,
                            String user,
                            String password,
                            String namespace) {
        final ClientPolicy clientPolicy = new ClientPolicy();
        clientPolicy.user = user;
        clientPolicy.password = password;

        this.client = new AerospikeClient(clientPolicy, host, port);
        this.namespace = checkNotNull(namespace);
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

        if (record != null) {
            return record.bins.get(bin);
        }

        return null;
    }

    public static synchronized AerospikeService getInstance() {
        if (instance == null) {
            instance = new AerospikeService(
                    System.getenv("AEROSPIKE_HOST"),
                    3000,
                    System.getenv("AEROSPIKE_USER"),
                    System.getenv("AEROSPIKE_PASSWORD"),
                    System.getenv("AEROSPIKE_NAMESPACE_SSP"));
        }

        return instance;
    }

}
