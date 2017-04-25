package com.ote.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class WithDiscoveryGateway {

    @Autowired
    private DiscoveryClient client;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/gateway/hello")
    public String withDiscovery() {

        System.out.println("here");

        String serverUri = client.getInstances("gateway").stream().
                findFirst().
                orElseThrow(() -> new RuntimeException("No gateway")).
                getUri().toString();

        return this.restTemplate.getForObject(serverUri + "/hello-service/home", String.class);
    }



}
