package com.hwang.kindergarden.domain.repository

import DailyMeal
import MealItem
import kotlinx.coroutines.flow.Flow


interface MealRepository {
    suspend fun getMealsForDate(date: String): Result<DailyMeal>
    suspend fun addMeal(meal: MealItem) : Result<Boolean>
    suspend fun updateMeal(meal: MealItem) : Result<Boolean>
    suspend fun deleteMeal(date: String, mealId: String) : Result<Boolean>
    fun observeMealsForDate(date: String): Flow<DailyMeal>
}
