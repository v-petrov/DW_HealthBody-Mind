package com.diplomawork.healthbody.mind.annotations;

import com.diplomawork.healthbody.mind.security.model.RegisterRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class GoalWeightValidator implements ConstraintValidator<ValidGoalWeight, RegisterRequest> {

    @Override
    public boolean isValid(RegisterRequest registerRequest, ConstraintValidatorContext context) {
        if (registerRequest.getWeight() == null || registerRequest.getGoalWeight() == null || registerRequest.getGoal() == null) {
            return true;
        }

        BigDecimal weight = registerRequest.getWeight();
        BigDecimal goalWeight = registerRequest.getGoalWeight();
        String goal = registerRequest.getGoal();

        return switch (goal) {
            case "LOSE_WEIGHT" -> goalWeight.compareTo(weight) < 0;
            case "GAIN_WEIGHT" -> goalWeight.compareTo(weight) > 0;
            case "MAINTAIN_WEIGHT" ->
                    goalWeight.compareTo(weight) == 0;
            default -> false;
        };
    }
}

