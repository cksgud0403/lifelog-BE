package com.example.lifelog.dto;

import lombok.*;

import java.time.LocalDateTime;

public class AnswerResponseDto {

    @Getter
    @Setter
    @Builder
    public static class AnswerDetailDto {
        private Long answer_id;
        private Long entry_id;
        private Long question_id;
        private String answer_text;
        private LocalDateTime answer_at;
    }
}

