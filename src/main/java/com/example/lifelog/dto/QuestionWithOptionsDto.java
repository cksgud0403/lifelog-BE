package com.example.lifelog.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class QuestionWithOptionsDto {
    private Long id; // 질문 ID
    private String question; // 질문 텍스트
    private String description; // 질문 유형 ('객관식', '주관식')
    private List<String> options; // 객관식 옵션
    private Integer orderIndex; // 추가
}
