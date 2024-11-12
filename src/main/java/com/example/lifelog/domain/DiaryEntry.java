package com.example.lifelog.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@ToString
public class DiaryEntry {
    Long entry_id;
    Long user_id;
    LocalDate date;
    String content;
    String emotion_score;
    LocalDateTime created_at;
    LocalDateTime updated_at;
}
