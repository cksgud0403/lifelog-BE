package com.example.lifelog.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class QuestionOptionResponseDto {

    @Getter
    @Setter
    @Builder
    public static class createQuestionOptionResultDto {
        Long option_id;
        Long question_id;
        String option_text;
    }


    @Getter
    @Setter
    @Builder
    public static class QuestionOptionDetailDto {
        Long option_id;
        Long question_id;
        String option_text;
    }

    @Getter
    @Setter
    @Builder
    public static class updateQuestionOptionResultDto {
        Long option_id;
        Long question_id;
        String option_text;
    }
}