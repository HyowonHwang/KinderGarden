package com.hwang.kindergarden.presentation.viewmodel

import DailyMeal
import MealItem
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hwang.kindergarden.domain.repository.MealRepository
import com.hwang.kindergarden.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.plus

@HiltViewModel
class MealViewModel @Inject constructor(
    private val repository: MealRepository
) : ViewModel() {
    private val _dailyMeals = MutableStateFlow<DailyMeal?>(null)
    val dailyMeals: StateFlow<DailyMeal?> = _dailyMeals.asStateFlow()

    init {
        loadMealsForDate(DateUtils.getCurrentDateString())
    }

    fun loadMealsForDate(date : String) {
        viewModelScope.launch {
            try {
                _dailyMeals.value = repository.getMealsForDate(date)
            } catch (e: Exception) {
                // TODO: 에러 처리
                e.printStackTrace()
            }
        }
    }

    fun addMeal(meal: MealItem) {
        viewModelScope.launch {
            try {
                repository.addMeal(meal)
                // 로컬 상태 업데이트
                val currentMeals = _dailyMeals.value?.meals ?: emptyList()
                val updatedMeals = currentMeals + meal
                _dailyMeals.value = _dailyMeals.value?.copy(meals = updatedMeals)
            } catch (e: Exception) {
                // TODO: 에러 처리
                e.printStackTrace()
            }
        }
    }

    fun updateMeal(meal: MealItem) {
        viewModelScope.launch {
            try {
                repository.updateMeal(meal)
                // 로컬 상태 업데이트
                val currentMeals = _dailyMeals.value?.meals ?: emptyList()
                val updatedMeals = currentMeals.map {
                    if (it.id == meal.id) meal else it
                }
                _dailyMeals.value = _dailyMeals.value?.copy(meals = updatedMeals)
            } catch (e: Exception) {
                // TODO: 에러 처리
                e.printStackTrace()
            }
        }
    }

    fun deleteMeal(mealId: String) {
        viewModelScope.launch {
            try {
                val dateString = _dailyMeals.value?.date ?: return@launch
                repository.deleteMeal(dateString, mealId)
                // 로컬 상태 업데이트
                val currentMeals = _dailyMeals.value?.meals ?: emptyList()
                val updatedMeals = currentMeals.filter { it.id != mealId }
                _dailyMeals.value = _dailyMeals.value?.copy(meals = updatedMeals)
            } catch (e: Exception) {
                // TODO: 에러 처리
                e.printStackTrace()
            }
        }
    }

    fun setupRealtimeUpdates(date: String) {
        viewModelScope.launch {
            repository.observeMealsForDate(date).collect { dailyMeal ->
                _dailyMeals.value = dailyMeal
            }
        }
    }
}