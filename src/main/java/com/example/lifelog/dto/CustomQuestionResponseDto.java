package com.example.lifelog.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class CustomQuestionResponseDto {

    @Getter
    @Setter
    @Builder
    public static class createCustomQuestionResultDto {
        Long question_id;
        String question_txt;
        Long user_id;
        String question_type;
        LocalDateTime created_at;
        LocalDateTime updated_at;
    }


    @Getter
    @Setter
    @Builder
    public static class CustomQuestionDetailDto {
        String question_text;
        String question_type;
    }

    @Getter
    @Setter
    @Builder
    public static class updateCustomQuestionResultDto {
        String question_text;
        String question_type;
        LocalDateTime updated_at;
    }
}
