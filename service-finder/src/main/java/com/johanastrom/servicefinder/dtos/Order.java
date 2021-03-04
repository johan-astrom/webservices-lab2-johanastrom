
package com.johanastrom.servicefinder.dtos;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

import java.sql.Timestamp;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Order {

    @Expose
    @JsonProperty("isbn13")
    private Long isbn13;
    @Expose
    @JsonProperty("orderDate")
    private Timestamp orderDate;
    @Expose
    @JsonProperty("shippingDate")
    private Timestamp shippingDate;

    public Long getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(Long isbn13) {
        this.isbn13 = isbn13;
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
