package com.example.lifelog.dto;

import lombok.Getter;
import lombok.Setter;

public class AnswerRequestDto {

    @Getter
    @Setter
    public static class CreateAnswerDto {
        private Long entry_id;
        private Long question_id;
        private String answer_text;
    }

    @Getter
    @Setter
    public static class UpdateAnswerDto {
        private String answer_text;
    }
}
