package com.diplomawork.healthbody.mind.controller;

import com.diplomawork.healthbody.mind.dto.CaloriesDto;
import com.diplomawork.healthbody.mind.dto.UserDataDto;
import com.diplomawork.healthbody.mind.dto.UserProfileDto;
import com.diplomawork.healthbody.mind.security.jwt.JwtService;
import com.diplomawork.healthbody.mind.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;
    private static final String IMAGES_DIR = "images/";

    @GetMapping("/getCalories")
    private ResponseEntity<CaloriesDto> getUsersCalories(@RequestHeader("Authorization") String token) {
        if (!token.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid token format.");
        }

        String cleanToken = token.replace("Bearer ", "");
        Integer userId = jwtService.extractUserId(cleanToken);
        CaloriesDto caloriesDto = userService.getUserCalories(userId);
        return ResponseEntity.ok(caloriesDto);
    }
    @GetMapping("/getUserData")
    private ResponseEntity<UserDataDto> getUserData(@RequestHeader("Authorization") String token) {
        if (!token.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid token format.");
        }

        String cleanToken = token.replace("Bearer ", "");
        Integer userId = jwtService.extractUserId(cleanToken);
        UserDataDto userDataDto = userService.getUserData(userId);
        return ResponseEntity.ok(userDataDto);
    }
    @PutMapping("/saveUserCalories")
    private ResponseEntity<Map<String, String>> saveUserCalories(@RequestHeader("Authorization") String token, @Valid @RequestBody CaloriesDto caloriesDto) {
        if (!token.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid token format.");
        }

        String cleanToken = token.replace("Bearer ", "");
        Integer userId = jwtService.extractUserId(cleanToken);
        userService.saveUserCalories(userId, caloriesDto);
        return ResponseEntity.ok(Collections.singletonMap("validation", "Calories are saved successfully!"));
    }
    @PutMapping("/saveUserProfile")
    private ResponseEntity<CaloriesDto> saveUserProfile(@RequestHeader("Authorization") String token, @Valid @RequestBody UserProfileDto userProfileDto) {
        if (!token.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid token format.");
        }

        String cleanToken = token.replace("Bearer ", "");
        Integer userId = jwtService.extractUserId(cleanToken);
        CaloriesDto updatedUserCalories = userService.saveUserProfile(userId, userProfileDto);

        return ResponseEntity.ok(updatedUserCalories);
    }
    @PutMapping("/updateProfilePicture")
    private ResponseEntity<String> uploadProfilePicture(@RequestHeader("Authorization") String token, @RequestParam("file") MultipartFile file) {
        if (!token.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid token format.");
        }

        String cleanToken = token.replace("Bearer ", "");
        Integer userId = jwtService.extractUserId(cleanToken);
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("File is empty!");
            }
            Files.createDirectories(Paths.get(IMAGES_DIR));
            String filename = "profilePictureOfUserWithId_" + userId + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(IMAGES_DIR, filename);
            Files.write(filePath, file.getBytes());
            String imageUrl = "/images/" + filename;
            userService.updateProfilePicture(userId, imageUrl);

            return ResponseEntity.ok(imageUrl);
        } catch (Exception e) {
            throw new RuntimeException("Error with processing the image!");
        }
    }
    @GetMapping("/getProfilePicture")
    private ResponseEntity<String> getProfilePicture(@RequestHeader("Authorization") String token) {
        if (!token.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid token format.");
        }

        String cleanToken = token.replace("Bearer ", "");
        Integer userId = jwtService.extractUserId(cleanToken);
        String imageUrl = userService.getProfilePicture(userId);

        return ResponseEntity.ok(imageUrl);
    }
}
