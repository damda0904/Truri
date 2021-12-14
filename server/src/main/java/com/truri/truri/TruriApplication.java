package com.truri.truri;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TruriApplication {

    public static void main(String[] args) {
        SpringApplication.run(TruriApplication.class, args);
    }

}
