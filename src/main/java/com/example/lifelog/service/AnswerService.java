package com.example.lifelog.service;

import com.example.lifelog.domain.Answer;
import com.example.lifelog.dto.AnswerRequestDto;
import com.example.lifelog.dto.AnswerResponseDto;
import com.example.lifelog.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnswerService {

    private final AnswerRepository answerRepository;

    @Transactional
    public AnswerResponseDto.AnswerDetailDto createAnswer(AnswerRequestDto.CreateAnswerDto createAnswerDto) throws SQLException {
        Answer answer = Answer.builder()
                .entry_id(createAnswerDto.getEntry_id())
                .question_id(createAnswerDto.getQuestion_id())
                .answer_text(createAnswerDto.getAnswer_text())
                .build();

        Answer savedAnswer = answerRepository.save(answer);

        return AnswerResponseDto.AnswerDetailDto.builder()
                .answer_id(savedAnswer.getAnswer_id())
                .entry_id(savedAnswer.getEntry_id())
                .question_id(savedAnswer.getQuestion_id())
                .answer_text(savedAnswer.getAnswer_text())
                .answer_at(savedAnswer.getAnswer_at())
                .build();
    }

    public AnswerResponseDto.AnswerDetailDto getAnswer(Long id) throws SQLException {
        Answer answer = answerRepository.findById(id);
        return AnswerResponseDto.AnswerDetailDto.builder()
                .answer_id(answer.getAnswer_id())
                .entry_id(answer.getEntry_id())
                .question_id(answer.getQuestion_id())
                .answer_text(answer.getAnswer_text())
                .answer_at(answer.getAnswer_at())
                .build();
    }

    @Transactional
    public AnswerResponseDto.AnswerDetailDto updateAnswer(Long id, AnswerRequestDto.UpdateAnswerDto updateAnswerDto) throws SQLException {
        Answer existingAnswer = answerRepository.findById(id);
        if (existingAnswer == null) {
            throw new NoSuchElementException("Answer with id " + id + " not found.");
        }

        existingAnswer.setAnswer_text(updateAnswerDto.getAnswer_text());
        answerRepository.updateAnswer(existingAnswer);

        return AnswerResponseDto.AnswerDetailDto.builder()
                .answer_id(existingAnswer.getAnswer_id())
                .entry_id(existingAnswer.getEntry_id())
                .question_id(existingAnswer.getQuestion_id())
                .answer_text(existingAnswer.getAnswer_text())
                .answer_at(existingAnswer.getAnswer_at())
                .build();
    }


    @Transactional
    public void deleteAnswer(Long id) throws SQLException {
        answerRepository.deleteById(id);
    }
}
