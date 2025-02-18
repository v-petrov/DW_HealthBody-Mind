package com.diplomawork.healthbody.mind.controller;

import com.diplomawork.healthbody.mind.dto.CaloriesDto;
import com.diplomawork.healthbody.mind.security.jwt.JwtService;
import com.diplomawork.healthbody.mind.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;

    @GetMapping("/getCalories")
    private ResponseEntity<CaloriesDto> getUsersCalories(@RequestHeader("Authorization") String token) {
        if (!token.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid token format.");
        }

        String cleanToken = token.replace("Bearer ", "");
        Integer userId = jwtService.extractUserId(cleanToken);
        CaloriesDto caloriesDto= userService.getUserCalories(userId);
        return ResponseEntity.ok(caloriesDto);
    }
}
