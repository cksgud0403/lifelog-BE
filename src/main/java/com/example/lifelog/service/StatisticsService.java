package com.example.lifelog.service;

import com.example.lifelog.domain.DiaryEntry;
import com.example.lifelog.repository.DiaryEntryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticsService {

    private final DiaryEntryRepository diaryEntryRepository;

    public Map<String, String> calculateEmotionStatistics(int year, int month) throws SQLException {
        // 데이터 필터링
        List<DiaryEntry> filteredEntries = diaryEntryRepository.findByYearAndMonth(year, month);

        // 데이터가 없을 경우 0% 반환
        if (filteredEntries.isEmpty()) {
            Map<String, String> emptyStatistics = new LinkedHashMap<>();
            for (int i = 1; i <= 5; i++) {
                emptyStatistics.put(String.valueOf(i), "0%");
            }
            return emptyStatistics;
        }

        // 감정 점수 배열 초기화 (0~5)
        int[] emotionScoreCounts = new int[6];
        int totalEntries = filteredEntries.size();

        // 데이터 순회하여 점수 배열 채우기
        for (DiaryEntry entry : filteredEntries) {
            int score = Integer.parseInt(entry.getEmotion_score()); // 감정 점수 파싱
            emotionScoreCounts[score]++;
        }

        // 퍼센트 계산 (소수점 제거 전)
        int[] percentages = new int[6];
        int totalPercentage = 0;

        for (int i = 1; i <= 5; i++) { // 1부터 5까지 퍼센트 계산
            double percentage = ((double) emotionScoreCounts[i] / totalEntries) * 100;
            percentages[i] = (int) Math.floor(percentage); // 소수점 제거
            totalPercentage += percentages[i]; // 합계 계산
        }

        // 합계가 100%가 되도록 조정
        int difference = 100 - totalPercentage;
        if (difference != 0) {
            adjustPercentages(percentages, difference);
        }

        // 결과를 Map에 저장
        Map<String, String> emotionPercentages = new LinkedHashMap<>();
        for (int i = 1; i <= 5; i++) {
            emotionPercentages.put(String.valueOf(i), percentages[i] + "%");
        }

        return emotionPercentages;
    }

    // 퍼센트 값을 조정하여 합계를 100%로 맞춤
    private void adjustPercentages(int[] percentages, int difference) {
        // 정렬된 인덱스 목록 (큰 값 우선)
        List<Integer> indices = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            indices.add(i);
        }
        indices.sort((a, b) -> Integer.compare(percentages[b], percentages[a]));

        // 부족하거나 초과된 값 조정
        for (int i = 0; i < Math.abs(difference); i++) {
            int index = indices.get(i % indices.size());
            percentages[index] += (difference > 0 ? 1 : -1);
        }
    }
}




