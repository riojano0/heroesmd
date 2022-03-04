package com.montivero.poc.heroesmd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@EnableCaching
@SpringBootApplication
public class HeroesMdApplication {

	public static void main(String[] args) {
		SpringApplication.run(HeroesMdApplication.class, args);
	}

}
