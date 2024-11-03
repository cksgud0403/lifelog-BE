package com.example.lifelog.repository;

import com.example.lifelog.domain.Location;
import com.example.lifelog.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final DataSource dataSource;

    public User save(User user) throws SQLException {
        String sql = "INSERT INTO user (username, password, email, created_at, updated_at) VALUES (?, ?, ?, NOW(), NOW())";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getEmail());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);

                    // 삽입된 레코드를 다시 조회하여 created_at과 updated_at을 가져옵니다.
                    String selectSql = "SELECT created_at, updated_at FROM user WHERE user_id = ?";
                    try (PreparedStatement selectPstmt = conn.prepareStatement(selectSql)) {
                        selectPstmt.setLong(1, id);
                        try (ResultSet rs = selectPstmt.executeQuery()) {
                            if (rs.next()) {
                                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
                                LocalDateTime updatedAt = rs.getTimestamp("updated_at").toLocalDateTime();

                                log.info("createAt = {}", createdAt);
                                log.info("updatedAt = {}", updatedAt);

                                return User.builder()
                                        .username(user.getUsername())
                                        .password(user.getPassword())
                                        .email(user.getEmail())
                                        .created_at(createdAt)
                                        .updated_at(updatedAt)
                                        .build();
                            }
                        }
                    }
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        }

        return user;
    }

    public User findById(Long id) throws SQLException {
        String sql = "SELECT username, password, email FROM user WHERE user_id = ?";

        User user = null;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {

                    user = User.builder()
                            .username(rs.getString("username"))
                            .password(rs.getString("password"))
                            .email(rs.getString("email"))
                            .build();
                }
            }
        }

        return user;
    }

    public User updateUser(Long id, User user) throws SQLException {
        String updateSql = "UPDATE user SET username = ?, email = ?, password = ? WHERE user_id = ?";
        String selectSql = "SELECT username, email, updated_at FROM user WHERE user_id = ?";

        try (Connection conn = dataSource.getConnection()) {
            // 업데이트 쿼리 설정 및 실행
            try (PreparedStatement updatePstmt = conn.prepareStatement(updateSql)) {
                updatePstmt.setString(1, user.getUsername());
                updatePstmt.setString(2, user.getEmail());
                updatePstmt.setString(3, user.getPassword());
                updatePstmt.setLong(4, id);

                int rowsUpdated = updatePstmt.executeUpdate();
                if (rowsUpdated == 0) {
                    throw new NoSuchElementException("User with ID " + id + " not found.");
                }
            }

            // 업데이트 후 최신 정보를 가져오기 위해 SELECT 쿼리 실행
            try (PreparedStatement selectPstmt = conn.prepareStatement(selectSql)) {
                selectPstmt.setLong(1, id);

                try (ResultSet rs = selectPstmt.executeQuery()) {
                    if (rs.next()) {
                        return User.builder()
                                .username(rs.getString("username"))
                                .email(rs.getString("email"))
                                .updated_at(rs.getTimestamp("updated_at").toLocalDateTime())
                                .build();
                    } else {
                        throw new NoSuchElementException("User with ID " + id + " not found after update.");
                    }
                }
            }
        } catch (SQLException e) {
            // SQLException 발생 시 로그를 남기고 예외 다시 던짐
            System.err.println("Database error: " + e.getMessage());
            throw e;
        }
    }


    public void deleteUser(Long id) throws SQLException {
        String sql = "DELETE FROM user WHERE user_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id); // ID를 쿼리에 설정
            int affectedRows = pstmt.executeUpdate(); // 삭제 쿼리 실행

            if (affectedRows == 0) {
                throw new NoSuchElementException("No User found with id: " + id); // 삭제할 레코드가 없을 경우 예외 발생
            }
        }
    }
}