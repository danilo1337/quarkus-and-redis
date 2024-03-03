package com.github.danilo1337;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

@Path("/hello")
public class GreetingResource {

    @ConfigProperty(name = "app.redis.ip")
    String ip;

    @ConfigProperty(name = "app.redis.port")
    Integer port;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        JedisPool pool = new JedisPool(ip, port);
        String response = "";
        try (Jedis jedis = pool.getResource()) {
            // Store & Retrieve a simple string
            jedis.set("foo", "bar");
            System.out.println(jedis.get("foo")); // prints bar

            // Store & Retrieve a HashMap
            Map<String, String> hash = new HashMap<>();
            ;
            hash.put("name", "John");
            hash.put("surname", "Smith");
            hash.put("company", "Redis");
            hash.put("age", "29");
            hash.put("ip", ip);
            hash.put("port", port+"");


            jedis.hset("user-session:123", hash);

            response = jedis.hgetAll("user-session:123").toString();

        }

        return response;
    }

}
