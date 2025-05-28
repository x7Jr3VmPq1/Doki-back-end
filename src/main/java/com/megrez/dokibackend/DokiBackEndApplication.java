package com.megrez.dokibackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DokiBackEndApplication {
    public static void main(String[] args) {
        SpringApplication.run(DokiBackEndApplication.class, args);
    }

}
