package com.example.lifelog.service;

import com.example.lifelog.domain.CustomQuestion;
import com.example.lifelog.domain.User;
import com.example.lifelog.dto.CustomQuestionRequestDto;
import com.example.lifelog.dto.CustomQuestionResponseDto;
import com.example.lifelog.dto.UserRequestDto;
import com.example.lifelog.dto.UserResponseDto;
import com.example.lifelog.repository.CustomQuestionRepository;
import com.example.lifelog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CustomQuestionService {


    private final CustomQuestionRepository customQuestionRepository;


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
        CustomQuestion customQuestion = customQuestionRepository.findById(id);


        return CustomQuestionResponseDto.CustomQuestionDetailDto
                .builder()
                .question_text(customQuestion.getQuestion_text())
                .question_type(customQuestion.getQuestion_type())
                .build();
    }

    @Transactional
    public CustomQuestionResponseDto.updateCustomQuestionResultDto modifyCustomQuestion(Long id, CustomQuestionRequestDto.updateCustomQuestionDto updateCustomQuestionDto) throws SQLException {
        CustomQuestion customQuestion = CustomQuestion.builder() //Dto -> User 객체로 변환
                .question_text(updateCustomQuestionDto.getQuestion_text())
                .question_type(updateCustomQuestionDto.getQuestion_type())
                .build();

        CustomQuestion updatedCustomQuestion = customQuestionRepository.updateCustomQuestion(id, customQuestion);

        return CustomQuestionResponseDto.updateCustomQuestionResultDto
                .builder()
                .question_text(customQuestion.getQuestion_text())
                .question_type(customQuestion.getQuestion_type())
                .updated_at(updatedCustomQuestion.getUpdated_at())
                .build();
    }

    @Transactional
    public void removeCustomQuestion(Long id) throws SQLException {
        customQuestionRepository.deleteCustomQuestion(id);
    }

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
}