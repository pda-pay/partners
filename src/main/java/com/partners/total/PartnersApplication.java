package com.partners.total;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PartnersApplication {

    public static void main(String[] args) {
        SpringApplication.run(PartnersApplication.class, args);
    }

}
