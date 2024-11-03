package com.example.lifelog.controller;


import com.example.lifelog.domain.Location;
import com.example.lifelog.domain.User;
import com.example.lifelog.dto.*;
import com.example.lifelog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto.UserDetailDto> getUser(@PathVariable Long id) throws SQLException {
        return new ResponseEntity<>(userService.getUser(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserResponseDto.createUserResultDto> createUser(@RequestBody UserRequestDto.createUserDto createUserDto) throws SQLException {
        return new ResponseEntity<>(userService.createUser(createUserDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto.updateUserResultDto> modifyUser(@PathVariable Long id, @RequestBody UserRequestDto.updateUserDto updateUserDto) throws SQLException {
        return new ResponseEntity<>(userService.modifyUser(id, updateUserDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeUser(@PathVariable Long id) throws SQLException {
        userService.removeUser(id);
        return ResponseEntity.noContent().build();
    }
}
