package com.starakogev.notes;

import com.starakogev.notes.entity.Note;
import com.starakogev.notes.service.NoteJDBCTemplateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import java.sql.*;
import java.time.LocalDateTime;

@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NotesApplicationTests {

    @Autowired
    private NoteJDBCTemplateService jdbcTemplate;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
    }
}
