package com.example.lifelog.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class UserResponseDto {

    @Getter
    @Setter
    @Builder
    public static class createUserResultDto {
        String username;
        String email;
        LocalDateTime created_at;
        LocalDateTime updated_at;
    }


    @Getter
    @Setter
    @Builder
    public static class UserDetailDto {
        String username;
        String email;
    }

    @Getter
    @Setter
    @Builder
    public static class updateUserResultDto {
        String username;
        String email;
        LocalDateTime updated_at;
    }
}
