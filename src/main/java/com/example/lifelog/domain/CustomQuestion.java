package com.example.lifelog.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
public class CustomQuestion {
    Long question_id;
    Long user_id;
    String question_text;
    String question_type;
    LocalDateTime created_at;
    LocalDateTime updated_at;
}
