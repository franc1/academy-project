package com.logate.academy.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

// ova klasa sluzi da bismo 'key' property is application.yml mapirali u ovoj klasi,
// da bismo je onda koristili u aplikaciji
@Configuration
@ConfigurationProperties(prefix = "microservice")
public class MicroserviceConfiguration {
	
	private String key;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
}
