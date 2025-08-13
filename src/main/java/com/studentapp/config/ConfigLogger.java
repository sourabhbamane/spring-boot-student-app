package com.studentapp.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

@Component
public class ConfigLogger {
    private static final Logger log = LoggerFactory.getLogger(ConfigLogger.class);

    @Autowired
    private Environment env;

    @PostConstruct
    public void logConfig() {
        log.info("Active Profiles: {}", Arrays.toString(env.getActiveProfiles()));
        log.info("Kafka Bootstrap Servers: {}", env.getProperty("spring.kafka.bootstrap-servers"));
    }
}