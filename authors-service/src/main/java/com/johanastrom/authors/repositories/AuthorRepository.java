package com.johanastrom.authors.repositories;

import com.johanastrom.authors.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Integer> {

    List<Author> findByLastName(String lastName);

    List<Author> findByFirstName(String firstName);
}


