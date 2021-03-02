package com.johanastrom.booksservice.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BookObject(@JsonProperty(value = "isbn13")
                         Long isbn13,
                         @JsonProperty(value = "title")
                         String title,
                         @JsonProperty(value = "language")
                          String language,
                         @JsonProperty(value = "price")
                          float price,
                         @JsonProperty(value = "genre")
                          String genre) {
}
