package com.example.lifelog.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Photo {
    private Long photo_id;
    private Long entry_id; // DiaryEntry와 연관된 ID
    private String photo_url;
    private LocalDateTime uploaded_at;
}
