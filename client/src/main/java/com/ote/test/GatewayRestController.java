package com.ote.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/gateway")
@Slf4j
public class GatewayRestController {

    @Autowired
    private DiscoveryClient client;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/service/home")
    public String homeRemote() {

        log.info("Discover Gateway then call service's endpoint");

        String gatewayUri = client.getInstances("gateway").stream().
                findFirst().
                orElseThrow(() -> new RuntimeException("No gateway")).
                getUri().toString();

        return this.restTemplate.getForObject(gatewayUri + "/service/home", String.class);
    }



}
