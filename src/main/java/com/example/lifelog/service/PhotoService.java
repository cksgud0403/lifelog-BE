package com.example.lifelog.service;

import com.example.lifelog.domain.Photo;
import com.example.lifelog.dto.PhotoRequestDto;
import com.example.lifelog.dto.PhotoResponseDto;
import com.example.lifelog.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PhotoService {

    private final PhotoRepository photoRepository;

    @Transactional
    public PhotoResponseDto.PhotoDetailDto createPhoto(PhotoRequestDto.CreatePhotoDto createPhotoDto) throws SQLException {
        Photo photo = Photo.builder()
                .entry_id(createPhotoDto.getEntry_id())
                .photo_url(createPhotoDto.getPhoto_url())
                .build();

        Photo savedPhoto = photoRepository.save(photo);

        return PhotoResponseDto.PhotoDetailDto.builder()
                .photo_id(savedPhoto.getPhoto_id())
                .entry_id(savedPhoto.getEntry_id())
                .photo_url(savedPhoto.getPhoto_url())
                .uploaded_at(savedPhoto.getUploaded_at())
                .build();
    }

    public PhotoResponseDto.PhotoDetailDto getPhoto(Long id) throws SQLException {
        Photo photo = photoRepository.findById(id);
        return PhotoResponseDto.PhotoDetailDto.builder()
                .photo_id(photo.getPhoto_id())
                .entry_id(photo.getEntry_id())
                .photo_url(photo.getPhoto_url())
                .uploaded_at(photo.getUploaded_at())
                .build();
    }

    public List<PhotoResponseDto.PhotoDetailDto> getAllPhotos() throws SQLException {
        return photoRepository.findAll().stream()
                .map(photo -> PhotoResponseDto.PhotoDetailDto.builder()
                        .photo_id(photo.getPhoto_id())
                        .entry_id(photo.getEntry_id())
                        .photo_url(photo.getPhoto_url())
                        .uploaded_at(photo.getUploaded_at())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public PhotoResponseDto.PhotoDetailDto updatePhoto(Long id, PhotoRequestDto.UpdatePhotoDto updatePhotoDto) throws SQLException {
        Photo existingPhoto = photoRepository.findById(id);
        if (existingPhoto == null) {
            throw new NoSuchElementException("Photo with id " + id + " not found.");
        }

        existingPhoto.setPhoto_url(updatePhotoDto.getPhoto_url());
        photoRepository.updatePhoto(existingPhoto);

        return PhotoResponseDto.PhotoDetailDto.builder()
                .photo_id(existingPhoto.getPhoto_id())
                .entry_id(existingPhoto.getEntry_id())
                .photo_url(existingPhoto.getPhoto_url())
                .uploaded_at(existingPhoto.getUploaded_at())
                .build();
    }

    @Transactional
    public void deletePhoto(Long id) throws SQLException {
        photoRepository.deleteById(id);
    }
}
