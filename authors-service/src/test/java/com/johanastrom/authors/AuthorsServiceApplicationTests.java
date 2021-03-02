package com.johanastrom.authors;

import com.johanastrom.authors.dtos.AuthorRecord;
import com.johanastrom.authors.entities.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthorsServiceApplicationTests {

    @Autowired
    private TestRestTemplate testClient;

    @LocalServerPort
    private int port;

    @Test
    void contextLoads() {
        var postResult = testClient.postForEntity("http://localhost:" + port + "/authors", new AuthorRecord(1, "test", "test", new Timestamp(1000)), AuthorRecord.class);
        assertThat(postResult.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        var getResult = testClient.getForEntity("http://localhost:" + port + "/authors", Author[].class);
        assertThat(getResult.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResult.getBody().length).isGreaterThan(0);

        var patchedAuthor = testClient.patchForObject("http://localhost:" + port + "/authors", new AuthorRecord(1, "test2", "test2", new Timestamp(1000)), AuthorRecord.class);
        ResponseEntity<AuthorRecord> patchResult = ResponseEntity.of(Optional.of(patchedAuthor));
        assertThat(patchResult.getStatusCode().equals(HttpStatus.OK));

    }

}
