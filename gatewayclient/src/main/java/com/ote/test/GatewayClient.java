package com.ote.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class GatewayClient {

    @Autowired
    private DiscoveryClient client;

    @Autowired
    private RestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @RequestMapping("/hello")
    public String withDiscovery() {

        System.out.println("here");

        String serverUri = client.getInstances("gateway").stream().
                findFirst().
                orElseThrow(() -> new RuntimeException("No service")).
                getUri().toString();

        return this.restTemplate.getForObject(serverUri + "/hello-service/home", String.class);
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(GatewayClient.class).web(true).run(args);
    }

}