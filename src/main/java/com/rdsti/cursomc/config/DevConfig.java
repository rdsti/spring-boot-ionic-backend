package com.rdsti.cursomc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.rdsti.cursomc.services.DBConfigService;

@Configuration
@Profile("dev")
public class DevConfig {

	@Autowired
	private DBConfigService dbConfigService;
	
	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String strategy;
	
	@Bean
	public boolean instantiateTesteDataBase() throws Exception {
		
		if (!"create".equals(strategy)) {
			return false;
		}
		
		dbConfigService.instantiateTesteDataBase();
		
		return true;
	}
		
}
