package com.johanastrom.servicefinder;

import com.johanastrom.servicefinder.dtos.BookOrdered;
import com.johanastrom.servicefinder.services.BookOrderIntermediary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@EnableRetry
@RestController
@RequestMapping("/getservice")
public class ServiceFinder {

    private RestTemplate restTemplate;

    private RestTemplate restTemplateWithoutLoadBalancing;

    private BookOrderIntermediary service;

    @Autowired
    public ServiceFinder(RestTemplate restTemplate, RestTemplate restTemplateWithoutLoadBalancing, BookOrderIntermediary service) {
        this.restTemplateWithoutLoadBalancing = restTemplateWithoutLoadBalancing;
        this.service = service;
        this.restTemplate=restTemplate;
    }

    @Retryable(value = RuntimeException.class, maxAttempts = 4, backoff = @Backoff(value = 100))
    @GetMapping("/find-authors")
    public String findAuthorServices() {
        return this.restTemplate.getForObject("http://authors-service/authors", String.class);
    }

    @GetMapping("/find-books")
    public String findBooksServices() {
        return this.restTemplate.getForObject("http://books-service/books", String.class);
    }

    @GetMapping("/books-ordered")
    public List<BookOrdered> findBooksOrdered() {
        return this.service.findBooksOrdered();
    }

    @GetMapping("/the-weather-in-arvidsjaur")
    public String findWeather() {
        return this.restTemplateWithoutLoadBalancing.getForObject("https://opendata-download-metobs.smhi.se/api/version/1.0/parameter/1/station/159880.json", String.class);
    }

    @GetMapping("/cat-facts")
    public String findCatFacts(){
        return this.restTemplateWithoutLoadBalancing.getForObject("https://cat-fact.herokuapp.com/facts", String.class);
    }

}
