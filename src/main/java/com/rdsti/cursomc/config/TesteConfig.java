package com.rdsti.cursomc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.rdsti.cursomc.services.DBConfigService;
import com.rdsti.cursomc.services.EmailService;
import com.rdsti.cursomc.services.MockEmailService;

@Configuration
@Profile("test")
public class TesteConfig {

	@Autowired
	private DBConfigService dbConfigService;
	
	@Bean
	public boolean instantiateTesteDataBase() throws Exception {
		
		dbConfigService.instantiateTesteDataBase();
		
		return true;
	}
	
	@Bean
	public EmailService emailService() {
		
		return new MockEmailService();
	}
		
}
