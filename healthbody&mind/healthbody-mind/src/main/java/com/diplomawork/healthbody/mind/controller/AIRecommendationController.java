package com.diplomawork.healthbody.mind.controller;

import com.diplomawork.healthbody.mind.dto.UserDynamicDataDto;
import com.diplomawork.healthbody.mind.security.jwt.JwtService;
import com.diplomawork.healthbody.mind.service.AIRecommendationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/recommendation")
@RequiredArgsConstructor
public class AIRecommendationController {
    private final AIRecommendationService aiRecommendationService;
    private final JwtService jwtService;
    @PostMapping("/sendQuestionToDialogflow")
    public ResponseEntity<String> sendQuestionToDialogflow(@RequestBody Map<String, Object> question) {
        String response = aiRecommendationService.sendQuestionToDialogflow(question);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/getDailyRecommendation")
    private ResponseEntity<Map<String, String>> getDailyRecommendation(@RequestHeader("Authorization") String token, @Valid @RequestBody UserDynamicDataDto userDynamicDataDto) {
        if (!token.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid token format.");
        }

        String cleanToken = token.replace("Bearer ", "");
        Integer userId = jwtService.extractUserId(cleanToken);
        String dailyRecommendation = aiRecommendationService.getDailyRecommendation(userId, userDynamicDataDto);
        return ResponseEntity.ok(Collections.singletonMap("dailyRecommendation", dailyRecommendation));
    }
}