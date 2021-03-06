package com.starakogev.notes;

import com.starakogev.notes.dto.NoteDto;
import com.starakogev.notes.entity.Note;
import com.starakogev.notes.service.NoteJDBCTemplateService;
import com.starakogev.notes.service.TimeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.Map;

@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(scripts = "classpath:clear_data_base.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class NotesApplicationTests {

    @Autowired
    private NoteJDBCTemplateService jdbcTemplate;
    @Autowired
    private TestRestTemplate restTemplate;
    @SpyBean
    private TimeService timeService;

    @Test
    void contextLoads() {
    }

    @Test
    void insertNote(){
        LocalDateTime time = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        Mockito.when(timeService.getCurrentTime()).thenReturn(time);
        NoteDto note = NoteDto.builder()
                .name("name")
                .note("note")
                .creationDate(Timestamp.valueOf(time))
                .hashtag(List.of("#1", "#2"))
                .build();
        restTemplate.exchange("/create/note", HttpMethod.POST, new HttpEntity<>(note), new ParameterizedTypeReference<String>() {
        });
        ResponseEntity<List<NoteDto>> responseEntity = restTemplate.exchange("/find/notes/by-text?text={text}", HttpMethod.GET,
                HttpEntity.EMPTY, new ParameterizedTypeReference<List<NoteDto>>() {}, Map.of("text", "note"));
        Assertions.assertTrue(responseEntity.hasBody());
        Assertions.assertEquals(1, responseEntity.getBody().size());
        Assertions.assertEquals(note, responseEntity.getBody().get(0));
    }
}
