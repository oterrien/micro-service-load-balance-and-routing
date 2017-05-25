package com.ote.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * client tries to loadbalance the service (it does not use the gateway for that)
 */
@RestController
@RequestMapping("/loadBalance")
@Slf4j
public class LoadBalancerRestController {

    @Autowired
    private LoadBalancerClient loadBalancer;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/service/home")
    @Retryable
    public String homeRemote() {

        log.info("Choose a service (load balancer) then call its endpoint");

        String serviceUri = loadBalancer.choose("service").getUri().toString();

        return this.restTemplate.getForObject(serviceUri + "/home", String.class);
    }
}
