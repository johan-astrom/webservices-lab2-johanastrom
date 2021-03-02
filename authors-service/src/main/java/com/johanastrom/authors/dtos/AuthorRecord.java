package com.johanastrom.authors.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.util.Objects;


public record AuthorRecord(@JsonProperty("id") int id,
                           @JsonProperty("firstName") String firstName,
                           @JsonProperty("lastName") String lastName,
                           @JsonProperty("birthDate") Timestamp birthDate){

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != getClass()) return false;
        AuthorRecord author = (AuthorRecord) obj;
        return Objects.equals(author.firstName, this.firstName) &&
                Objects.equals(author.lastName, this.lastName) &&
                Objects.equals(author.birthDate, (this.birthDate)) ||
                author.id() == this.id;
    }

}
