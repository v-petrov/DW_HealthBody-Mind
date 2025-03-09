package com.diplomawork.healthbody.mind.service;

import com.diplomawork.healthbody.mind.dto.FoodDto;
import com.diplomawork.healthbody.mind.dto.FoodIntakeDto;
import com.diplomawork.healthbody.mind.dto.UpdateFoodIntakeDto;
import com.diplomawork.healthbody.mind.exceptions.UserNotFoundException;
import com.diplomawork.healthbody.mind.model.Food;
import com.diplomawork.healthbody.mind.model.FoodIntake;
import com.diplomawork.healthbody.mind.model.User;
import com.diplomawork.healthbody.mind.model.enums.MealTime;
import com.diplomawork.healthbody.mind.repository.FoodIntakeRepository;
import com.diplomawork.healthbody.mind.repository.FoodRepository;
import com.diplomawork.healthbody.mind.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodService {
    private final FoodRepository foodRepository;
    private final FoodIntakeRepository foodIntakeRepository;
    private final UserRepository userRepository;
    public List<FoodDto> searchFoodByName(String foodName) {
        return foodRepository.findByNameContainingIgnoreCase(foodName).stream()
                .map(food -> FoodDto.builder()
                        .id(food.getId())
                        .name(food.getName())
                        .calories(food.getCalories())
                        .carbs(Double.parseDouble(String.valueOf(food.getCarbs())))
                        .fats(Double.parseDouble(String.valueOf(food.getFat())))
                        .protein(Double.parseDouble(String.valueOf(food.getProtein())))
                        .sugar(Double.parseDouble(String.valueOf(food.getSugar())))
                        .measurement(String.valueOf(food.getMeasurement()))
                        .build())
                .collect(Collectors.toList());
    }
    public Long saveFoodIntake(Integer userId, FoodIntakeDto foodIntakeDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User couldn't be found!"));
        Food food = foodRepository.findById(foodIntakeDto.getFoodDto().getId())
                .orElseThrow(() -> new RuntimeException("Food couldn't be found!"));
        FoodIntake foodIntake = FoodIntake.builder()
                .quantity(BigDecimal.valueOf(foodIntakeDto.getQuantity()))
                .mealTime(MealTime.valueOf(foodIntakeDto.getMealTime()))
                .date(foodIntakeDto.getDate())
                .user(user)
                .food(food).build();
        return foodIntakeRepository.save(foodIntake).getId();
    }
    public List<FoodIntakeDto> getFoodIntakesByDate(Integer userId, LocalDate date) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User couldn't be found!"));
        return foodIntakeRepository.findByUserAndDate(user, date)
                .stream()
                .map(foodIntake -> FoodIntakeDto.builder()
                        .id(foodIntake.getId())
                        .quantity(Double.parseDouble(String.valueOf(foodIntake.getQuantity())))
                        .mealTime(String.valueOf(foodIntake.getMealTime()))
                        .date(foodIntake.getDate())
                        .foodDto(FoodDto.builder()
                                .id(foodIntake.getFood().getId())
                                .name(foodIntake.getFood().getName())
                                .calories(foodIntake.getFood().getCalories())
                                .carbs(Double.parseDouble(String.valueOf(foodIntake.getFood().getCarbs())))
                                .fats(Double.parseDouble(String.valueOf(foodIntake.getFood().getFat())))
                                .protein(Double.parseDouble(String.valueOf(foodIntake.getFood().getProtein())))
                                .sugar(Double.parseDouble(String.valueOf(foodIntake.getFood().getSugar())))
                                .measurement(String.valueOf(foodIntake.getFood().getMeasurement()))
                                .build())
                        .build())
                .collect(Collectors.toList());
    }
    public void deleteFoodIntake(Long foodIntakeId) {
        if (!foodIntakeRepository.existsById(foodIntakeId)) {
            throw new RuntimeException("Food intake couldn't be found!");
        }
        foodIntakeRepository.deleteById(foodIntakeId);
    }
    public void updateFoodIntake(UpdateFoodIntakeDto updateFoodIntakeDto) {
        FoodIntake foodIntake = foodIntakeRepository.findById(updateFoodIntakeDto.getId())
                .orElseThrow(() -> new RuntimeException("Food intake couldn't be found!"));
        foodIntake.setQuantity(BigDecimal.valueOf(updateFoodIntakeDto.getQuantity()));
        foodIntakeRepository.save(foodIntake);
    }
}
