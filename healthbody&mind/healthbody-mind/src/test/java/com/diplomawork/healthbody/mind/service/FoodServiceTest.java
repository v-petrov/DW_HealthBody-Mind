package com.diplomawork.healthbody.mind.service;

import com.diplomawork.healthbody.mind.dto.FoodDto;
import com.diplomawork.healthbody.mind.dto.FoodIntakeDto;
import com.diplomawork.healthbody.mind.dto.UpdateFoodIntakeDto;
import com.diplomawork.healthbody.mind.exceptions.UserNotFoundException;
import com.diplomawork.healthbody.mind.model.Food;
import com.diplomawork.healthbody.mind.model.FoodIntake;
import com.diplomawork.healthbody.mind.model.User;
import com.diplomawork.healthbody.mind.model.enums.MealTime;
import com.diplomawork.healthbody.mind.model.enums.Measurement;
import com.diplomawork.healthbody.mind.repository.FoodIntakeRepository;
import com.diplomawork.healthbody.mind.repository.FoodRepository;
import com.diplomawork.healthbody.mind.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FoodServiceTest {
    private FoodRepository foodRepository;
    private FoodIntakeRepository foodIntakeRepository;
    private UserRepository userRepository;
    private FoodService foodService;
    @BeforeEach
    void setUp() {
        foodRepository = mock(FoodRepository.class);
        foodIntakeRepository = mock(FoodIntakeRepository.class);
        userRepository = mock(UserRepository.class);
        foodService = new FoodService(foodRepository, foodIntakeRepository, userRepository);
    }
    @Test
    void testSearchFoodByNameFound() {
        Food mockFood = Food.builder()
                .id(1)
                .name("Apple")
                .calories(52)
                .carbs(BigDecimal.valueOf(14))
                .fat(BigDecimal.valueOf(0.2))
                .protein(BigDecimal.valueOf(0.3))
                .sugar(BigDecimal.valueOf(10))
                .measurement(Measurement.G)
                .build();

        when(foodRepository.findByNameContainingIgnoreCase("Apple")).thenReturn(List.of(mockFood));
        List<FoodDto> result = foodService.searchFoodByName("Apple");

        assertEquals(1, result.size());
        FoodDto foodDto = result.getFirst();
        assertEquals("Apple", foodDto.getName());
        assertEquals(52, foodDto.getCalories());
        assertEquals(14.0, foodDto.getCarbs());
        assertEquals(0.2, foodDto.getFats());
        assertEquals(0.3, foodDto.getProtein());
        assertEquals(10.0, foodDto.getSugar());
        assertEquals(String.valueOf(Measurement.G), foodDto.getMeasurement());

        verify(foodRepository, times(1)).findByNameContainingIgnoreCase("Apple");
    }
    @Test
    void testSearchFoodByNameNotFound() {
        when(foodRepository.findByNameContainingIgnoreCase("Orange")).thenReturn(Collections.emptyList());
        List<FoodDto> result = foodService.searchFoodByName("Orange");

        assertEquals(0, result.size());
        verify(foodRepository, times(1)).findByNameContainingIgnoreCase("Orange");
    }
    @Test
    void testSaveFoodIntakeSuccess() {
        Integer userId = 1;
        Integer foodId = 10;

        User mockUser = new User();
        mockUser.setId(userId);

        Food mockFood = Food.builder()
                .id(foodId)
                .name("Banana")
                .build();

        FoodDto foodDto = FoodDto.builder()
                .id(foodId)
                .build();

        FoodIntakeDto foodIntakeDto = FoodIntakeDto.builder()
                .quantity(2.0)
                .mealTime(MealTime.BREAKFAST.name())
                .date(LocalDate.now())
                .foodDto(foodDto)
                .build();

        FoodIntake savedFoodIntake = FoodIntake.builder()
                .id(100L)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(foodRepository.findById(foodId)).thenReturn(Optional.of(mockFood));
        when(foodIntakeRepository.save(any(FoodIntake.class))).thenReturn(savedFoodIntake);
        Long result = foodService.saveFoodIntake(userId, foodIntakeDto);

        assertEquals(100L, result);
        verify(userRepository, times(1)).findById(userId);
        verify(foodRepository, times(1)).findById(foodId);
        verify(foodIntakeRepository, times(1)).save(any(FoodIntake.class));
    }
    @Test
    void testSaveFoodIntakeUserNotFoundThrownException() {
        Integer userId = 1;
        FoodIntakeDto foodIntakeDto = FoodIntakeDto.builder().build();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () ->
                foodService.saveFoodIntake(userId, foodIntakeDto)
        );
        assertEquals("User couldn't be found!", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verifyNoInteractions(foodRepository);
        verifyNoInteractions(foodIntakeRepository);
    }
    @Test
    void testSaveFoodIntakeFoodNotFoundThrownException() {
        Integer userId = 1;
        Integer foodId = 10;

        User mockUser = new User();
        mockUser.setId(userId);

        FoodDto foodDto = FoodDto.builder()
                .id(foodId)
                .build();

        FoodIntakeDto foodIntakeDto = FoodIntakeDto.builder()
                .quantity(1.0)
                .mealTime(MealTime.LUNCH.name())
                .date(LocalDate.now())
                .foodDto(foodDto)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(foodRepository.findById(foodId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                foodService.saveFoodIntake(userId, foodIntakeDto)
        );
        assertEquals("Food couldn't be found!", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(foodRepository, times(1)).findById(foodId);
        verifyNoInteractions(foodIntakeRepository);
    }
    @Test
    void testGetFoodIntakesByDateSuccess() {
        Integer userId = 1;
        LocalDate date = LocalDate.of(2024, 4, 28);

        User mockUser = new User();
        mockUser.setId(userId);

        Food mockFood = Food.builder()
                .id(1)
                .name("Oatmeal")
                .calories(150)
                .carbs(BigDecimal.valueOf(27))
                .fat(BigDecimal.valueOf(3))
                .protein(BigDecimal.valueOf(5))
                .sugar(BigDecimal.valueOf(1))
                .measurement(Measurement.G)
                .build();

        FoodIntake mockFoodIntake = FoodIntake.builder()
                .id(1L)
                .quantity(BigDecimal.valueOf(2.0))
                .mealTime(MealTime.BREAKFAST)
                .date(date)
                .food(mockFood)
                .user(mockUser)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(foodIntakeRepository.findByUserAndDate(mockUser, date)).thenReturn(List.of(mockFoodIntake));

        List<FoodIntakeDto> result = foodService.getFoodIntakesByDate(userId, date);

        assertEquals(1, result.size());
        FoodIntakeDto intakeDto = result.getFirst();
        assertEquals(1L, intakeDto.getId());
        assertEquals(2.0, intakeDto.getQuantity());
        assertEquals(MealTime.BREAKFAST.name(), intakeDto.getMealTime());
        assertEquals(date, intakeDto.getDate());

        FoodDto foodDto = intakeDto.getFoodDto();
        assertNotNull(foodDto);
        assertEquals(1, foodDto.getId());
        assertEquals("Oatmeal", foodDto.getName());
        assertEquals(150, foodDto.getCalories());
        assertEquals(27.0, foodDto.getCarbs());
        assertEquals(3.0, foodDto.getFats());
        assertEquals(5.0, foodDto.getProtein());
        assertEquals(1.0, foodDto.getSugar());
        assertEquals(String.valueOf(Measurement.G), foodDto.getMeasurement());

        verify(userRepository, times(1)).findById(userId);
        verify(foodIntakeRepository, times(1)).findByUserAndDate(mockUser, date);
    }
    @Test
    void testGetFoodIntakesByDateUserNotFoundThrownException() {
        Integer userId = 2;
        LocalDate date = LocalDate.now();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () ->
                foodService.getFoodIntakesByDate(userId, date)
        );
        assertEquals("User couldn't be found!", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verifyNoInteractions(foodIntakeRepository);
    }
    @Test
    void testDeleteFoodIntakeSuccess() {
        Long foodIntakeId = 1L;
        when(foodIntakeRepository.existsById(foodIntakeId)).thenReturn(true);
        foodService.deleteFoodIntake(foodIntakeId);

        verify(foodIntakeRepository, times(1)).existsById(foodIntakeId);
        verify(foodIntakeRepository, times(1)).deleteById(foodIntakeId);
    }
    @Test
    void testDeleteFoodIntakeNotFoundThrownException() {
        Long foodIntakeId = 2L;
        when(foodIntakeRepository.existsById(foodIntakeId)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                foodService.deleteFoodIntake(foodIntakeId)
        );

        assertEquals("Food intake couldn't be found!", exception.getMessage());
        verify(foodIntakeRepository, times(1)).existsById(foodIntakeId);
        verify(foodIntakeRepository, never()).deleteById(any());
    }
    @Test
    void testUpdateFoodIntakeSuccess() {
        Long intakeId = 1L;
        double newQuantity = 3.5;
        FoodIntake existingIntake = FoodIntake.builder()
                .id(intakeId)
                .quantity(BigDecimal.valueOf(1.0))
                .build();

        UpdateFoodIntakeDto updateDto = UpdateFoodIntakeDto.builder()
                .id(intakeId)
                .quantity(newQuantity)
                .build();
        when(foodIntakeRepository.findById(intakeId)).thenReturn(Optional.of(existingIntake));

        foodService.updateFoodIntake(updateDto);

        assertEquals(BigDecimal.valueOf(newQuantity), existingIntake.getQuantity());
        verify(foodIntakeRepository, times(1)).save(existingIntake);
    }
    @Test
    void testUpdateFoodIntakeNotFoundThrownException() {
        Long intakeId = 1L;
        UpdateFoodIntakeDto updateDto = UpdateFoodIntakeDto.builder()
                .id(intakeId)
                .quantity(2.0)
                .build();

        when(foodIntakeRepository.findById(intakeId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                foodService.updateFoodIntake(updateDto)
        );
        assertEquals("Food intake couldn't be found!", exception.getMessage());
        verify(foodIntakeRepository, never()).save(any());
    }
}
