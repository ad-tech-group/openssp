package io.freestar.openssp.common.exchange.aerospike;

import com.aerospike.client.*;
import com.aerospike.client.policy.ClientPolicy;
import com.aerospike.client.policy.WritePolicy;
import com.google.gson.Gson;
import io.freestar.openssp.common.exchange.aerospike.data.CookieSyncDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Properties;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class AerospikeService {
    private final static Logger log = LoggerFactory.getLogger(AerospikeService.class);
    private static AerospikeService singleton;
    @Value("${aerospike.host:172.28.128.33}")
    String host;

    private final AerospikeInfo info;
    private AerospikeClient client;

    private AerospikeService() {
        ApplicationContext appContext = new ClassPathXmlApplicationContext(
                "classpath:/META-INF/config.xml");
        info = appContext.getBean(AerospikeInfo.class);

        System.out.println("BKS.first="+host);
        System.out.println("BKS.ssecond="+info.host);

        /*
            host=properties.getProperty("host");
            port = Integer.parseInt(properties.getProperty("port"));
            user=properties.getProperty("user");
            password=properties.getProperty("password");
            namespace=properties.getProperty("namespace");
            expiration=Integer.parseInt(properties.getProperty("expiration"));
            */

            final ClientPolicy clientPolicy = new ClientPolicy();
            if (info.user != null) {
                clientPolicy.user = info.user;
            }
            if (info.password != null) {
                clientPolicy.password = info.password;
            }

            this.client = new AerospikeClient(clientPolicy, info.host, info.port);
            /*
            this.host = checkNotNull(host);
            this.port = checkNotNull(port);
            this.client = new AerospikeClient(clientPolicy, host, Integer.parseInt(port));
            this.namespace = checkNotNull(namespace);
            this.set = checkNotNull(set);
            this.bin = checkNotNull(bin);
            */

    }

    /**
     * Retrieve a value from Aerospike. If the record is not found returns null.
     *
     * @param key the key for the record
     * @return String value
     * @throws AerospikeException if there is an error reading from Aerospike
     */
    public CookieSyncDTO get(String key) {
        final Key asKey = new Key(info.namespace, info.set, key);
        final Record record = client.get(null, asKey);
        Gson gson = new Gson();
        return (record != null) ? gson.fromJson((String)record.getValue((info.bin != null) ? info.bin : "json"), CookieSyncDTO.class) : null;
    }

    public void set(String key, CookieSyncDTO dto) {
        WritePolicy policy = new WritePolicy();
        policy.expiration = info.expiration;

        final Key asKey = new Key(info.namespace, info.set, key);
        Gson gson = new Gson();
        final Bin asBin = new Bin(info.bin, gson.toJson(dto, CookieSyncDTO.class));
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
