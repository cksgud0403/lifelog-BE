package com.example.lifelog.controller;

import com.example.lifelog.dto.AnswerRequestDto;
import com.example.lifelog.dto.AnswerResponseDto;
import com.example.lifelog.service.AnswerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/answers")
@RequiredArgsConstructor
@Tag(name= "Answer", description = "Answer API")
public class AnswerController {

    private final AnswerService answerService;

    @PostMapping
    public ResponseEntity<AnswerResponseDto.AnswerDetailDto> createAnswer(@RequestBody AnswerRequestDto.CreateAnswerDto createAnswerDto) throws SQLException {
        return new ResponseEntity<>(answerService.createAnswer(createAnswerDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnswerResponseDto.AnswerDetailDto> getAnswer(@PathVariable("id") Long id) throws SQLException {
        return new ResponseEntity<>(answerService.getAnswer(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnswerResponseDto.AnswerDetailDto> updateAnswer(@PathVariable("id") Long id, @RequestBody AnswerRequestDto.UpdateAnswerDto updateAnswerDto) throws SQLException {
        AnswerResponseDto.AnswerDetailDto updatedAnswer = answerService.updateAnswer(id, updateAnswerDto);
        return ResponseEntity.ok(updatedAnswer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable Long id) throws SQLException {
        answerService.deleteAnswer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/entry/{entryId}")
    public ResponseEntity<List<AnswerResponseDto.AnswerDetailDto>> getAnswersByEntryId(@PathVariable("entryId") Long entryId) throws SQLException {
        List<AnswerResponseDto.AnswerDetailDto> answers = answerService.getAnswersByEntryId(entryId);
        return new ResponseEntity<>(answers, HttpStatus.OK);
    }
}

