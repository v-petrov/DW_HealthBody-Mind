package com.diplomawork.healthbody.mind.service;

import com.diplomawork.healthbody.mind.dto.ChartTypeDto;
import com.diplomawork.healthbody.mind.repository.ExerciseRepository;
import com.diplomawork.healthbody.mind.repository.FoodIntakeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChartService {
    private final FoodIntakeRepository foodIntakeRepository;
    private final ExerciseRepository exerciseRepository;

    public Map<String, Integer> getChartDataForAPeriod(Integer userId, int days, ChartTypeDto chartType) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days - 1);

        List<Map<String, Object>> listData = listDataForChart(userId, startDate, endDate, chartType);
        Map<String, Integer> mapData = mapDataForChart(listData, chartType);

        return fillMissingDatesWithZero(startDate, endDate, mapData);
    }

    private List<Map<String, Object>> listDataForChart(Integer userId, LocalDate startDate, LocalDate endDate, ChartTypeDto chartType) {
        return switch (chartType) {
            case CALORIES_INTAKE -> foodIntakeRepository.getCaloriesIntakeByDate(userId, startDate, endDate);
            case BURNED_CALORIES -> exerciseRepository.getBurnedCaloriesByDate(userId, startDate, endDate);
            case CARBS_INTAKE -> foodIntakeRepository.getCarbsIntakeByDate(userId, startDate, endDate);
            case FAT_INTAKE -> foodIntakeRepository.getFatIntakeByDate(userId, startDate, endDate);
            case PROTEIN_INTAKE -> foodIntakeRepository.getProteinIntakeByDate(userId, startDate, endDate);
        };
    }

    private Map<String, Integer> mapDataForChart(List<Map<String, Object>> rawData, ChartTypeDto chartType) {
        String valueKey = switch (chartType) {
            case CALORIES_INTAKE -> "caloriesIntake";
            case BURNED_CALORIES -> "burnedCalories";
            case CARBS_INTAKE -> "carbsIntake";
            case FAT_INTAKE -> "fatIntake";
            case PROTEIN_INTAKE -> "proteinIntake";
        };

        return rawData.stream()
                .sorted(Comparator.comparing(entry -> LocalDate.parse(entry.get("date").toString())))
                .collect(Collectors.toMap(
                        entry -> entry.get("date").toString(),
                        entry -> ((Number) entry.get(valueKey)).intValue(),
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
    }

    private Map<String, Integer> fillMissingDatesWithZero(LocalDate startDate, LocalDate endDate, Map<String, Integer> existingData) {
        Map<String, Integer> completeData = new LinkedHashMap<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            String dateStr = date.toString();
            completeData.put(dateStr, existingData.getOrDefault(dateStr, 0));
        }
        return completeData;
    }
}
