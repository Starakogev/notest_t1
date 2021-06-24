package com.starakogev.notes.dto;

import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class NoteDto {
    private String name;
    private String note;
    private Timestamp creationDate;
    private List<String> hashtag;
}
