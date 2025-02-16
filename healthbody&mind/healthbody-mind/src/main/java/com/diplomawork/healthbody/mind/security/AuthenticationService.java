package com.diplomawork.healthbody.mind.security;

import com.diplomawork.healthbody.mind.exceptions.EmailValidation;
import com.diplomawork.healthbody.mind.model.User;
import com.diplomawork.healthbody.mind.model.UserProfile;
import com.diplomawork.healthbody.mind.model.enums.ActivityLevel;
import com.diplomawork.healthbody.mind.model.enums.Gender;
import com.diplomawork.healthbody.mind.model.enums.Goal;
import com.diplomawork.healthbody.mind.model.enums.WeeklyGoal;
import com.diplomawork.healthbody.mind.repository.UserProfileRepository;
import com.diplomawork.healthbody.mind.repository.UserRepository;
import com.diplomawork.healthbody.mind.security.jwt.JwtService;
import com.diplomawork.healthbody.mind.security.model.AuthenticationRequest;
import com.diplomawork.healthbody.mind.security.model.RegisterRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public String userLogIn(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
                                                                                    authenticationRequest.getPassword()));

        User currUser = userRepository.findByEmail(authenticationRequest.getEmail()).orElseThrow();
        return jwtService.generateToken(currUser);
    }
    @Transactional
    public String userRegistration(RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new EmailValidation("This email already exists!");
        }
        String hashedPassword = passwordEncoder.encode(registerRequest.getPassword());

        User user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(hashedPassword)
                .build();
        user = userRepository.save(user);

        try {
            UserProfile userProfile = UserProfile.builder()
                    .goal(Goal.valueOf(registerRequest.getGoal()))
                    .activityLevel(ActivityLevel.valueOf(registerRequest.getActivityLevel()))
                    .gender(Gender.valueOf(registerRequest.getGender()))
                    .dateOfBirth(registerRequest.getDateOfBirth())
                    .height(registerRequest.getHeight())
                    .weight(registerRequest.getWeight())
                    .goalWeight(registerRequest.getGoalWeight())
                    .weeklyGoal(WeeklyGoal.valueOf(registerRequest.getWeeklyGoal()))
                    .user(user)
                    .build();
            userProfileRepository.save(userProfile);
        } catch (Exception e) {
            throw new RuntimeException("User's profile information couldn't be saved");
        }

        return jwtService.generateToken(user);
    }
}
