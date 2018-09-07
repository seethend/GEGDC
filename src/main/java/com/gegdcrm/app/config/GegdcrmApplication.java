package com.gegdcrm.app.config;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = { "com.gegdcrm.app.config", "com.gegdcrm.app.controller", "com.gegdcrm.app.model","com.gegdcrm.app.model",
		"com.gegdcrm.app.repo","com.gegdcrm.app.repo", "com.gegdcrm.app.services","com.gegdcrm.app.services", "com.gegdcrm.app.util","com.gegdcrm.app.util",
		"com.gegdcrm.app.validator","com.gegdcrm.app.controller" })
@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = { "com.gegdcrm.app.repo","com.gegdcrm.app.repo" })
@EntityScan(basePackages = { "com.gegdcrm.app.model","com.gegdcrm.app.model" })
@EnableCaching
public class GegdcrmApplication extends SpringBootServletInitializer {

	private static Logger CONFIG_LOGGEER = Logger.getLogger(GegdcrmApplication.class);

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		CONFIG_LOGGEER.info("Spring application is configuring");
		return builder.sources(GegdcrmApplication.class);
	}

	public static void main(String[] args) {
		CONFIG_LOGGEER.info("Spring application is runnng");
		SpringApplication.run(GegdcrmApplication.class, args);
	}

	@Bean
	public CacheManager cacheManager() {
		SimpleCacheManager cacheManager = new SimpleCacheManager();
		cacheManager.setCaches(Arrays.asList(new ConcurrentMapCache("expmgrwebcache")));
		return cacheManager;
	}
}
