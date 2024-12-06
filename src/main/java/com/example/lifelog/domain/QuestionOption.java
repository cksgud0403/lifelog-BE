package com.example.lifelog.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor // No-argument constructor
public class QuestionOption {
    private Long option_id;
    private Long question_id;
    private String option_text;

    // Add this constructor
    public QuestionOption(Long option_id, Long question_id, String option_text) {
        this.option_id = option_id;
        this.question_id = question_id;
        this.option_text = option_text;
    }
}