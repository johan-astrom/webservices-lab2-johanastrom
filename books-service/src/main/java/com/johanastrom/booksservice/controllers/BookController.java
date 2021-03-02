package com.johanastrom.booksservice.controllers;

import com.johanastrom.booksservice.dtos.BookDetails;
import com.johanastrom.booksservice.dtos.BookObject;
import com.johanastrom.booksservice.services.BookIntermediary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.function.Supplier;

@RestController
@RequestMapping("/books")
@Slf4j
public class BookController {

    private BookIntermediary bookIntermediary;

    @Autowired
    public BookController(BookIntermediary bookIntermediary) {
        this.bookIntermediary = bookIntermediary;
    }

    @GetMapping
    public List<BookObject> getAllBooks(){
        return bookIntermediary.getAll();
    }

    @GetMapping("/{isbn13}")
    public BookObject getOneBook(@PathVariable Long isbn13){
        return bookIntermediary.getOne(isbn13).orElseThrow(() -> notFoundExceptionSupplier(isbn13));
    }

    @GetMapping("/search")
    public List<BookObject> getBookByTitle(@RequestParam String title){
        List<BookObject> booksFound = bookIntermediary.getBookByTitle(title);
        if (!booksFound.isEmpty()){
            return booksFound;
        }
        String message = "Book with the requested title not found.";
        log.error(message);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookObject createBook(@RequestBody BookObject bookObject){
        return bookIntermediary.create(bookObject).orElseThrow(this::conflictExceptionSupplier);
    }


    @PutMapping("/{isbn13}")
    public BookObject replaceBook(@RequestBody BookObject bookObject, @PathVariable Long isbn13){
        return bookIntermediary.replace(isbn13, bookObject).orElseThrow(() -> notFoundExceptionSupplier(isbn13));
    }

    @PatchMapping("/{isbn13}")
    public BookObject updateBook(@RequestBody BookDetails bookDetails, @PathVariable Long isbn13){
        return bookIntermediary.update(isbn13, bookDetails).orElseThrow(() -> notFoundExceptionSupplier(isbn13));
    }

    @DeleteMapping("/{isbn13}")
    public void deleteBook(@PathVariable Long isbn13){
        try {
            bookIntermediary.delete(isbn13);
        }catch (DataAccessException e){
            throw notFoundExceptionSupplier(isbn13);
        }
    }

    private ResponseStatusException notFoundExceptionSupplier(Long isbn13){
        String message = "Book with isbn <" + isbn13 + "> not found.";
        log.error(message);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
    }

    private ResponseStatusException conflictExceptionSupplier() {
        String message = "Book already persisted to database.";
        log.error(message);
        throw new ResponseStatusException(HttpStatus.CONFLICT, message);
    }
}
