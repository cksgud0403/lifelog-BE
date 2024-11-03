package com.example.lifelog.controller;


import com.example.lifelog.dto.CustomQuestionRequestDto;
import com.example.lifelog.dto.CustomQuestionResponseDto;
import com.example.lifelog.dto.UserRequestDto;
import com.example.lifelog.dto.UserResponseDto;
import com.example.lifelog.service.CustomQuestionService;
import com.example.lifelog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/custom-questions")
@RequiredArgsConstructor
public class CustomQuestionController {

    private final CustomQuestionService customQuestionService;

    @GetMapping("/{id}")
    public ResponseEntity<CustomQuestionResponseDto.CustomQuestionDetailDto> getCustomQuestion(@PathVariable Long id) throws SQLException {
        return new ResponseEntity<>(customQuestionService.getCustomQuestion(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CustomQuestionResponseDto.createCustomQuestionResultDto> createCustomQuestion(@RequestBody CustomQuestionRequestDto.createCustomQuestionDto createCustomQuestionDto) throws SQLException {
        return new ResponseEntity<>(customQuestionService.createCustomQuestion(createCustomQuestionDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomQuestionResponseDto.updateCustomQuestionResultDto> modifyCustomQuestion(@PathVariable Long id, @RequestBody CustomQuestionRequestDto.updateCustomQuestionDto customQuestionDto) throws SQLException {
        return new ResponseEntity<>(customQuestionService.modifyCustomQuestion(id, customQuestionDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCustomQuestion(@PathVariable Long id) throws SQLException {
        customQuestionService.removeCustomQuestion(id);
        return ResponseEntity.noContent().build();
    }
}
