package io.freestar.openssp.common.exchange.aerospike;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AerospikeInfo {
    @Value("${aerospike.host:172.28.128.3}")
    String host;
    @Value("${aerospike.port:3000}")
    int port;
    @Value("${aerospike.user}")
    String user;
    @Value("${aerospike.password}")
    String password;
    @Value("${aerospike.namespace:ssp}")
    String namespace;
    @Value("${aerospike.expiration:0}")
    int expiration;
    @Value("${aerospike.set:dev-gen}")
    String set;
    @Value("${aerospike.bin:json}")
    String bin;

}
