package com.ote.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@RestController
@RequestMapping("/discovery")
@Slf4j
public class DiscoverServiceRestController {

    @Autowired
    private DiscoveryClient client;

    @Autowired
    private RestTemplate restTemplate;

    private Map<String, Metrics> metricsByService = new ConcurrentHashMap<>();

    @RequestMapping("/service/home")
    public String homeRemote() {

        log.info("Discover service then call its endpoint");

        log.info("Number of instances : " + client.getInstances("service").size());

        // Trying to loadbalance manually (by searching the service with less memory
        String serviceUri = client.getInstances("service").
                stream().
                map(p -> p.getUri().toString()).
                peek(this::computeMetrics).
                sorted(Comparator.comparing(p -> metricsByService.get(p).getMemFree())).
                findFirst().
                orElseThrow(() -> new RuntimeException("No service"));


        return this.restTemplate.getForObject(serviceUri + "/home", String.class);
    }

    private void computeMetrics(String serviceUri) {
        OptionalConsumer.of(Optional.ofNullable(metricsByService.get(serviceUri))).
                ifNotPresent(() -> {
                    Metrics metrics = restTemplate.getForObject(serviceUri + "/metrics", Metrics.class);
                    metricsByService.put(serviceUri, metrics);
                });
    }

    @Data
    public static class Metrics {

        private Long mem;

        @JsonProperty("mem.free")
        private Long memFree;
    }

    @RequiredArgsConstructor
    public static class OptionalConsumer<T> {

        private final Optional<T> optional;

        public static <T> OptionalConsumer<T> of(Optional<T> optional) {
            return new OptionalConsumer<>(optional);
        }

        public OptionalConsumer<T> ifPresent(Consumer<T> c) {
            optional.ifPresent(c);
            return this;
        }

        public OptionalConsumer<T> ifNotPresent(Runnable r) {
            if (!optional.isPresent())
                r.run();
            return this;
        }
    }

}
