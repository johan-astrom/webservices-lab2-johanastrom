package com.johanastrom.servicefinderlab2.dtos;

import java.sql.Timestamp;

public class BookOrdered {

    private Long isbn13;
    private String title;
    private double price;
    private Timestamp orderDate;
    private Timestamp shippingDate;

    public BookOrdered(Long isbn13, String title, double price, Timestamp orderDate, Timestamp shippingDate) {
        this.isbn13 = isbn13;
        this.title = title;
        this.price = price;
        this.orderDate = orderDate;
        this.shippingDate = shippingDate;
    }

    public Long getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(Long isbn13) {
        this.isbn13 = isbn13;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public Timestamp getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(Timestamp shippingDate) {
        this.shippingDate = shippingDate;
    }
}
