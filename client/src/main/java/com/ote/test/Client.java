package com.ote.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
@RibbonClient("hello")
public class Client {

    @Autowired
    private DiscoveryClient client;

    @Autowired
    private LoadBalancerClient loadBalancer;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestTemplate restTemplateLB;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplateLB() {
        return new RestTemplate();
    }

    @RequestMapping("/disco")
    public String withDiscovery() {

        String serverUri = client.getInstances("hello").stream().
                findFirst().
                orElseThrow(() -> new RuntimeException("No service")).
                getUri().toString();

        return this.restTemplate.getForObject(serverUri + "/home", String.class);
    }


    @RequestMapping("/loadBalance")
    public String withLoadBalance() {

        String serverUri = loadBalancer.choose("hello").getUri().toString();

        return this.restTemplate.getForObject(serverUri + "/home", String.class);
    }


    @RequestMapping("/loadBalanceRest")
    public String withLoadBalanceRest() {

        return this.restTemplateLB.getForObject("http://hello/home", String.class);
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(Client.class).web(true).run(args);
    }

}