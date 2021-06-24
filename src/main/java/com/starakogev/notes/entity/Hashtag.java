package com.starakogev.notes.entity;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hashtag {
    Long id;
    String hashtag;
}
