package com.marvel.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MarvelApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarvelApiApplication.class, args);
	}

}
