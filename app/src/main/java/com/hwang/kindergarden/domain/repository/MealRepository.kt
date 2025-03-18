package com.hwang.kindergarden.domain.repository

import DailyMeal
import MealItem
import kotlinx.coroutines.flow.Flow


interface MealRepository {
    suspend fun getMealsForDate(date: String): DailyMeal
    suspend fun addMeal(meal: MealItem)
    suspend fun updateMeal(meal: MealItem)
    suspend fun deleteMeal(date: String, mealId: String)
    fun observeMealsForDate(date: String): Flow<DailyMeal>
}
