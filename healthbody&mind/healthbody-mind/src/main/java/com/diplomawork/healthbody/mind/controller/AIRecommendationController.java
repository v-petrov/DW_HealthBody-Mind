package com.diplomawork.healthbody.mind.controller;

import com.diplomawork.healthbody.mind.service.AIRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recommendation")
@RequiredArgsConstructor
public class AIRecommendationController {
    private final AIRecommendationService aiRecommendationService;
}
