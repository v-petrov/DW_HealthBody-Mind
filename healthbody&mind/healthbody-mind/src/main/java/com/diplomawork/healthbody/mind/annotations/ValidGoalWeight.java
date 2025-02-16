package com.diplomawork.healthbody.mind.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GoalWeightValidator.class)
public @interface ValidGoalWeight {
    String message() default "Goal weight should be matched to your goal and current weight!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}