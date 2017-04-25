package com.ote.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class WithDiscoverService {

    @Autowired
    private DiscoveryClient client;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/discovery/hello")
    public String withDiscovery() {

        // This is not loadbalanced
        String serverUri = client.getInstances("hello").stream().
                findAny().
                orElseThrow(() -> new RuntimeException("No service")).
                getUri().toString();

        return this.restTemplate.getForObject(serverUri + "/home", String.class);
    }

}
