package com.example.lifelog.controller;


import com.example.lifelog.dto.CustomQuestionRequestDto;
import com.example.lifelog.dto.CustomQuestionResponseDto;
import com.example.lifelog.dto.QuestionOptionRequestDto;
import com.example.lifelog.dto.QuestionOptionResponseDto;
import com.example.lifelog.service.CustomQuestionService;
import com.example.lifelog.service.QuestionOptionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/question-options")
@RequiredArgsConstructor
@Tag(name = "QuestionOption", description = "QuestionOption API")
public class QuestionOptionController {

    private final QuestionOptionService questionOptionService;

    @GetMapping("/{id}")
    public ResponseEntity<QuestionOptionResponseDto.QuestionOptionDetailDto> getQuestionOption(@PathVariable("id") Long id) throws SQLException {
        return new ResponseEntity<>(questionOptionService.getQuestionOption(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<QuestionOptionResponseDto.createQuestionOptionResultDto> createQuestionOption(@RequestBody QuestionOptionRequestDto.createQuestionOptionDto createCustomQuestionDto) throws SQLException {
        return new ResponseEntity<>(questionOptionService.createQuestionOption(createCustomQuestionDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionOptionResponseDto.updateQuestionOptionResultDto> modifyQuestionOption(@PathVariable("id") Long id, @RequestBody QuestionOptionRequestDto.updateQuestionOptionDto updateQuestionOptionDto) throws SQLException {
        return new ResponseEntity<>(questionOptionService.modifyQuestionOption(id, updateQuestionOptionDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeQuestionOption(@PathVariable("id") Long id) throws SQLException {
        questionOptionService.removeCustomQuestion(id);
        return ResponseEntity.noContent().build();
    }
}
