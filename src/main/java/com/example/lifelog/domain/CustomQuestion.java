package com.example.lifelog.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@ToString
public class CustomQuestion {
    private Long question_id;
    private Long user_id;
    private String question_text;
    private String question_type;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private Integer order_index;

    public CustomQuestion(Long question_id, Long user_id, String question_text, String question_type,
                          LocalDateTime created_at, LocalDateTime updated_at, Integer order_index) {
        this.question_id = question_id;
        this.user_id = user_id;
        this.question_text = question_text;
        this.question_type = question_type;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.order_index = order_index;
    }

    public Integer getOrder_index() { return order_index; }
    public void setOrder_index(Integer order_index) { this.order_index = order_index; }
    public void setQuestion_text(String question_text) {
        this.question_text = question_text;
    }
}
