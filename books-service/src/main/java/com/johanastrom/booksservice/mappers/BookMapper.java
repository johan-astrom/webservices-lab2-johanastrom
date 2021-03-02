package com.johanastrom.booksservice.mappers;

import com.johanastrom.booksservice.dtos.BookObject;
import com.johanastrom.booksservice.entities.Book;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;

// TODO: 2021-02-25 Lös eventuellt Optional-workaround
@Mapper(componentModel = "spring")
public interface BookMapper {

    BookObject map(Book book);

    Book map(BookObject bookObject);

    List<BookObject> map(List<Book> bookList);

}
