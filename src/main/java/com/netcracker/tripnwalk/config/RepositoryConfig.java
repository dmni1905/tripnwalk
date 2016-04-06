package com.netcracker.tripnwalk.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.netcracker.tripnwalk.repository")
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.netcracker.tripnwalk.entry"})
public class RepositoryConfig {
}
