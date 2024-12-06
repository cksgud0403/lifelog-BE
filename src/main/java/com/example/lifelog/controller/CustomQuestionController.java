package com.example.lifelog.controller;


import com.example.lifelog.dto.CustomQuestionRequestDto;
import com.example.lifelog.dto.CustomQuestionResponseDto;
import com.example.lifelog.dto.UserRequestDto;
import com.example.lifelog.dto.UserResponseDto;
import com.example.lifelog.service.CustomQuestionService;
import com.example.lifelog.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/custom-questions")
@RequiredArgsConstructor
@Tag(name = "CustomQuestion", description = "CustomQuestion API")
public class CustomQuestionController {

    private final CustomQuestionService customQuestionService;

    @GetMapping("/{id}")
    public ResponseEntity<CustomQuestionResponseDto.CustomQuestionDetailDto> getCustomQuestion(@PathVariable("id") Long id) throws SQLException {
        return new ResponseEntity<>(customQuestionService.getCustomQuestion(id), HttpStatus.OK);
    }

    /**
     * 특정 유저의 질문 목록 조회.
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<CustomQuestionResponseDto.CustomQuestionsByUserDto> getCustomQuestionsByUserId(@PathVariable("id") Long id) throws SQLException {
        return new ResponseEntity<>(customQuestionService.getCustomQuestionsByUserId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CustomQuestionResponseDto.createCustomQuestionResultDto> createCustomQuestion(@RequestBody CustomQuestionRequestDto.createCustomQuestionDto createCustomQuestionDto) throws SQLException {
        return new ResponseEntity<>(customQuestionService.createCustomQuestion(createCustomQuestionDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomQuestionResponseDto.updateCustomQuestionResultDto> modifyCustomQuestion(@PathVariable("id") Long id, @RequestBody CustomQuestionRequestDto.updateCustomQuestionDto customQuestionDto) throws SQLException {
        return new ResponseEntity<>(customQuestionService.modifyCustomQuestion(id, customQuestionDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCustomQuestion(@PathVariable("id") Long id) throws SQLException {
        customQuestionService.removeCustomQuestion(id);
        return ResponseEntity.noContent().build();
    }
}
