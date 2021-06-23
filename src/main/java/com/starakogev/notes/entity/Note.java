package com.starakogev.notes.entity;

import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Note {
    private Long id;
    private String name;
    private String note;
    private Timestamp creationDate;
    private String hashtag;
}
