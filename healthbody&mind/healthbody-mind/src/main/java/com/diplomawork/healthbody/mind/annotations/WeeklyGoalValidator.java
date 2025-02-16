package com.diplomawork.healthbody.mind.annotations;

import com.diplomawork.healthbody.mind.model.enums.Goal;
import com.diplomawork.healthbody.mind.model.enums.WeeklyGoal;
import com.diplomawork.healthbody.mind.security.model.RegisterRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class WeeklyGoalValidator implements ConstraintValidator<ValidWeeklyGoal, RegisterRequest> {

    @Override
    public boolean isValid(RegisterRequest registerRequest, ConstraintValidatorContext context) {
        if (registerRequest == null || registerRequest.getGoal() == null || registerRequest.getWeeklyGoal() == null) {
            return true;
        }

        Goal goal;
        WeeklyGoal weeklyGoal;

        try {
            goal = Goal.valueOf(registerRequest.getGoal());
            weeklyGoal = WeeklyGoal.valueOf(registerRequest.getWeeklyGoal());
        } catch (IllegalArgumentException e) {
            return false;
        }

        if (goal == Goal.LOSE_WEIGHT && (weeklyGoal == WeeklyGoal.GAIN_0_5_KG || weeklyGoal == WeeklyGoal.GAIN_1_KG || weeklyGoal == WeeklyGoal.MAINTAIN)) {
            return false;
        }

        if (goal == Goal.GAIN_WEIGHT && (weeklyGoal == WeeklyGoal.LOSE_0_5_KG || weeklyGoal == WeeklyGoal.LOSE_1_KG || weeklyGoal == WeeklyGoal.MAINTAIN)) {
            return false;
        }
        return goal != Goal.MAINTAIN_WEIGHT || (weeklyGoal != WeeklyGoal.GAIN_0_5_KG && weeklyGoal != WeeklyGoal.GAIN_1_KG && weeklyGoal != WeeklyGoal.LOSE_0_5_KG && weeklyGoal != WeeklyGoal.LOSE_1_KG);
    }
}
