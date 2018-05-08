package io.freestar.openssp.common.exchange.aerospike;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookieSyncInfo {
    @Value("${aerospike.host}")
    String host;
    @Value("${aerospike.user}")
    String user;
    @Value("${aerospike.password}")
    String password;
    @Value("${aerospike.namespace}")
    String namespace;
    @Value("${aerospike.expiration:0}")
    int expiration;
}