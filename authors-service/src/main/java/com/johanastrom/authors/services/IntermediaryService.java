package com.johanastrom.authors.services;

import com.johanastrom.authors.dtos.AuthorPersonalData;
import com.johanastrom.authors.dtos.AuthorRecord;

import java.util.List;
import java.util.Optional;

public interface IntermediaryService {
    List<AuthorRecord> getAllAuthors();

    Optional<AuthorRecord> getOneAuthor(int id);

    List<AuthorRecord> getByLastName(String lastName);

    List<AuthorRecord> getByFirstName(String firstName);

    Optional<AuthorRecord> createAuthor(AuthorRecord authorRecord);

    Optional<AuthorRecord> replace(int id, AuthorRecord authorRecord);

    Optional<AuthorRecord> update(int id, AuthorPersonalData authorPersonalData);

    void delete(int id);

}
