package com.example.lifelog.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DiaryEntryResponseDto {

    @Getter
    @Setter
    @Builder
    public static class CreateDiaryEntryResultDto {
        Long entry_id;
        Long user_id;
        LocalDate date;
        String content;
        String emotion_score;
        LocalDateTime created_at;
        LocalDateTime updated_at;
    }

    @Getter
    @Setter
    @Builder
    public static class DiaryEntryDetailDto {
        Long entry_id;
        Long user_id;
        LocalDate date;
        String content;
        String emotion_score;
    }

    @Getter
    @Setter
    @Builder
    public static class UpdateDiaryEntryResultDto {
        String content;
        String emotion_score;
        LocalDateTime updated_at;
    }
}
