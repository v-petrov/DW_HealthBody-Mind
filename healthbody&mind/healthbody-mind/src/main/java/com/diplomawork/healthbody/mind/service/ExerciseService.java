package com.diplomawork.healthbody.mind.service;

import com.diplomawork.healthbody.mind.dto.CardioDto;
import com.diplomawork.healthbody.mind.dto.ExerciseDataDto;
import com.diplomawork.healthbody.mind.dto.LiftingDto;
import com.diplomawork.healthbody.mind.exceptions.UserNotFoundException;
import com.diplomawork.healthbody.mind.model.Exercise;
import com.diplomawork.healthbody.mind.model.User;
import com.diplomawork.healthbody.mind.model.enums.Type;
import com.diplomawork.healthbody.mind.model.enums.WorkoutActivityLevel;
import com.diplomawork.healthbody.mind.repository.ExerciseRepository;
import com.diplomawork.healthbody.mind.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;
    public void saveLiftingData(Integer userId, LiftingDto liftingDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User couldn't be found!"));
        Exercise exercise = Exercise.builder()
                .durationInMinutes(liftingDto.getDurationInMinutes())
                .caloriesBurned(liftingDto.getCaloriesBurned())
                .type(Type.LIFTING)
                .workoutActivityLevel(WorkoutActivityLevel.valueOf(liftingDto.getWorkoutActivityLevel()))
                .date(LocalDate.parse(liftingDto.getDate()))
                .user(user).build();
        exerciseRepository.save(exercise);
    }
    public void saveCardioData(Integer userId, CardioDto cardioDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User couldn't be found!"));
        Exercise exercise = Exercise.builder()
                .durationInMinutes(cardioDto.getDurationInMinutes())
                .caloriesBurned(cardioDto.getCaloriesBurned())
                .steps(cardioDto.getSteps())
                .type(Type.CARDIO)
                .date(LocalDate.parse(cardioDto.getDate()))
                .user(user).build();
        exerciseRepository.save(exercise);
    }
    public ExerciseDataDto getExerciseDataByDate(Integer userId, LocalDate date) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User couldn't be found!"));
        List<Exercise> exerciseList = exerciseRepository.findByUserAndDate(user, date);
        int caloriesBurnedL = 0, caloriesBurnedC = 0, hoursC = 0, minutesC = 0, steps = 0;
        for (Exercise e : exerciseList) {
            switch (e.getType()) {
                case LIFTING -> caloriesBurnedL += e.getCaloriesBurned();
                case CARDIO -> {
                    steps += e.getSteps();
                    caloriesBurnedC += e.getCaloriesBurned();
                    int durationInMinutes = e.getDurationInMinutes();
                    if (durationInMinutes >= 60) {
                        int currHours = durationInMinutes / 60;
                        hoursC += currHours;
                        minutesC += durationInMinutes - currHours * 60;
                    } else {
                        minutesC += durationInMinutes;
                    }
                    if (minutesC >= 60) {
                        hoursC += minutesC / 60;
                        minutesC = minutesC % 60;
                    }
                }
            }
        }
        return ExerciseDataDto.builder()
                .caloriesBurnedL(caloriesBurnedL)
                .caloriesBurnedC(caloriesBurnedC)
                .hoursC(hoursC)
                .minutesC(minutesC)
                .dailySteps(steps).build();
    }
}