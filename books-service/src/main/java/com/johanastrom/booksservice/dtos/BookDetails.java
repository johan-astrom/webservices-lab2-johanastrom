package com.johanastrom.booksservice.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BookDetails(@JsonProperty("title") String title,
                          @JsonProperty("language") String language,
                          @JsonProperty("price") float price,
                          @JsonProperty("genre") String genre) {
}
