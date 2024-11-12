package com.example.lifelog.controller;

import com.example.lifelog.dto.PhotoRequestDto;
import com.example.lifelog.dto.PhotoResponseDto;
import com.example.lifelog.service.PhotoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/photo")
@RequiredArgsConstructor
@Tag(name= "Photo", description = "Photo API")
public class PhotoController {

    private final PhotoService photoService;

    @PostMapping
    public ResponseEntity<PhotoResponseDto.PhotoDetailDto> createPhoto(@RequestBody PhotoRequestDto.CreatePhotoDto createPhotoDto) throws SQLException {
        return new ResponseEntity<>(photoService.createPhoto(createPhotoDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhotoResponseDto.PhotoDetailDto> getPhoto(@PathVariable Long id) throws SQLException {
        return new ResponseEntity<>(photoService.getPhoto(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<PhotoResponseDto.PhotoDetailDto>> getAllPhotos() throws SQLException {
        return new ResponseEntity<>(photoService.getAllPhotos(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PhotoResponseDto.PhotoDetailDto> updatePhoto(@PathVariable Long id, @RequestBody PhotoRequestDto.UpdatePhotoDto updatePhotoDto) throws SQLException {
        return new ResponseEntity<>(photoService.updatePhoto(id, updatePhotoDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhoto(@PathVariable Long id) throws SQLException {
        photoService.deletePhoto(id);
        return ResponseEntity.noContent().build();
    }
}

