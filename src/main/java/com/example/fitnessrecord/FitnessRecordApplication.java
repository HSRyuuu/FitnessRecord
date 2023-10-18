package com.example.fitnessrecord;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class FitnessRecordApplication {

    public static void main(String[] args) {
        SpringApplication.run(FitnessRecordApplication.class, args);
    }

}
