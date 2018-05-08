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
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Properties;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class AerospikeService {
    private final static Logger log = LoggerFactory.getLogger(AerospikeService.class);
    private static AerospikeService singleton;
    @Value("${aerospike.host}")
    String host;

    private AerospikeClient client;
    //    private final AerospikeInfo info;

    private AerospikeService() {
        ApplicationContext appContext = new ClassPathXmlApplicationContext(
                "classpath:/META-INF/config.xml");
        //info = appContext.getBean(AerospikeInfo.class);

        System.out.println("BKS.first="+host);
        //System.out.println("BKS.second="+info.host);
        System.out.println("BKS.third="+System.getProperty("AEROSPIKE_HOST"));
        System.out.println("BKS.user1="+System.getProperty("AEROSPIKE_USER"));
        System.out.println("BKS.user2="+System.getenv("AEROSPIKE_USER"));

        /*
            host=properties.getProperty("host");
            port = Integer.parseInt(properties.getProperty("port"));
            user=properties.getProperty("user");
            password=properties.getProperty("password");
            namespace=properties.getProperty("namespace");
            expiration=Integer.parseInt(properties.getProperty("expiration"));
            */

            final ClientPolicy clientPolicy = new ClientPolicy();
            if (System.getenv("AEROSPIKE_USER") != null) {
                clientPolicy.user = System.getenv("AEROSPIKE_USER");
            }
            if (System.getenv("AEROSPIKE_PASSWORD") != null) {
                clientPolicy.password = System.getenv("AEROSPIKE_PASSWORD");
            }

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
        final Key asKey = new Key(System.getenv("AEROSPIKE_NAMESPACE_SSP"), "cookie_sync", key);
        final Record record = client.get(null, asKey);
        Gson gson = new Gson();
        return (record != null) ? gson.fromJson((String)record.getValue("json"), CookieSyncDTO.class) : null;
    }

    public void set(String key, CookieSyncDTO dto) {
        WritePolicy policy = new WritePolicy();

        final Key asKey = new Key(System.getenv("AEROSPIKE_NAMESPACE_SSP"), "cookie_sync", key);
        Gson gson = new Gson();
        final Bin asBin = new Bin("json", gson.toJson(dto, CookieSyncDTO.class));
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
