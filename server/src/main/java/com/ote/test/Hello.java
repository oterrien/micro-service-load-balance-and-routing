package com.ote.test;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class Hello {

    @RequestMapping("/home")
    public String home() {

        System.out.println("here");
        return "Hello world";
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(Hello.class).web(true).run(args);
    }
}