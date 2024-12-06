package com.example.lifelog.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Answer {
    private Long answer_id;
    private Long entry_id; // 외래 키, diary_entry 테이블 참조
    private Long question_id; // 외래 키, custom_question 테이블 참조
    private String question_text;
    private String answer_text;
    private LocalDateTime answer_at;
}
