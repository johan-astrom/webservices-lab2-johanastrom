package com.johanastrom.authors.services;

import com.johanastrom.authors.dtos.AuthorPersonalData;
import com.johanastrom.authors.dtos.AuthorRecord;
import com.johanastrom.authors.entities.Author;
import com.johanastrom.authors.mappers.AuthorToDto;
import com.johanastrom.authors.repositories.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorIntermediary implements IntermediaryService {

    private AuthorToDto authorToDto;
    private AuthorRepository authorRepository;

    @Autowired
    public AuthorIntermediary(AuthorToDto authorToDto, AuthorRepository authorRepository) {
        this.authorToDto=authorToDto;
        this.authorRepository=authorRepository;

    }


    @Override
    public List<AuthorRecord> getAllAuthors(){
        return authorToDto.map(authorRepository.findAll());
    }

    @Override
    public Optional<AuthorRecord> getOneAuthor(int id){
        return authorToDto.map(authorRepository.findById(id));
    }

    public List<AuthorRecord> getByLastName(String lastName){
        return authorToDto.map(authorRepository.findByLastName(lastName));
    }

    public List<AuthorRecord> getByFirstName(String firstName){
        return authorToDto.map(authorRepository.findByFirstName(firstName));

    }

    @Override
    public Optional<AuthorRecord> createAuthor(AuthorRecord authorRecord){
        for (AuthorRecord author : getAllAuthors()){
            if (authorRecord.equals(author)){
                return Optional.empty();
            }
        }
        AuthorRecord createdAuthor = authorToDto.map(authorRepository.save(authorToDto.map(authorRecord)));
        return Optional.of(createdAuthor);

    }

    @Override
    public Optional<AuthorRecord> replace(int id, AuthorRecord authorRecord){
        Optional<Author> author = authorRepository.findById(id);
        if (author.isPresent()){
            Author updatedAuthor = author.get();
            updatedAuthor.setFirstName(authorRecord.firstName());
            updatedAuthor.setLastName(authorRecord.lastName());
            updatedAuthor.setBirthDate(authorRecord.birthDate());
            AuthorRecord updatedAuthorRecord = authorToDto.map(authorRepository.save(updatedAuthor));
            return Optional.of(updatedAuthorRecord);
        }
        return Optional.empty();
    }

    @Override
    public Optional<AuthorRecord> update(int id, AuthorPersonalData authorPersonalData){
        Optional<Author> author = authorRepository.findById(id);
        if (author.isPresent()){
            Author updatedAuthor = author.get();
            if (authorPersonalData.firstName()!=null) {
                updatedAuthor.setFirstName(authorPersonalData.firstName());
            }
            if (authorPersonalData.lastName()!=null) {
                updatedAuthor.setLastName(authorPersonalData.lastName());
            }
            if (authorPersonalData.birthDate()!=null){
                updatedAuthor.setBirthDate(authorPersonalData.birthDate());
            }
            return Optional.of(authorToDto.map(authorRepository.save(updatedAuthor)));
        }
        return Optional.empty();
    }

    @Override
    public void delete(int id) {
        authorRepository.deleteById(id);
    }

}
