package com.example.lifelog.repository;

import com.example.lifelog.domain.CustomQuestion;
import com.example.lifelog.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CustomQuestionRepository {
    private final DataSource dataSource;

    public CustomQuestion save(CustomQuestion question) throws SQLException {
        String sql = "INSERT INTO custom_question (user_id, question_text, question_type) VALUES (?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setLong(1, question.getUser_id());
            pstmt.setString(2, question.getQuestion_text());
            pstmt.setString(3, question.getQuestion_type());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long questionId = generatedKeys.getLong(1);

                    // 삽입된 레코드를 다시 조회하여 created_at과 updated_at을 가져옵니다.
                    String selectSql = "SELECT created_at, updated_at FROM custom_question WHERE question_id = ?";
                    try (PreparedStatement selectPstmt = conn.prepareStatement(selectSql)) {
                        selectPstmt.setLong(1, questionId);
                        try (ResultSet rs = selectPstmt.executeQuery()) {
                            if (rs.next()) {
                                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
                                LocalDateTime updatedAt = rs.getTimestamp("updated_at").toLocalDateTime();

                                log.info("createAt = {}", createdAt);
                                log.info("updatedAt = {}", updatedAt);

                                return CustomQuestion.builder()
                                        .question_id(questionId)
                                        .user_id(question.getUser_id())
                                        .question_text(question.getQuestion_text())
                                        .question_type(question.getQuestion_type())
                                        .created_at(createdAt)
                                        .updated_at(updatedAt)
                                        .build();
                            }
                        }
                    }
                } else {
                    throw new SQLException("Creating custom question failed, no ID obtained.");
                }
            }
        }

        return question;
    }

    public CustomQuestion findById(Long id) throws SQLException {
        String sql = "SELECT question_text, question_type FROM custom_question WHERE question_id = ?";

        CustomQuestion customQuestion = null;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {

                    customQuestion = CustomQuestion.builder()
                            .question_text(rs.getString("question_text"))
                            .question_type(rs.getString("question_type"))
                            .build();
                }
            }
        }

        return customQuestion;
    }

    public CustomQuestion updateCustomQuestion(Long id, CustomQuestion customQuestion) throws SQLException {
        String updateSql = "UPDATE custom_question SET question_text = ?, question_type = ? WHERE question_id = ?";

        try (Connection conn = dataSource.getConnection()) {
            // 업데이트 쿼리 설정 및 실행
            try (PreparedStatement updatePstmt = conn.prepareStatement(updateSql)) {
                updatePstmt.setString(1, customQuestion.getQuestion_text());
                updatePstmt.setString(2, customQuestion.getQuestion_type());
                updatePstmt.setLong(3, id);

                int rowsUpdated = updatePstmt.executeUpdate();
                if (rowsUpdated == 0) {
                    throw new NoSuchElementException("No CustomQuestion found with id: " + id); // 레코드가 없을 경우 예외 발생
                }
            }

            String selectSql = "SELECT user_id, question_text, question_type, updated_at FROM custom_question WHERE question_id = ?";

            // 업데이트 후 최신 정보를 가져오기 위해 SELECT 쿼리 실행
            try (PreparedStatement selectPstmt = conn.prepareStatement(selectSql)) {
                selectPstmt.setLong(1, id);

                try (ResultSet rs = selectPstmt.executeQuery()) {
                    if (rs.next()) {
                        return CustomQuestion.builder()
                                .question_id(id)
                                .user_id(rs.getLong("user_id"))
                                .question_text(rs.getString("question_text"))
                                .question_type(rs.getString("question_type"))
                                .updated_at(rs.getTimestamp("updated_at").toLocalDateTime())
                                .build();
                    } else {
                        throw new NoSuchElementException("No CustomQuestion found with id: " + id); // 레코드가 없을 경우 예외 발생
                    }
                }
            }
        } catch (SQLException e) {
            // SQLException 발생 시 예외 다시 던짐
            throw e;
        }
    }


    public void deleteCustomQuestion(Long id) throws SQLException {
        String sql = "DELETE FROM custom_question WHERE question_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id); // ID를 쿼리에 설정
            int affectedRows = pstmt.executeUpdate(); // 삭제 쿼리 실행

            if (affectedRows == 0) {
                throw new NoSuchElementException("No CustomQuestion found with id: " + id); // 삭제할 레코드가 없을 경우 예외 발생
            }
        }
    }

    public List<CustomQuestion> findByUserId(Long userId) throws SQLException {
        String sql = "SELECT * FROM custom_question WHERE user_id = ?";
        List<CustomQuestion> questions = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    questions.add(CustomQuestion.builder()
                        .question_id(rs.getLong("question_id"))
                        .user_id(rs.getLong("user_id"))
                        .question_text(rs.getString("question_text"))
                        .question_type(rs.getString("question_type"))
                        .created_at(rs.getTimestamp("created_at").toLocalDateTime())
                        .updated_at(rs.getTimestamp("updated_at").toLocalDateTime())
                        .build());
                }
            }
        }
        return questions;
    }
}
