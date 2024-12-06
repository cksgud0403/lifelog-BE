package com.example.lifelog.service;

import com.example.lifelog.domain.QuestionOption;
import com.example.lifelog.dto.QuestionOptionRequestDto;
import com.example.lifelog.dto.QuestionOptionResponseDto;
import com.example.lifelog.repository.QuestionOptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class QuestionOptionService {


    private final QuestionOptionRepository questionOptionRepository;


    @Transactional
    public QuestionOptionResponseDto.createQuestionOptionResultDto createQuestionOption(QuestionOptionRequestDto.createQuestionOptionDto createCustomQuestionDto) throws SQLException {
        QuestionOption customQuestion = QuestionOption.builder() //Dto -> User 객체로 변환
                .question_id(createCustomQuestionDto.getQuestion_id())
                .option_text(createCustomQuestionDto.getOption_text())
                .build();

        QuestionOption saveQuestionOption = questionOptionRepository.save(customQuestion); //User 객체를 저장한다. User 객체는 DB의 테이블과 1:1 매핑되는 객체

        return QuestionOptionResponseDto.createQuestionOptionResultDto //User 객체 -> Dto
                .builder()
                .option_id(saveQuestionOption.getOption_id())
                .question_id(saveQuestionOption.getQuestion_id())
                .option_text(saveQuestionOption.getOption_text())
                .build();
    }

    public QuestionOptionResponseDto.QuestionOptionDetailDto getQuestionOption (Long id) throws SQLException {
        QuestionOption questionOption = questionOptionRepository.findById(id);

        return QuestionOptionResponseDto.QuestionOptionDetailDto
                .builder()
                .option_id(questionOption.getOption_id())
                .question_id(questionOption.getQuestion_id())
                .option_text(questionOption.getOption_text())
                .build();
    }

    @Transactional
    public QuestionOptionResponseDto.updateQuestionOptionResultDto modifyQuestionOption(Long id, QuestionOptionRequestDto.updateQuestionOptionDto updateQuestionOptionDto) throws SQLException {
        QuestionOption questionOption = QuestionOption.builder() //Dto -> User 객체로 변환
                .option_text(updateQuestionOptionDto.getOption_text())
                .build();

        QuestionOption updateQuestionOption = questionOptionRepository.updateQuestionOption(id, questionOption);

        return QuestionOptionResponseDto.updateQuestionOptionResultDto
                .builder()
                .option_id(updateQuestionOption.getOption_id())
                .question_id(updateQuestionOption.getQuestion_id())
                .option_text(updateQuestionOption.getOption_text())
                .build();
    }

    @Transactional
    public void removeCustomQuestion(Long id) throws SQLException {
        questionOptionRepository.deleteQuestionOption(id);
    }
}