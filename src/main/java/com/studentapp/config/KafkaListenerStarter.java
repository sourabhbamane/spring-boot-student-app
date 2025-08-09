package com.studentapp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaListenerStarter implements ApplicationRunner {

    private final KafkaListenerEndpointRegistry registry;

    @Override
    public void run(ApplicationArguments args) {
        registry.getListenerContainers().forEach(container -> {
            container.start();
        });
    }
}