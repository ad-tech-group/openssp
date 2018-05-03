package io.freestar.openssp.common.exchange.aerospike;

import com.aerospike.client.*;
import com.aerospike.client.policy.ClientPolicy;
import com.aerospike.client.policy.WritePolicy;
import com.google.gson.Gson;
import io.freestar.openssp.common.exchange.aerospike.data.CookieSyncDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class AerospikeService {
    private final static Logger log = LoggerFactory.getLogger(AerospikeService.class);
    private static AerospikeService singleton;
    private final Properties properties = new Properties();
    private AerospikeClient client;
    private String namespace;
    private Integer expiration;
    private String set;
    private String bin;

    private AerospikeService() {
        InputStream is = getClass().getClassLoader().getResourceAsStream(resolveEnvironment()+"aerospike.properties");
        if (is != null) {
            try {
                properties.load(is);
                is.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
            String host=properties.getProperty("host");
            int port = Integer.parseInt(properties.getProperty("port"));
            String user=properties.getProperty("user");
            String pw=properties.getProperty("password");
            String namespace=properties.getProperty("namespace");
            int expiration=Integer.parseInt(properties.getProperty("expiration"));

            final ClientPolicy clientPolicy = new ClientPolicy();
            clientPolicy.user = user;
            clientPolicy.password = pw;

            this.client = new AerospikeClient(clientPolicy, host, port);
            this.namespace = checkNotNull(namespace);
            this.expiration = checkNotNull(expiration);
            set = properties.getProperty("set");
            bin = properties.getProperty("bin");
        }

    }

    /**
     * Retrieve a value from Aerospike. If the record is not found returns null.
     *
     * @param key the key for the record
     * @return String value
     * @throws AerospikeException if there is an error reading from Aerospike
     */
    public CookieSyncDTO get(String key) {
        final Key asKey = new Key(this.namespace, set, key);
        final Record record = client.get(null, asKey);
        Gson gson = new Gson();
        return (record != null) ? gson.fromJson((String)record.getValue((bin != null) ? bin : "json"), CookieSyncDTO.class) : null;
    }

    public void set(String key, CookieSyncDTO dto) {
        WritePolicy policy = new WritePolicy();
        policy.expiration = this.expiration;

        final Key asKey = new Key(this.namespace, set, key);
        Gson gson = new Gson();
        final Bin asBin = new Bin(bin, gson.toJson(dto, CookieSyncDTO.class));
        client.put(policy, asKey, asBin);
    }

    private String resolveEnvironment() {
        String environment = System.getProperty("SSP_ENVIRONMENT");
        log.info("Environment: "+environment);
        if (environment != null) {
            return environment+"_";
        } else {
            return "";
        }
    }

    public synchronized static AerospikeService getInstance() {
        if (singleton == null) {
            singleton = new AerospikeService();
        }
        return singleton;
    }
}
