package com.diplomawork.healthbody.mind.controller;

import com.diplomawork.healthbody.mind.dto.FoodDto;
import com.diplomawork.healthbody.mind.dto.FoodIntakeDto;
import com.diplomawork.healthbody.mind.dto.UpdateFoodIntakeDto;
import com.diplomawork.healthbody.mind.security.jwt.JwtService;
import com.diplomawork.healthbody.mind.service.FoodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/food")
@RequiredArgsConstructor
public class FoodController {
    private final FoodService foodService;
    private final JwtService jwtService;
    @GetMapping("/searchByName")
    private ResponseEntity<Map<String, Object>> getFoodByName(@RequestParam("foodName") String foodName) {
        List<FoodDto> foundFoods = foodService.searchFoodByName(foodName);
        return ResponseEntity.ok(Collections.singletonMap("data", foundFoods));
    }
    @PostMapping("/saveFoodIntake")
    private ResponseEntity<Map<String, Long>> saveFoodIntake(@RequestHeader("Authorization") String token, @Valid @RequestBody FoodIntakeDto foodIntakeDto) {
        if (!token.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid token format.");
        }

        String cleanToken = token.replace("Bearer ", "");
        Integer userId = jwtService.extractUserId(cleanToken);
        Long foodIntakeId = foodService.saveFoodIntake(userId, foodIntakeDto);
        return ResponseEntity.ok(Collections.singletonMap("id", foodIntakeId));
    }
    @GetMapping("/getFoodIntakesByDate")
    private ResponseEntity<List<FoodIntakeDto>> getFoodIntakesByDate(@RequestHeader("Authorization") String token, @RequestParam("date") LocalDate date) {
        if (!token.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid token format.");
        }

        String cleanToken = token.replace("Bearer ", "");
        Integer userId = jwtService.extractUserId(cleanToken);
        List<FoodIntakeDto> foodIntakeDtoList = foodService.getFoodIntakesByDate(userId, date);
        return ResponseEntity.ok(foodIntakeDtoList);
    }
    @DeleteMapping("/deleteFoodIntake/{id}")
    private ResponseEntity<Map<String, String>> deleteFoodIntake(@PathVariable Long id) {
        foodService.deleteFoodIntake(id);
        return ResponseEntity.ok(Collections.singletonMap("validation", "Food intake has been deleted successfully!"));
    }
    @PutMapping("/updateFoodIntake")
    private ResponseEntity<Map<String, String>> updateFoodIntake(@Valid @RequestBody UpdateFoodIntakeDto updateFoodIntakeDto) {
        foodService.updateFoodIntake(updateFoodIntakeDto);
        return ResponseEntity.ok(Collections.singletonMap("validation", "Food intake has been updated successfully!"));
    }
}