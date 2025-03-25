package com.diplomawork.healthbody.mind.service;

import com.diplomawork.healthbody.mind.dto.UserDynamicDataDto;
import com.diplomawork.healthbody.mind.model.Recommendation;
import com.diplomawork.healthbody.mind.model.User;
import com.diplomawork.healthbody.mind.model.UserRecommendation;
import com.diplomawork.healthbody.mind.model.enums.Goal;
import com.diplomawork.healthbody.mind.repository.RecommendationRepository;
import com.diplomawork.healthbody.mind.repository.UserRecommendationRepository;
import com.diplomawork.healthbody.mind.repository.UserRepository;
import com.diplomawork.healthbody.mind.util.DialogflowTokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AIRecommendationService {
    private final RecommendationRepository recommendationRepository;
    private final UserRecommendationRepository userRecommendationRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final DialogflowTokenManager dialogflowTokenManager;

    @Value("${dialogflow.project.id}")
    private String projectId;

    private static final String DIALOGFLOW_URL = "https://dialogflow.googleapis.com/v2/projects/%s/agent/sessions/test-session-123456:detectIntent";
    public String sendQuestionToDialogflow(Map<String, Object> question) {
        String url = String.format(DIALOGFLOW_URL, projectId);

        try {
            String token = dialogflowTokenManager.getAccessToken();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(question, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            return response.getBody() != null ? response.getBody() : "No response from Dialogflow.";

        } catch (IOException e) {
            throw new RuntimeException("Failed to retrieve Dialogflow token: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error connecting to Dialogflow: " + e.getMessage(), e);
        }
    }
    public String getDailyRecommendation(Integer userId, UserDynamicDataDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User couldn't be found"));

        Optional<UserRecommendation> existingRecommendation = userRecommendationRepository.findByUserAndDate(user, dto.getDate());
        if (existingRecommendation.isPresent()) {
            return existingRecommendation.get().getFilledRecommendation();
        } else {
            if (!dto.getDate().isEqual(LocalDate.now())) {
                return "";
            }
        }

        Goal userGoal = Goal.valueOf(dto.getGoal());
        List<Recommendation> templates = recommendationRepository.findByGoal(userGoal);
        if (templates.isEmpty()) {
            throw new RuntimeException("No templates found for goal: " + userGoal);
        }
        int index = (dto.getDate().getDayOfYear() + userId) % templates.size();
        String unFilledTemplate = templates.get(index).getRecommendation();
        String filledTemplate = fillTemplate(unFilledTemplate, dto);

        UserRecommendation userRec = UserRecommendation.builder()
                .user(user)
                .date(dto.getDate())
                .filledRecommendation(filledTemplate)
                .build();
        userRecommendationRepository.save(userRec);

        return filledTemplate;
    }
    private String fillTemplate(String template, UserDynamicDataDto userDynamicDataDto) {
        int estimatedWeeks = estimateWeeksToGoal(userDynamicDataDto.getWeight(), userDynamicDataDto.getGoalWeight(),
                userDynamicDataDto.getWeeklyGoal());

        return template
                .replace("{user.goal}", userDynamicDataDto.getGoal().replace("_", " ").toLowerCase())
                .replace("{user.weeklyGoal}", userDynamicDataDto.getWeeklyGoal().replace("_", " ").toLowerCase())
                .replace("{user.weight}", String.format("%.1f", userDynamicDataDto.getWeight()))
                .replace("{user.goalWeight}", String.format("%.1f", userDynamicDataDto.getGoalWeight()))
                .replace("{user.activityLevel}", userDynamicDataDto.getActivityLevel().replace("_", " ").toLowerCase())
                .replace("{user.steps}", String.valueOf(userDynamicDataDto.getSteps()))
                .replace("{user.calories}", String.valueOf(userDynamicDataDto.getCalories()))
                .replace("{user.protein}", String.valueOf(userDynamicDataDto.getProtein()))
                .replace("{user.carbs}", String.valueOf(userDynamicDataDto.getCarbs()))
                .replace("{user.fats}", String.valueOf(userDynamicDataDto.getFats()))
                .replace("{user.water}", String.format("%.1f", userDynamicDataDto.getWater()))
                .replace("{estimatedWeeks}", String.valueOf(estimatedWeeks));
    }
    private int estimateWeeksToGoal(double weight, double goalWeight, String weeklyGoal) {
        return switch (weeklyGoal) {
            case "LOSE_1_KG", "GAIN_1_KG" -> (int) Math.ceil(Math.abs(weight - goalWeight));
            case "LOSE_0_5_KG", "GAIN_0_5_KG" -> (int) Math.ceil(Math.abs(weight - goalWeight) / 0.5);
            default -> 0;
        };
    }
}
