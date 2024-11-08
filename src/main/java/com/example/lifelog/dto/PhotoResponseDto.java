package com.example.lifelog.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class PhotoResponseDto {

    @Getter
    @Setter
    @Builder
    public static class PhotoDetailDto {
        private Long photo_id;
        private Long entry_id;
        private String photo_url;
        private LocalDateTime uploaded_at;
    }
}

