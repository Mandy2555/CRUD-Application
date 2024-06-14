package com.myapp.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ComponentScan("com.myapp")
@EnableMongoRepositories(basePackages = "com.myapp.repository") 
/**
 * 
 */
public class MyCrudApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(MyCrudApplication.class, args);
	}
	


}
