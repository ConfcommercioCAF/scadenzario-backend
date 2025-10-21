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
	@Bean
    CommandLineRunner testConnessione(JdbcTemplate jdbcTemplate) {
        return args -> {
            System.out.println("🔄 Test connessione al database...");

            try {
                // Esegui una query di test
               // String result = jdbcTemplate.queryForObject("SELECT 'Connessione OK' AS test", String.class);
              //  System.out.println("✅ Connessione riuscita! Risultato: " + result);

                // Se vuoi, puoi anche provare una query reale
                jdbcTemplate.queryForList("SELECT * FROM scad_tipologie LIMIT 1")
                   .forEach(System.out::println);

            } catch (Exception e) {
                System.err.println("❌ Errore nella connessione al database:");
                e.printStackTrace();
            }
        };
    }
}
