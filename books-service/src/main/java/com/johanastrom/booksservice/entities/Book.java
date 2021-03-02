package com.johanastrom.booksservice.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Books")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Book {

    @Id
    private Long isbn13;

    private String title;
    private String language;
    private float price;
    private String genre;
}
