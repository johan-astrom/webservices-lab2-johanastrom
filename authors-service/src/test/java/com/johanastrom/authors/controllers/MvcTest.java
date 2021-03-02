package com.johanastrom.authors.controllers;

import com.johanastrom.authors.configurations.JohansConfig;
import com.johanastrom.authors.dtos.AuthorRecord;
import com.johanastrom.authors.services.IntermediaryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.sql.Timestamp;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@WebMvcTest
public class MvcTest {

    @MockBean
    private IntermediaryService service;

    @MockBean
    private JohansConfig johansConfig;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void callingWithURLPersonsPlusIDShouldReturnJSONWithOnePersonObject() throws Exception {
        when(service.getOneAuthor(1)).thenReturn(Optional.of(
                new AuthorRecord(1, "test", "test", new Timestamp(1000))));
        var result = mockMvc.perform(MockMvcRequestBuilders.get("/authors/1")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(result.getResponse().getContentType()).isEqualTo("application/json");

    }
}
