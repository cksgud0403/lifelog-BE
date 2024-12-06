package com.example.lifelog.repository;

import com.example.lifelog.domain.Answer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class AnswerRepository {
    private final DataSource dataSource;

    public Answer save(Answer answer) throws SQLException {
        String sql = "INSERT INTO answer (entry_id, question_id, answer_text) VALUES (?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setLong(1, answer.getEntry_id());
            pstmt.setLong(2, answer.getQuestion_id());
            pstmt.setString(3, answer.getAnswer_text());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Long answerId = generatedKeys.getLong(1);
                    return Answer.builder()
                            .answer_id(answerId)
                            .entry_id(answer.getEntry_id())
                            .question_id(answer.getQuestion_id())
                            .answer_text(answer.getAnswer_text())
                            .answer_at(LocalDateTime.now())
                            .build();
                }
            }
        }
        throw new SQLException("Creating answer failed, no ID obtained.");
    }

    public Answer findById(Long id) throws SQLException {
        String sql = "SELECT * FROM answer WHERE answer_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Answer.builder()
                            .answer_id(rs.getLong("answer_id"))
                            .entry_id(rs.getLong("entry_id"))
                            .question_id(rs.getLong("question_id"))
                            .answer_text(rs.getString("answer_text"))
                            .answer_at(rs.getTimestamp("answer_at").toLocalDateTime())
                            .build();
                }
            }
        }
        return null;
    }

    public void updateAnswer(Answer answer) throws SQLException {
        String sql = "UPDATE answer SET answer_text = ? WHERE answer_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, answer.getAnswer_text());
            pstmt.setLong(2, answer.getAnswer_id());
            pstmt.executeUpdate();
        }
    }

    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM answer WHERE answer_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        }
    }

    public List<Answer> findByEntryId(Long entryId) throws SQLException {
        String sql = "SELECT * FROM answer WHERE entry_id = ?";
        List<Answer> answers = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, entryId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Answer answer = Answer.builder()
                            .answer_id(rs.getLong("answer_id"))
                            .entry_id(rs.getLong("entry_id"))
                            .question_id(rs.getLong("question_id"))
                            .answer_text(rs.getString("answer_text"))
                            .answer_at(rs.getTimestamp("answer_at").toLocalDateTime())
                            .build();
                    answers.add(answer);
                }
            }
        }
        return answers;
    }
}

