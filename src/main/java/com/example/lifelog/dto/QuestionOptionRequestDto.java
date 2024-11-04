package com.example.lifelog.dto;

import lombok.Getter;
import lombok.Setter;

public class QuestionOptionRequestDto {


    @Getter
    @Setter
    public static class createQuestionOptionDto {
        Long question_id;
        String option_text;
    }


    @Getter
    @Setter
    public static class updateQuestionOptionDto {
        String option_text;
    }
}
