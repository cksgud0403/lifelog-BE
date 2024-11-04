package com.example.lifelog.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class QuestionOption {
    private Long option_id;
    private Long question_id;
    private String option_text;
}
