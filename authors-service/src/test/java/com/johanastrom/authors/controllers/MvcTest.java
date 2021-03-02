package com.johanastrom.authors.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.hamcrest.Matchers.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class MvcTest {

    @MockBean
    private IntermediaryService service;

    @MockBean
    private JohansConfig johansConfig;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void callingWithURLAuhtorsPlusIDShouldReturnJSONWithOneAuthorRecord() throws Exception {
        when(service.getOneAuthor(1)).thenReturn(Optional.of(
                new AuthorRecord(1, "test", "test", new Timestamp(1000))));
        var result = mockMvc.perform(MockMvcRequestBuilders.get("/authors/1")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(result.getResponse().getContentType()).isEqualTo("application/json");
    }

    @Test
    public void callingWithURLAuthorsShouldReturnJSONListOfAuthorRecords() throws Exception{
        when(service.getAllAuthors()).thenReturn(List.of(
                new AuthorRecord(1, "Test", "Test", new Timestamp(1000))));
        var result=mockMvc.perform(MockMvcRequestBuilders.get("/authors")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(result.getResponse().getContentType()).isEqualTo("application/json");
    }

    @Test
    public void callingWithURLAuthorsPlusSearchAndURLParametersShouldReturnJSONListOfAuthorRecords() throws Exception{
        when(service.getByLastName("Test")).thenReturn(List.of(
                new AuthorRecord(1, "Test", "Test", new Timestamp(1000))));
        var result=mockMvc.perform(MockMvcRequestBuilders.get("/authors/search?lastName=Test")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(result.getResponse().getContentType()).isEqualTo("application/json");
    }

    @Test
    public void postRequestShouldGenerateJSONOfAuthorRecordObject() throws Exception{
        var author = new AuthorRecord(1, "Test", "Test", new Timestamp(1000));
        when(service.createAuthor(any(AuthorRecord.class)))
                .thenReturn(Optional.of(author));
        mockMvc.perform(MockMvcRequestBuilders
                .post("/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(author)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is("Test")))
                .andExpect(jsonPath("$.id", is(1)));

    }

    // TODO: 2021-03-02 Lös denna
    @Test
    public void putRequestShouldGenerateJSONOfAuthorRecordObject() throws Exception{
        var author = new AuthorRecord(1, "Test", "Test", new Timestamp(1000));
        when(service.replace(1, any(AuthorRecord.class)))
                .thenReturn(Optional.of(author));
        mockMvc.perform(MockMvcRequestBuilders
                .post("/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(author)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Test")))
                .andExpect(jsonPath("$.id", is(1)));

    }
}
