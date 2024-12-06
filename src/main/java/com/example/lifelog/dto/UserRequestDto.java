package com.example.lifelog.dto;

import lombok.Getter;
import lombok.Setter;

public class UserRequestDto {


    @Getter
    @Setter
    public static class createUserDto {
        String username;
        String password;
        String email;
    }


    @Getter
    @Setter
    public static class updateUserDto {
        String username;
        String password;
        String email;
    }
}
