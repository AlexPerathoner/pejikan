package com.alexpera.pejikanbackend.config;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.alexpera.pejikanbackend.model")
public class PersistenceConfig {
}
