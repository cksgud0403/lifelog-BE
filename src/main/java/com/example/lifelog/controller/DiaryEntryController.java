package com.example.lifelog.controller;

import com.example.lifelog.dto.DiaryEntryRequestDto;
import com.example.lifelog.dto.DiaryEntryResponseDto;
import com.example.lifelog.service.DiaryEntryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/diary-entry")
@RequiredArgsConstructor
@Tag(name= "DiaryEntry", description = "DiaryEntry API")
public class DiaryEntryController {

    private final DiaryEntryService diaryEntryService;

    @GetMapping("/{id}")
    public ResponseEntity<DiaryEntryResponseDto.DiaryEntryDetailDto> getDiaryEntry(@PathVariable("id") Long id) throws SQLException {
        return new ResponseEntity<>(diaryEntryService.getDiaryEntry(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<DiaryEntryResponseDto.DiaryEntryDetailDto>> getAllDiaryEntries() throws SQLException {
        return new ResponseEntity<>(diaryEntryService.getAllDiaryEntries(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DiaryEntryResponseDto.CreateDiaryEntryResultDto> createDiaryEntry(@RequestBody DiaryEntryRequestDto.CreateDiaryEntryDto createDiaryEntryDto) throws SQLException {
        return new ResponseEntity<>(diaryEntryService.createDiaryEntry(createDiaryEntryDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiaryEntryResponseDto.UpdateDiaryEntryResultDto> modifyDiaryEntry(@PathVariable("id") Long id, @RequestBody DiaryEntryRequestDto.UpdateDiaryEntryDto updateDiaryEntryDto) throws SQLException {
        return new ResponseEntity<>(diaryEntryService.modifyDiaryEntry(id, updateDiaryEntryDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeDiaryEntry(@PathVariable("id") Long id) throws SQLException {
        diaryEntryService.removeDiaryEntry(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 유저 아이디와 날짜를 기준으로 일기 조회
     * @param userId 유저 아이디
     * @param date 날짜
     * @return 일기 상세 정보
     * @throws SQLException
     */
    @GetMapping("/user/{userId}/date")
    public ResponseEntity<DiaryEntryResponseDto.DiaryEntryDetailDto> getDiaryEntryByUserAndDate(
            @PathVariable("userId") Long userId,
            @RequestParam("date") String date) throws SQLException {
        LocalDate parsedDate = LocalDate.parse(date);
        DiaryEntryResponseDto.DiaryEntryDetailDto entry = diaryEntryService.getDiaryEntryByUserAndDate(userId, parsedDate);
        
        if (entry == null) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(entry, HttpStatus.OK);
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
