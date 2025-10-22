package com.scad.scadenzario;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class ScadenzarioApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScadenzarioApplication.class, args);
	}
}
