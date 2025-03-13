package com.diplomawork.healthbody.mind.controller;

import com.diplomawork.healthbody.mind.dto.ChartTypeDto;
import com.diplomawork.healthbody.mind.security.jwt.JwtService;
import com.diplomawork.healthbody.mind.service.ChartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/chart")
@RequiredArgsConstructor
public class ChartController {
    private final ChartService chartService;
    private final JwtService jwtService;
    @GetMapping("/getChartDataForAPeriod")
    private ResponseEntity<Map<String, Integer>> getChartDataForAPeriod(@RequestHeader("Authorization") String token, @RequestParam("days") int days, @RequestParam("chartType") ChartTypeDto chartType) {
        if (!token.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid token format.");
        }
        String cleanToken = token.replace("Bearer ", "");
        Integer userId = jwtService.extractUserId(cleanToken);
        Map<String, Integer> mapResult = chartService.getChartDataForAPeriod(userId, days, chartType);
        return ResponseEntity.ok(mapResult);
    }
}