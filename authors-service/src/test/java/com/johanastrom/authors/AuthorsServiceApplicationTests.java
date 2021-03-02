package com.johanastrom.authors;

import com.johanastrom.authors.entities.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthorsServiceApplicationTests {

    @Autowired
    private TestRestTemplate testClient;

    @LocalServerPort
    private int port;

    @Test
    void contextLoads() {
        var result = testClient.getForEntity("http://localhost:" + port + "/authors", Author[].class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        //assertThat(result.getBody().length).isGreaterThan(0);
    }

}
