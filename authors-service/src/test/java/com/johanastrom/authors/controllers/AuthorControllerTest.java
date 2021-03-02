package com.johanastrom.authors.controllers;

import com.johanastrom.authors.configurations.JohansConfig;
import com.johanastrom.authors.dtos.AuthorRecord;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AuthorControllerTest {


    @Test
    public void getAllAuthorsShouldReturnListOfAuthorRecordObjects(){
        AuthorController controller = new AuthorController(new TestIntermediary(), new JohansConfig());
        List<AuthorRecord> authorList = controller.getAllAuthors();
        assertThat(authorList.size()).isGreaterThan(0);
        assertThat(authorList.get(0).firstName()).isEqualTo("Record1");
        assertThat(authorList.get(1).lastName()).isEqualTo("Record2");
    }

    @Test
    public void createAuthorShouldThrowExceptionWhenPassedInvalidAuthorRecordObject(){
        AuthorController controller = new AuthorController(new TestIntermediary(), new JohansConfig());
        AuthorRecord record = new AuthorRecord(2, "test", "test", new Timestamp(1000));
        var exception = assertThrows(ResponseStatusException.class, () -> controller.createAuthor(record));
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.CONFLICT);
    }

}
