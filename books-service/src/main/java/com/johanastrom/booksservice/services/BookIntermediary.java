package com.johanastrom.booksservice.services;

import com.johanastrom.booksservice.dtos.BookDetails;
import com.johanastrom.booksservice.dtos.BookObject;
import com.johanastrom.booksservice.entities.Book;
import com.johanastrom.booksservice.mappers.BookMapper;
import com.johanastrom.booksservice.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookIntermediary {

    private BookRepository bookRepository;

    private BookMapper mapper;

    @Autowired
    public BookIntermediary(BookRepository bookRepository, BookMapper mapper) {
        this.bookRepository = bookRepository;
        this.mapper = mapper;
    }

    public List<BookObject> getAll(){
        return mapper.map(bookRepository.findAll());
    }

    public Optional<BookObject> getOne(long id){
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()){
            return Optional.of(mapper.map(book.get()));
        }
        return Optional.empty();
    }

    public List<BookObject> getBookByTitle(String title){
        return mapper.map(bookRepository.findByTitle(title));
    }

    public Optional<BookObject> create(BookObject bookObject){
        if (bookObject.price()<0 || bookRepository.findById(bookObject.isbn13()).isPresent()){
            return Optional.empty();
        }
        Book book = mapper.map(bookObject);
        BookObject bookToSave = mapper.map(bookRepository.save(book));
        return Optional.of(bookToSave);
    }

    public Optional<BookObject> replace(Long isbn13, BookObject bookObject){

        Optional<Book> book = bookRepository.findById(isbn13);
        if (book.isPresent() && bookObject.price()>0){
            Book bookToUpdate = book.get();
            bookToUpdate.setTitle(bookObject.title());
            bookToUpdate.setLanguage(bookObject.language());
            bookToUpdate.setPrice(bookObject.price());
            bookToUpdate.setGenre(bookObject.genre());
            BookObject updatedBook = mapper.map(bookRepository.save(bookToUpdate));
            return Optional.of(updatedBook);
        }
        return Optional.empty();
    }

    public Optional<BookObject> update(Long id, BookDetails bookDetails){
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()){
            Book bookToUpdate = book.get();
            if (bookDetails.title()!=null){
                bookToUpdate.setTitle(bookDetails.title());
            }
            if (bookDetails.language()!=null){
                bookToUpdate.setLanguage(bookDetails.language());
            }
            if (bookDetails.price()>0){
                bookToUpdate.setPrice(bookDetails.price());
            }
            if (bookDetails.genre()!=null){
                bookToUpdate.setGenre(bookDetails.genre());
            }
            BookObject updatedBook = mapper.map(bookRepository.save(bookToUpdate));
            return Optional.of(updatedBook);
        }
        return Optional.empty();
    }

    public void delete(Long id){
        bookRepository.deleteById(id);
    }


}
