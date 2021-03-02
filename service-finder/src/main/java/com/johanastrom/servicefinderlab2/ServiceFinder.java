package com.johanastrom.servicefinderlab2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@EnableRetry
@RestController
public class ServiceFinder {

    @Autowired
    private RestTemplate restTemplate;

    @Retryable(value = RuntimeException.class, maxAttempts = 4, backoff = @Backoff(value = 100))
    @GetMapping("/find-authors")
    public String findAuthorServices(){
        return this.restTemplate.getForObject("http://authors-service/authors", String.class);
    }

    @GetMapping("/find-books")
    public String findBooksServices(){
        return this.restTemplate.getForObject("http://books-service/books", String.class);
    }


    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        return builder.setConnectTimeout(Duration.ofSeconds(3)).setReadTimeout(Duration.ofSeconds(10)).build();
    }

}
