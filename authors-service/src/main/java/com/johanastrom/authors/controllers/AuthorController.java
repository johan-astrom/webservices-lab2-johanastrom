package com.johanastrom.authors.controllers;

import com.johanastrom.authors.configurations.JohansConfig;
import com.johanastrom.authors.dtos.AuthorPersonalData;
import com.johanastrom.authors.dtos.AuthorRecord;
import com.johanastrom.authors.services.IntermediaryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/authors")
@Slf4j
public class AuthorController {
    
    private IntermediaryService intermediaryService;
    private JohansConfig johansConfig;

    @Autowired
    public AuthorController(IntermediaryService intermediaryService, JohansConfig johansConfig) {
        this.intermediaryService = intermediaryService;
        this.johansConfig=johansConfig;
    }

    @GetMapping
    public List<AuthorRecord> getAllAuthors() {
        return intermediaryService.getAllAuthors();
    }

    @GetMapping("/{id}")
    public AuthorRecord getOneAuthor(@PathVariable int id) {
        return intermediaryService.getOneAuthor(id).orElseThrow(() -> notFoundExceptionSupplier(id));
    }

    @GetMapping("/johansconfig")
    public String getConfig(){
        return this.johansConfig.getTestKey();
    }

    @GetMapping("/search")
    public List<AuthorRecord> getByName(@RequestParam Optional<String> lastName,
                                        @RequestParam Optional<String> firstName){
        if (lastName.isPresent() && firstName.isPresent()){
            List<AuthorRecord> authorsFound = intermediaryService.getByLastName(lastName.get());
            List<AuthorRecord> authorsResult = new ArrayList<>();
            for (AuthorRecord author : authorsFound){
                if (author.firstName().equals(firstName.get())){
                    authorsResult.add(author);
                }
            }
            return authorsResult;
        }
        if (lastName.isPresent()) {
            return intermediaryService.getByLastName(lastName.get());
        }
        if (firstName.isPresent()){
            return intermediaryService.getByFirstName(firstName.get());
        }
        String message = "Author with the specified name not found.";
        log.error(message);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorRecord createAuthor(@RequestBody AuthorRecord authorRecord) {
        return intermediaryService.createAuthor(authorRecord).orElseThrow(this::conflictExceptionSupplier);
    }

    @PutMapping("/{id}")
    public AuthorRecord replaceAuthor(@PathVariable int id, @RequestBody AuthorRecord authorRecord) {
        return intermediaryService.replace(id, authorRecord).orElseThrow(() -> notFoundExceptionSupplier(id));
    }

    @PatchMapping("/{id}")
    public AuthorRecord updateAuthor(@PathVariable int id, @RequestBody AuthorPersonalData authorPersonalData) {
        return intermediaryService.update(id, authorPersonalData).orElseThrow(() -> notFoundExceptionSupplier(id));
    }

    @DeleteMapping("/{id}")
    public void removeAuthor(@PathVariable int id) {
        try {
            intermediaryService.delete(id);
        } catch (DataAccessException e) {
            throw notFoundExceptionSupplier(id);
        }
    }

    private ResponseStatusException notFoundExceptionSupplier(int id){
        String message = "Author with id <" + id + "> not found.";
        log.error(message);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
    }

    private ResponseStatusException conflictExceptionSupplier() {
        String message = "Author already persisted to database.";
        log.error(message);
        throw new ResponseStatusException(HttpStatus.CONFLICT, message);
    }

}
