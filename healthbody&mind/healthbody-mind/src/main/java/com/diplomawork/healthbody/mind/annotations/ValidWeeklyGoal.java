package com.diplomawork.healthbody.mind.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = WeeklyGoalValidator.class)
public @interface ValidWeeklyGoal {
    String message() default "Weekly goal should be matched to your main goal!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}