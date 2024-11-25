package com.example.lifelog.controller;

import com.example.lifelog.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
@Slf4j
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping
    public ResponseEntity<Map<String, String>> getStatistics(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam String type
    ) {
        try {
            if (type.equalsIgnoreCase("emotion")) {
                Map<String, String> emotionStatistics = statisticsService.calculateEmotionStatistics(year, month);
                return ResponseEntity.ok(emotionStatistics);
            } else {
                throw new IllegalArgumentException("Invalid type: " + type);
            }
        } catch (Exception e) {
            log.error("Error fetching statistics", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}



