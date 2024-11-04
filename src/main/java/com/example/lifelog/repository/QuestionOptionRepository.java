package com.example.lifelog.repository;

import com.example.lifelog.domain.CustomQuestion;
import com.example.lifelog.domain.QuestionOption;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Repository
@RequiredArgsConstructor
@Slf4j
public class QuestionOptionRepository {
    private final DataSource dataSource;

    public QuestionOption save(QuestionOption questionOption) throws SQLException {
        String sql = "INSERT INTO question_option (question_id, option_text) VALUES (?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setLong(1, questionOption.getQuestion_id());
            pstmt.setString(2, questionOption.getOption_text());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long optionId = generatedKeys.getLong(1);

                    return QuestionOption.builder()
                            .option_id(optionId)
                            .question_id(questionOption.getQuestion_id())
                            .option_text(questionOption.getOption_text())
                            .build();
                } else {
                    throw new SQLException("Creating question option failed, no ID obtained.");
                }
            }
        }
    }

    public QuestionOption findById(Long id) throws SQLException {
        String sql = "SELECT option_id, question_id, option_text FROM question_option WHERE option_id = ?";

        QuestionOption questionOption = null;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    questionOption = QuestionOption.builder()
                            .option_id(rs.getLong("option_id"))
                            .question_id(rs.getLong("question_id"))
                            .option_text(rs.getString("option_text"))
                            .build();
                }
            }
        }

        return questionOption;
    }

    public QuestionOption updateQuestionOption(Long id, QuestionOption questionOption) throws SQLException {
        String updateSql = "UPDATE question_option SET option_text = ? WHERE option_id = ?";

        try (Connection conn = dataSource.getConnection()) {
            // Execute the update query
            try (PreparedStatement updatePstmt = conn.prepareStatement(updateSql)) {
                updatePstmt.setString(1, questionOption.getOption_text());
                updatePstmt.setLong(2, id);

                int rowsUpdated = updatePstmt.executeUpdate();
                if (rowsUpdated == 0) {
                    throw new NoSuchElementException("No QuestionOption found with id: " + id); // Throw exception if no record is updated
                }
            }

            String selectSql = "SELECT question_id, option_text FROM question_option WHERE option_id = ?";

            // Execute the select query to retrieve the updated record
            try (PreparedStatement selectPstmt = conn.prepareStatement(selectSql)) {
                selectPstmt.setLong(1, id);

                try (ResultSet rs = selectPstmt.executeQuery()) {
                    if (rs.next()) {
                        return QuestionOption.builder()
                                .option_id(id)
                                .question_id(rs.getLong("question_id"))
                                .option_text(rs.getString("option_text"))
                                .build();
                    } else {
                        throw new NoSuchElementException("No QuestionOption found with id: " + id); // Throw exception if no record is found
                    }
                }
            }
        } catch (SQLException e) {
            // Re-throw SQLException if it occurs
            throw e;
        }
    }

    public void deleteQuestionOption(Long id) throws SQLException {
        String sql = "DELETE FROM question_option WHERE option_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id); // Set ID in the query
            int affectedRows = pstmt.executeUpdate(); // Execute delete query

            if (affectedRows == 0) {
                throw new NoSuchElementException("No QuestionOption found with id: " + id); // Throw exception if no record is deleted
            }
        }
    }
}
