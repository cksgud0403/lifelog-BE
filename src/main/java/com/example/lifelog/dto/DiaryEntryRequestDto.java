package com.example.lifelog.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class DiaryEntryRequestDto {

    @Getter
    @Setter
    public static class CreateDiaryEntryDto {
        Long user_id;
        LocalDate date;
        String content;
        String emotion_score;
    }

    @Getter
    @Setter
    public static class UpdateDiaryEntryDto {
        String content;
        String emotion_score;
    }
}

