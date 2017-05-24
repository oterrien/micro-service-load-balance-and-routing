package com.ote.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class HomeRestController {

    @Value("${spring.cloud.consul.discovery.instanceId}")
    public String instanceId;

    @RequestMapping("/home")
    public String home() {

        log.info("####-" + instanceId);
        return "Hello from " + instanceId;
    }
}