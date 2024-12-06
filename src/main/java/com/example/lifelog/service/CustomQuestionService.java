package com.example.lifelog.service;

import com.example.lifelog.domain.CustomQuestion;
import com.example.lifelog.domain.User;
import com.example.lifelog.dto.*;
import com.example.lifelog.repository.CustomQuestionRepository;
import com.example.lifelog.repository.QuestionOptionRepository;
import com.example.lifelog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import com.example.lifelog.domain.QuestionOption;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CustomQuestionService {


    private final CustomQuestionRepository customQuestionRepository;
    private final QuestionOptionRepository questionOptionRepository;

    @Transactional
    public CustomQuestionResponseDto.createCustomQuestionResultDto createCustomQuestion(CustomQuestionRequestDto.createCustomQuestionDto createCustomQuestionDto) throws SQLException {
        CustomQuestion customQuestion = CustomQuestion.builder() //Dto -> User 객체로 변환
                .user_id(createCustomQuestionDto.getUser_id())
                .question_text(createCustomQuestionDto.getQuestion_text())
                .question_type(createCustomQuestionDto.getQuestion_type())
                .build();

        CustomQuestion saveCustomQuestion = customQuestionRepository.save(customQuestion); //User 객체를 저장한다. User 객체는 DB의 테이블과 1:1 매핑되는 객체

        return CustomQuestionResponseDto.createCustomQuestionResultDto //User 객체 -> Dto
                .builder()
                .question_id(saveCustomQuestion.getQuestion_id())
                .user_id(saveCustomQuestion.getUser_id())
                .question_txt(saveCustomQuestion.getQuestion_text())
                .question_type(saveCustomQuestion.getQuestion_type())
                .created_at(saveCustomQuestion.getCreated_at())
                .updated_at(saveCustomQuestion.getUpdated_at())
                .build();
    }

    public CustomQuestionResponseDto.CustomQuestionDetailDto getCustomQuestion(Long id) throws SQLException {
        CustomQuestion customQuestion = customQuestionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No CustomQuestion found with id: " + id));

        return CustomQuestionResponseDto.CustomQuestionDetailDto
                .builder()
                .question_text(customQuestion.getQuestion_text())
                .question_type(customQuestion.getQuestion_type())
                .build();
    }

    @Transactional
    public CustomQuestionResponseDto.updateCustomQuestionResultDto modifyCustomQuestion(Long id, CustomQuestionRequestDto.updateCustomQuestionDto questionDto) throws SQLException {
        CustomQuestion question = customQuestionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid question ID"));
        question.setQuestion_text(questionDto.getQuestion_text());
        question.setQuestion_type(questionDto.getQuestion_type());
        
        CustomQuestion updatedQuestion = customQuestionRepository.updateCustomQuestion(id, question);
        
        return CustomQuestionResponseDto.updateCustomQuestionResultDto.builder()
                .question_text(updatedQuestion.getQuestion_text())
                .question_type(updatedQuestion.getQuestion_type())
                .updated_at(updatedQuestion.getUpdated_at())
                .build();
    }

    @Transactional
    public void removeCustomQuestion(Long id) throws SQLException {
        customQuestionRepository.deleteCustomQuestion(id);
    }

    /**
     * 특정 유저의 질문 조회. (유저 ID로 질문 조회)
     */
    public CustomQuestionResponseDto.CustomQuestionsByUserDto getCustomQuestionsByUserId(Long userId) throws SQLException {
        List<CustomQuestion> questions = customQuestionRepository.findByUserId(userId);

        List<CustomQuestionResponseDto.CustomQuestionListDto> questionDtos = questions.stream()
                .map(q -> CustomQuestionResponseDto.CustomQuestionListDto.builder()
                        .id(q.getQuestion_id())
                        .question(q.getQuestion_text())
                        .description(q.getQuestion_type())
                        .createdAt(q.getCreated_at())
                        .updatedAt(q.getUpdated_at())
                        .build())
                .collect(Collectors.toList());

        return CustomQuestionResponseDto.CustomQuestionsByUserDto.builder()
                .questions(questionDtos)
                .build();
    }

    /**
     * 객관식 옵션과 함께 질문 저장.
     */
    public void saveQuestionsWithOptions(List<QuestionWithOptionsDto> questions) throws SQLException {
        for (QuestionWithOptionsDto question : questions) {
            // 1. 질문 저장 또는 수정
            CustomQuestion savedQuestion = saveOrUpdateQuestion(question);

            // 2. 기존 옵션 삭제
            questionOptionRepository.deleteByQuestionId(savedQuestion.getQuestion_id());
            
            // 3. 새로운 옵션 저장
            if (question.getOptions() != null) {
                for (String option : question.getOptions()) {
                    QuestionOption questionOption = new QuestionOption();
                    questionOption.setQuestion_id(savedQuestion.getQuestion_id());
                    questionOption.setOption_text(option);
                    questionOptionRepository.save(questionOption);
                }
            }
        }
    }

    /**
     * 객관식 옵션과 함께 질문 저장 또는 수정.
     */
    private CustomQuestion saveOrUpdateQuestion(QuestionWithOptionsDto questionDto) throws SQLException {
        CustomQuestion question;
        if (questionDto.getId() != null) {
            // 수정 로직
            question = customQuestionRepository.findById(questionDto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid question ID"));
            question.setQuestion_text(questionDto.getQuestion());
            question.setQuestion_type(questionDto.getDescription());
            question.setOrder_index(questionDto.getOrderIndex());
        } else {
            // 생성 로직
            question = new CustomQuestion();
            question.setQuestion_text(questionDto.getQuestion());
            question.setQuestion_type(questionDto.getDescription());
            question.setOrder_index(questionDto.getOrderIndex());
        }
        return customQuestionRepository.save(question);
    }
}