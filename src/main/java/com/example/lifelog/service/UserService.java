package com.example.lifelog.service;

import com.example.lifelog.domain.Location;
import com.example.lifelog.domain.User;
import com.example.lifelog.dto.*;
import com.example.lifelog.repository.LocationRepository;
import com.example.lifelog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {


    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDto.createUserResultDto createUser(UserRequestDto.createUserDto createUserDto) throws SQLException {
        User user = User.builder() //Dto -> User 객체로 변환
                .username(createUserDto.getUsername())
                .password(passwordEncoder.encode(createUserDto.getPassword())) //비밀번호 암호화
                .email(createUserDto.getEmail())
                .build();

        User saveUser = userRepository.save(user); //User 객체를 저장한다. User 객체는 DB의 테이블과 1:1 매핑되는 객체

        return UserResponseDto.createUserResultDto //User 객체 -> Dto
                .builder()
                .username(saveUser.getUsername())
                .email(saveUser.getEmail())
                .created_at(saveUser.getCreated_at())
                .updated_at(saveUser.getUpdated_at())
                .build();
    }

    public UserResponseDto.UserDetailDto getUser(Long id) throws SQLException {
        User user = userRepository.findById(id);


        return UserResponseDto.UserDetailDto
                .builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    @Transactional
    public UserResponseDto.updateUserResultDto modifyUser(Long id, UserRequestDto.updateUserDto updateUserDto) throws SQLException {
        User user = User.builder()
                .username(updateUserDto.getUsername())
                .password(passwordEncoder.encode(updateUserDto.getPassword()))
                .email(updateUserDto.getEmail())
                .build();

        User updatedUser = userRepository.updateUser(id, user);

        return UserResponseDto.updateUserResultDto
                .builder()
                .username(updatedUser.getUsername())
                .email(updatedUser.getEmail())
                .updated_at(updatedUser.getUpdated_at())
                .build();
    }

    @Transactional
    public void removeUser(Long id) throws SQLException {
        userRepository.deleteUser(id);
    }
}