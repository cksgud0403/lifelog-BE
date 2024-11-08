package com.example.lifelog.repository;

import com.example.lifelog.domain.DiaryEntry;
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
public class DiaryEntryRepository {
    private final DataSource dataSource;

    public DiaryEntry save(DiaryEntry diaryEntry) throws SQLException {
        String sql = "INSERT INTO diary_entry (user_id, date, content, emotion_score) VALUES (?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setLong(1, diaryEntry.getUser_id());
            pstmt.setDate(2, Date.valueOf(diaryEntry.getDate()));
            pstmt.setString(3, diaryEntry.getContent());
            pstmt.setString(4, diaryEntry.getEmotion_score());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long entryId = generatedKeys.getLong(1);

                    String selectSql = "SELECT created_at, updated_at FROM diary_entry WHERE entry_id = ?";
                    try (PreparedStatement selectPstmt = conn.prepareStatement(selectSql)) {
                        selectPstmt.setLong(1, entryId);
                        try (ResultSet rs = selectPstmt.executeQuery()) {
                            if (rs.next()) {
                                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
                                LocalDateTime updatedAt = rs.getTimestamp("updated_at").toLocalDateTime();

                                log.info("createdAt = {}", createdAt);
                                log.info("updatedAt = {}", updatedAt);

                                return DiaryEntry.builder()
                                        .entry_id(entryId)
                                        .user_id(diaryEntry.getUser_id())
                                        .date(diaryEntry.getDate())
                                        .content(diaryEntry.getContent())
                                        .emotion_score(diaryEntry.getEmotion_score())
                                        .created_at(createdAt)
                                        .updated_at(updatedAt)
                                        .build();
                            }
                        }
                    }
                } else {
                    throw new SQLException("Creating diary entry failed, no ID obtained.");
                }
            }
        }

        return diaryEntry;
    }

    public DiaryEntry findById(Long id) throws SQLException {
        String sql = "SELECT entry_id, user_id, date, content, emotion_score FROM diary_entry WHERE entry_id = ?";

        DiaryEntry diaryEntry = null;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    diaryEntry = DiaryEntry.builder()
                            .entry_id(rs.getLong("entry_id"))
                            .user_id(rs.getLong("user_id"))
                            .date(rs.getDate("date").toLocalDate())
                            .content(rs.getString("content"))
                            .emotion_score(rs.getString("emotion_score"))
                            .build();
                }
            }
        }

        return diaryEntry;
    }

    public DiaryEntry updateDiaryEntry(Long id, DiaryEntry diaryEntry) throws SQLException {
        String updateSql = "UPDATE diary_entry SET content = ?, emotion_score = ? WHERE entry_id = ?";

        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement updatePstmt = conn.prepareStatement(updateSql)) {
                updatePstmt.setString(1, diaryEntry.getContent());
                updatePstmt.setString(2, diaryEntry.getEmotion_score());
                updatePstmt.setLong(3, id);
                int rowsUpdated = updatePstmt.executeUpdate();
                if (rowsUpdated == 0) {
                    throw new NoSuchElementException("No DiaryEntry found with id: " + id);
                }
            }

            String selectSql = "SELECT user_id, date, content, emotion_score, updated_at FROM diary_entry WHERE entry_id = ?";
            try (PreparedStatement selectPstmt = conn.prepareStatement(selectSql)) {
                selectPstmt.setLong(1, id);
                try (ResultSet rs = selectPstmt.executeQuery()) {
                    if (rs.next()) {
                        return DiaryEntry.builder()
                                .entry_id(id)
                                .user_id(rs.getLong("user_id"))
                                .date(rs.getDate("date").toLocalDate())
                                .content(rs.getString("content"))
                                .emotion_score(rs.getString("emotion_score"))
                                .updated_at(rs.getTimestamp("updated_at").toLocalDateTime())
                                .build();
                    } else {
                        throw new NoSuchElementException("No DiaryEntry found with id: " + id);
                    }
                }
            }
        }
    }

    public void deleteDiaryEntry(Long id) throws SQLException {
        String sql = "DELETE FROM diary_entry WHERE entry_id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new NoSuchElementException("No DiaryEntry found with id: " + id);
            }
        }
    }

    public List<DiaryEntry> findAll() throws SQLException {
        String sql = "SELECT entry_id, user_id, date, content, emotion_score FROM diary_entry";
        List<DiaryEntry> diaryEntries = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                DiaryEntry diaryEntry = DiaryEntry.builder()
                        .entry_id(rs.getLong("entry_id"))
                        .user_id(rs.getLong("user_id"))
                        .date(rs.getDate("date").toLocalDate())
                        .content(rs.getString("content"))
                        .emotion_score(rs.getString("emotion_score"))
                        .build();
                diaryEntries.add(diaryEntry);
            }
        }

        return diaryEntries;
    }
}

