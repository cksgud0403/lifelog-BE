package com.example.lifelog.service;

import com.example.lifelog.domain.DiaryEntry;
import com.example.lifelog.dto.DiaryEntryRequestDto;
import com.example.lifelog.dto.DiaryEntryResponseDto;
import com.example.lifelog.repository.DiaryEntryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DiaryEntryService {

    private final DiaryEntryRepository diaryEntryRepository;

    @Transactional
    public DiaryEntryResponseDto.CreateDiaryEntryResultDto createDiaryEntry(DiaryEntryRequestDto.CreateDiaryEntryDto createDiaryEntryDto) throws SQLException {
        DiaryEntry diaryEntry = DiaryEntry.builder()
                .user_id(createDiaryEntryDto.getUser_id())
                .date(createDiaryEntryDto.getDate())
                .content(createDiaryEntryDto.getContent())
                .emotion_score(createDiaryEntryDto.getEmotion_score())
                .build();

        DiaryEntry savedDiaryEntry = diaryEntryRepository.save(diaryEntry);

        return DiaryEntryResponseDto.CreateDiaryEntryResultDto.builder()
                .entry_id(savedDiaryEntry.getEntry_id())
                .user_id(savedDiaryEntry.getUser_id())
                .date(savedDiaryEntry.getDate())
                .content(savedDiaryEntry.getContent())
                .emotion_score(savedDiaryEntry.getEmotion_score())
                .created_at(savedDiaryEntry.getCreated_at())
                .updated_at(savedDiaryEntry.getUpdated_at())
                .build();
    }

    public DiaryEntryResponseDto.DiaryEntryDetailDto getDiaryEntry(Long id) throws SQLException {
        DiaryEntry diaryEntry = diaryEntryRepository.findById(id);

        return DiaryEntryResponseDto.DiaryEntryDetailDto.builder()
                .entry_id(diaryEntry.getEntry_id())
                .user_id(diaryEntry.getUser_id())
                .date(diaryEntry.getDate())
                .content(diaryEntry.getContent())
                .emotion_score(diaryEntry.getEmotion_score())
                .build();
    }

    @Transactional
    public DiaryEntryResponseDto.UpdateDiaryEntryResultDto modifyDiaryEntry(Long id, DiaryEntryRequestDto.UpdateDiaryEntryDto updateDiaryEntryDto) throws SQLException {
        DiaryEntry diaryEntry = DiaryEntry.builder()
                .content(updateDiaryEntryDto.getContent())
                .emotion_score(updateDiaryEntryDto.getEmotion_score())
                .build();

        DiaryEntry updatedDiaryEntry = diaryEntryRepository.updateDiaryEntry(id, diaryEntry);

        return DiaryEntryResponseDto.UpdateDiaryEntryResultDto.builder()
                .content(updatedDiaryEntry.getContent())
                .emotion_score(updatedDiaryEntry.getEmotion_score())
                .updated_at(updatedDiaryEntry.getUpdated_at())
                .build();
    }

    @Transactional
    public void removeDiaryEntry(Long id) throws SQLException {
        diaryEntryRepository.deleteDiaryEntry(id);
    }

    public List<DiaryEntryResponseDto.DiaryEntryDetailDto> getAllDiaryEntries() throws SQLException {
        List<DiaryEntry> diaryEntries = diaryEntryRepository.findAll();
        return diaryEntries.stream()
                .map(entry -> DiaryEntryResponseDto.DiaryEntryDetailDto.builder()
                        .entry_id(entry.getEntry_id())
                        .user_id(entry.getUser_id())
                        .date(entry.getDate())
                        .content(entry.getContent())
                        .emotion_score(entry.getEmotion_score())
                        .build())
                .collect(Collectors.toList());
    }

    public DiaryEntryResponseDto.DiaryEntryDetailDto getDiaryEntryByUserAndDate(Long userId, LocalDate date) throws SQLException {
        DiaryEntry diaryEntry = diaryEntryRepository.findByUserIdAndDate(userId, date);
        
        if (diaryEntry == null) {
            return null;
        }

        return DiaryEntryResponseDto.DiaryEntryDetailDto.builder()
                .entry_id(diaryEntry.getEntry_id())
                .user_id(diaryEntry.getUser_id())
                .date(diaryEntry.getDate())
                .content(diaryEntry.getContent())
                .emotion_score(diaryEntry.getEmotion_score())
                .build();
    }
}