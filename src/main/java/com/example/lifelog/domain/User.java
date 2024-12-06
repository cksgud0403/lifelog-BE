package com.example.lifelog.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
public class User {
    Long user_id;
    String username;
    String password;
    String email;
    LocalDateTime created_at;
    LocalDateTime updated_at;
}
