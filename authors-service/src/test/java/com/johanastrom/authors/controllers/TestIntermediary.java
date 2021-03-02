package com.johanastrom.authors.controllers;

import com.johanastrom.authors.dtos.AuthorPersonalData;
import com.johanastrom.authors.dtos.AuthorRecord;
import com.johanastrom.authors.services.IntermediaryService;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public class TestIntermediary implements IntermediaryService {
    @Override
    public List<AuthorRecord> getAllAuthors() {
        return List.of(new AuthorRecord(1, "Record1", "Record1", new Timestamp(1000)),
                new AuthorRecord(2, "Record2", "Record2", new Timestamp(1000)));
    }

    @Override
    public Optional<AuthorRecord> getOneAuthor(int id) {
        return Optional.empty();
    }

    @Override
    public List<AuthorRecord> getByLastName(String lastName) {
        return null;
    }

    @Override
    public List<AuthorRecord> getByFirstName(String firstName) {
        return null;
    }

    @Override
    public Optional<AuthorRecord> createAuthor(AuthorRecord authorRecord) {
        if (authorRecord.id()==1){
            return Optional.of(new AuthorRecord(1, "persisted", "persisted", new Timestamp(1000)));
        }
        return Optional.empty();
    }

    @Override
    public Optional<AuthorRecord> replace(int id, AuthorRecord authorRecord) {
        return Optional.empty();
    }

    @Override
    public Optional<AuthorRecord> update(int id, AuthorPersonalData authorPersonalData) {
        return Optional.empty();
    }

    @Override
    public void delete(int id) {

    }
}
