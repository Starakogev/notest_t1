package com.starakogev.notes.dto;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteDto {
    private String name;
    private String note;
    private Timestamp creationDate;
}
