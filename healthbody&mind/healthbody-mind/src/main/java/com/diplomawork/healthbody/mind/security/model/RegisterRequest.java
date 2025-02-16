package com.diplomawork.healthbody.mind.security.model;

import com.diplomawork.healthbody.mind.annotations.ValidGoalWeight;
import com.diplomawork.healthbody.mind.annotations.ValidWeeklyGoal;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ValidGoalWeight
@ValidWeeklyGoal
public class RegisterRequest {

    @NotBlank(message = "First name is required!")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters!")
    @Pattern(regexp = "^(?!.*[-' ].*[-' ])[A-ZÀ-ÖØ-Ý][a-zà-öø-ÿ]*(?:[-' ][A-ZÀ-ÖØ-Ý][a-zà-öø-ÿ]*)?$", message = "Names must begin with an uppercase letter,may\ncontain a hyphen, an apostrophe or a space.")
    private String firstName;

    @NotBlank(message = "Last name is required!")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters!")
    @Pattern(regexp = "^(?!.*[-' ].*[-' ])[A-ZÀ-ÖØ-Ý][a-zà-öø-ÿ]*(?:[-' ][A-ZÀ-ÖØ-Ý][a-zà-öø-ÿ]*)?$", message = "Names must begin with an uppercase letter,may\ncontain a hyphen, an apostrophe or a space.")
    private String lastName;

    @NotBlank(message = "Email is required!")
    @Email(message = "Invalid email format!")
    private String email;

    @NotBlank(message = "Password is required!")
    @Size(min = 8, message = "Password must be at least 8 characters!")
    private String password;



    @NotBlank(message = "Goal is required!")
    private String goal;

    @NotBlank(message = "Activity level is required!")
    private String activityLevel;

    @NotBlank(message = "Gender is required!")
    private String gender;

    @NotNull(message = "Date of birth is required!")
    private LocalDate dateOfBirth;

    @Min(value = 50, message = "Height must be at least 50 cm!")
    @Max(value = 270, message = "Height must be at most 270 cm!")
    @NotNull(message = "Height is required!")
    private int height;

    @DecimalMin(value = "30.0", message = "Weight must be at least 30 kg!")
    @DecimalMax(value = "300.0", message = "Weight must be at most 300 kg!")
    @NotNull(message = "Weight is required!")
    private BigDecimal weight;

    @DecimalMin(value = "30.0", message = "Goal weight must be at least 30 kg!")
    @DecimalMax(value = "300.0", message = "Goal weight must be at most 300 kg!")
    @NotNull(message = "Goal weight is required!")
    private BigDecimal goalWeight;

    @NotNull(message = "Weekly goal is required!")
    private String weeklyGoal;
}