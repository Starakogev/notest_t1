package com.starakogev.notes.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class TimeService {
    public LocalDateTime getCurrentTime(){
        return LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
    }
}
