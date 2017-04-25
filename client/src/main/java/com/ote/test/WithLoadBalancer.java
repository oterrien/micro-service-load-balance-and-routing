package com.ote.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class WithLoadBalancer {

    @Autowired
    private LoadBalancerClient loadBalancer;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/loadBalance/hello")
    public String withLoadBalance() {

        String serverUri = loadBalancer.choose("hello").getUri().toString();

        return this.restTemplate.getForObject(serverUri + "/home", String.class);
    }
}
