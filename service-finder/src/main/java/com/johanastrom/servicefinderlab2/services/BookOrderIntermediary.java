package com.johanastrom.servicefinderlab2.services;

import com.johanastrom.servicefinderlab2.Book;
import com.johanastrom.servicefinderlab2.BookOrdered;
import com.johanastrom.servicefinderlab2.Embedded;
import com.johanastrom.servicefinderlab2.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookOrderIntermediary {

    private RestTemplate restTemplate;

    @Autowired
    public BookOrderIntermediary(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<BookOrdered> findBooksOrdered() {
        Book[] books = this.restTemplate.getForObject("http://books-service/books", Book[].class);
        Embedded orders = this.restTemplate.getForObject("http://orders-service/orders", Embedded.class);
        List<BookOrdered> booksOrdered = new ArrayList<>();
        for (Order order : orders.get_embedded().getOrders()) {
            for (Book book : books) {
                if (book.getIsbn13().equals(order.getIsbn13())) {
                    booksOrdered.add(new BookOrdered(book.getIsbn13(), book.getTitle(), book.getPrice(),
                            order.getOrderDate(), order.getShippingDate()));
                }
            }
        }
        return booksOrdered;
    }

}
