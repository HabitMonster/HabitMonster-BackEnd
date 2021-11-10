package com.sollertia.habit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class HabitApplication {

    public static void main(String[] args) {
        SpringApplication.run(HabitApplication.class, args);
    }

}
