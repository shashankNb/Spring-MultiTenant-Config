package com.ea.crm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@SpringBootApplication()
public class EaCrmServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EaCrmServiceApplication.class, args);
	}

}
