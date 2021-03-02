package com.johanastrom.authors.mappers;

import com.johanastrom.authors.entities.Author;
import com.johanastrom.authors.dtos.AuthorRecord;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AuthorToDto {

    public AuthorRecord map(Author author){
        return new AuthorRecord(author.getId(), author.getFirstName(), author.getLastName(), author.getBirthDate());
    }

    public Author map(AuthorRecord authorRecord){
        return new Author(authorRecord.id(), authorRecord.firstName(), authorRecord.lastName(), authorRecord.birthDate());
    }

    public Optional<AuthorRecord> map(Optional<Author> author){
        return author.map(this::map);
    }

    public List<AuthorRecord> map(List<Author> authors){
        return authors
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }


}
