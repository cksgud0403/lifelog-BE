package com.example.lifelog.dto;

import lombok.Getter;
import lombok.Setter;

public class CustomQuestionRequestDto {


    @Getter
    @Setter
    public static class createCustomQuestionDto {
        String question_text;
        Long user_id;
        String question_type;
    }


    @Getter
    @Setter
    public static class updateCustomQuestionDto {
        String question_text;
        String question_type;
    }
}
