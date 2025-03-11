package com.diplomawork.healthbody.mind.controller;

import com.diplomawork.healthbody.mind.dto.CardioDto;
import com.diplomawork.healthbody.mind.dto.ExerciseDataDto;
import com.diplomawork.healthbody.mind.dto.LiftingDto;
import com.diplomawork.healthbody.mind.security.jwt.JwtService;
import com.diplomawork.healthbody.mind.service.ExerciseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/exercise")
@RequiredArgsConstructor
public class ExerciseController {
    private final ExerciseService exerciseService;
    private final JwtService jwtService;
    @PostMapping("/saveLiftingData")
    private ResponseEntity<Map<String, String>> saveLiftingData(@RequestHeader("Authorization") String token, @Valid @RequestBody LiftingDto liftingDto) {
        if (!token.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid token format.");
        }

        String cleanToken = token.replace("Bearer ", "");
        Integer userId = jwtService.extractUserId(cleanToken);
        exerciseService.saveLiftingData(userId, liftingDto);
        return ResponseEntity.ok(Collections.singletonMap("validation", "Lifting data has been saved successfully!"));
    }
    @PostMapping("/saveCardioData")
    private ResponseEntity<Map<String, String>> saveCardioData(@RequestHeader("Authorization") String token, @Valid @RequestBody CardioDto cardioDto) {
        if (!token.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid token format.");
        }

        String cleanToken = token.replace("Bearer ", "");
        Integer userId = jwtService.extractUserId(cleanToken);
        exerciseService.saveCardioData(userId, cardioDto);
        return ResponseEntity.ok(Collections.singletonMap("validation", "Cardio data has been saved successfully!"));
    }
    @GetMapping("/getExerciseDataByDate")
    private ResponseEntity<ExerciseDataDto> getExerciseDataByDate(@RequestHeader("Authorization") String token, @RequestParam("date") LocalDate date) {
        if (!token.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid token format.");
        }

        String cleanToken = token.replace("Bearer ", "");
        Integer userId = jwtService.extractUserId(cleanToken);
        ExerciseDataDto exerciseDataDto = exerciseService.getExerciseDataByDate(userId, date);
        return  ResponseEntity.ok(exerciseDataDto);
    }
}
