package com.example.lifelog.dto;

import lombok.Getter;
import lombok.Setter;

public class PhotoRequestDto {

    @Getter
    @Setter
    public static class CreatePhotoDto {
        private Long entry_id;
        private String photo_url;
    }

    @Getter
    @Setter
    public static class UpdatePhotoDto {
        private String photo_url;
    }
}

