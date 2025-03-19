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

@HiltViewModel
class MealViewModel @Inject constructor(
    private val repository: MealRepository
) : ViewModel() {
    private val _dailyMeals = MutableStateFlow<DailyMeal?>(null)
    val dailyMeals: StateFlow<DailyMeal?> = _dailyMeals.asStateFlow()

    init {
        loadMealsForDate(DateUtils.getCurrentDateString())
    }

    fun loadMealsForDate(date: String) {
        viewModelScope.launch {
            repository.getMealsForDate(date)
                .onSuccess { dailyMeal ->
                    _dailyMeals.value = getSortedDailyMeals(dailyMeal)
                }
                .onFailure {
                    // TODO : Error 처리
                }
        }
    }

    fun addMeal(meal: MealItem) {
        viewModelScope.launch {
            repository.addMeal(meal)
                .onSuccess {
                    // update 옵져버에 위임
                }
                .onFailure {
                    // TODO: 에러 처리
                    it.printStackTrace()
                }
        }
    }

    fun updateMeal(meal: MealItem) {
        viewModelScope.launch {
            repository.updateMeal(meal)
                .onSuccess {
                }
                .onFailure {
                    // TODO: Error 처리
                    it.printStackTrace()
                }
        }
    }

    fun deleteMeal(mealId: String) {
        viewModelScope.launch {
            val dateString = _dailyMeals.value?.date ?: return@launch
            repository.deleteMeal(dateString, mealId)
                .onSuccess {
                }
                .onFailure {
                    // TODO: Error
                    it.printStackTrace()
                }
        }
    }

    fun setupRealtimeUpdates(date: String) {
        viewModelScope.launch {
            repository.observeMealsForDate(date).collect { dailyMeal ->
                _dailyMeals.value = getSortedDailyMeals(dailyMeal)
            }
        }
    }

    private fun getSortedDailyMeals(dailyMeal: DailyMeal): DailyMeal {
        return dailyMeal.copy(
            meals = dailyMeal.meals.sortedWith(compareBy { mealItem ->
                when (mealItem.type) {
                    MealType.BREAKFAST.value -> 0
                    MealType.LUNCH.value -> 1
                    MealType.DINNER.value -> 2
                    MealType.SNACK.value -> 3
                    else -> 4
                }
            })
        )
    }
}