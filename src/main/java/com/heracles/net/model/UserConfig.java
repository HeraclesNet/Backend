package com.heracles.net.model;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import com.heracles.net.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;

@Configuration
public class UserConfig {

    @Bean
    CommandLineRunner commandLineRunner(
            UserRepository repository) {
        return args -> {
            User nel = new User(
                    "nombre",
                    LocalDate.of(2000, Month.APRIL, 20),
                    "algo@algo",
                    "tim",
                    "noes1",
                    (float) 23.2,
                    (float) 12.3,
                    false);

            User pepito = new User(
                    "nombre2",
                    LocalDate.of(2000, Month.APRIL, 20),
                    "algo@algo2",
                    "tim2",
                    "noes2",
                    (float) 23.2,
                    (float) 12.3,
                    false);
            repository.saveAll(
                    List.of(nel, pepito));
        };
    }
}
