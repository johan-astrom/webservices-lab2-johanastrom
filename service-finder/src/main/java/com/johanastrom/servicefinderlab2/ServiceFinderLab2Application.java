package com.johanastrom.servicefinderlab2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
public class ServiceFinderLab2Application {

    public static void main(String[] args) {
        SpringApplication.run(ServiceFinderLab2Application.class, args);
    }

}
