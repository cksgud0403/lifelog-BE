package com.example.lifelog.repository;

import com.example.lifelog.domain.Photo;
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
public class PhotoRepository {
    private final DataSource dataSource;

    public Photo save(Photo photo) throws SQLException {
        String sql = "INSERT INTO photo (entry_id, photo_url) VALUES (?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setLong(1, photo.getEntry_id());
            pstmt.setString(2, photo.getPhoto_url());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Long photoId = generatedKeys.getLong(1);
                    return Photo.builder()
                            .photo_id(photoId)
                            .entry_id(photo.getEntry_id())
                            .photo_url(photo.getPhoto_url())
                            .uploaded_at(LocalDateTime.now())
                            .build();
                }
            }
        }
        throw new SQLException("Creating photo failed, no ID obtained.");
    }

    public Photo findById(Long id) throws SQLException {
        String sql = "SELECT * FROM photo WHERE photo_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Photo.builder()
                            .photo_id(rs.getLong("photo_id"))
                            .entry_id(rs.getLong("entry_id"))
                            .photo_url(rs.getString("photo_url"))
                            .uploaded_at(rs.getTimestamp("uploaded_at").toLocalDateTime())
                            .build();
                }
            }
        }
        return null;
    }

    public List<Photo> findAll() throws SQLException {
        String sql = "SELECT * FROM photo";
        List<Photo> photos = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                photos.add(Photo.builder()
                        .photo_id(rs.getLong("photo_id"))
                        .entry_id(rs.getLong("entry_id"))
                        .photo_url(rs.getString("photo_url"))
                        .uploaded_at(rs.getTimestamp("uploaded_at").toLocalDateTime())
                        .build());
            }
        }
        return photos;
    }

    public void updatePhoto(Photo photo) throws SQLException {
        String sql = "UPDATE photo SET photo_url = ? WHERE photo_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, photo.getPhoto_url());
            pstmt.setLong(2, photo.getPhoto_id());
            pstmt.executeUpdate();
        }
    }

    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM photo WHERE photo_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        }
    }
}

