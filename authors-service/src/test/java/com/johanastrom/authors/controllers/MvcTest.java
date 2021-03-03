package com.johanastrom.authors.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.johanastrom.authors.configurations.JohansConfig;
import com.johanastrom.authors.dtos.AuthorPersonalData;
import com.johanastrom.authors.dtos.AuthorRecord;
import com.johanastrom.authors.services.IntermediaryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    public void callingWithURLAuthorsPlusIDShouldReturnJSONWithOneAuthorRecord() throws Exception {
        when(service.getOneAuthor(1)).thenReturn(Optional.of(
                new AuthorRecord(1, "test", "test", new Timestamp(1000))));
        var result = mockMvc.perform(MockMvcRequestBuilders.get("/authors/1")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(result.getResponse().getContentType()).isEqualTo("application/json");
    }

    @Test
    public void callingWithURLAuthorsShouldReturnJSONListOfAuthorRecords() throws Exception {
        when(service.getAllAuthors()).thenReturn(List.of(
                new AuthorRecord(1, "Test", "Test", new Timestamp(1000))));
        var result = mockMvc.perform(MockMvcRequestBuilders.get("/authors")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(result.getResponse().getContentType()).isEqualTo("application/json");
    }

    @Test
    public void callingWithURLAuthorsPlusSearchAndURLParametersShouldReturnJSONListOfAuthorRecords() throws Exception {
        when(service.getByLastName("Test")).thenReturn(List.of(
                new AuthorRecord(1, "Test", "Test", new Timestamp(1000))));
        var result = mockMvc.perform(MockMvcRequestBuilders.get("/authors")
                .param("lastName", "Test")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(result.getResponse().getContentType()).isEqualTo("application/json");
    }

    @Test
    public void postRequestShouldGenerateJSONOfAuthorRecordObject() throws Exception {
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

    @Test
    public void putRequestShouldGenerateJSONOfAuthorRecordObject() throws Exception {
        var author = new AuthorRecord(1, "Test", "Test", new Timestamp(1000));
        when(service.replace(any(Integer.class), any(AuthorRecord.class)))
                .thenReturn(Optional.of(author));
        mockMvc.perform(MockMvcRequestBuilders
                .put("/authors/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(author)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Test")))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void patchRequestShouldGenerateJSONOfAuthorRecordObject() throws Exception {
        var author = new AuthorRecord(1, "Test", "Test", new Timestamp(1000));
        var authorData = new AuthorPersonalData("Test", "Test", new Timestamp(1000));
        when(service.update(any(Integer.class), any(AuthorPersonalData.class)))
                .thenReturn(Optional.of(author));
        mockMvc.perform(MockMvcRequestBuilders
                .patch("/authors/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authorData)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Test")))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void deleteRequestShouldGenerateHttpStatusOK() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/authors/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void callingGetWithInvalidIdShouldGenerateHttpStatusNotFound() throws Exception {
        when(service.getOneAuthor(any(Integer.class))).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/authors/{id}", "2")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(status().isNotFound());
    }

    @Test
    public void callingPutWithInvalidIdShouldGenerateHttpStatusNotFound() throws Exception {
        AuthorRecord author = new AuthorRecord(1, "Test", "Test", new Timestamp(1000));
        when(service.replace(any(Integer.class), any(AuthorRecord.class))).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.put("/authors/{id}", "2")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(author)))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(status().isNotFound());
    }

    @Test
    public void callingPatchWithInvalidIdShouldGenerateHttpStatusNotFound() throws Exception {
        AuthorRecord author = new AuthorRecord(1, "Test", "Test", new Timestamp(1000));
        when(service.update(any(Integer.class), any(AuthorPersonalData.class))).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.patch("/authors/{id}", "2")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(author)))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(status().isNotFound());
    }

    // TODO: 2021-03-03 Färdigställ
    @Test
    public void callingDeleteWithInvalidIdShouldGenerateHttpStatusNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/authors/{id}", "2")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(status().isNotFound());
    }


}





