package com.example.lifelog.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomQuestionListDto {
        private Long id;
        private String question;
        private String description;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomQuestionsByUserDto {
        private List<CustomQuestionListDto> questions;
    }
}
