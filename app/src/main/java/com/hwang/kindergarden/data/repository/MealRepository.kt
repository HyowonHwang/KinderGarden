package com.hwang.kindergarden.data.repository

import DailyMeal
import MealItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.hwang.kindergarden.domain.repository.MealRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MealRepositoryImpl @Inject constructor(
    private val database: FirebaseDatabase
) : MealRepository {
    private val mealsRef = database.getReference("meals")

    override suspend fun getMealsForDate(date: String): DailyMeal {
        val dateString = date
        val snapshot = mealsRef.child(dateString).get().await()
        
        return if (snapshot.exists()) {
            val meals = snapshot.children.mapNotNull { it.getValue(MealItem::class.java) }
            DailyMeal(
                date = dateString,
                meals = meals
            )
        } else {
            DailyMeal(
                date = dateString,
                meals = emptyList()
            )
        }
    }

    override suspend fun addMeal(meal: MealItem) {
        val dateString = meal.date
        val mealRef = mealsRef.child(dateString).child(meal.id)
        mealRef.setValue(meal).await()
    }

    override suspend fun updateMeal(meal: MealItem) {
        val dateString = meal.date
        val mealRef = mealsRef.child(dateString).child(meal.id)
        mealRef.setValue(meal).await()
    }

    override suspend fun deleteMeal(date: String, mealId: String) {
        val mealRef = mealsRef.child(date).child(mealId)
        mealRef.removeValue().await()
    }

    override fun observeMealsForDate(date: String): Flow<DailyMeal> = callbackFlow {
        val dateString = date
        
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dailyMeal = if (snapshot.exists()) {
                    val meals = snapshot.children.mapNotNull { it.getValue(MealItem::class.java) }
                    DailyMeal(
                        date = dateString,
                        meals = meals
                    )
                } else {
                    DailyMeal(
                        date = dateString,
                        meals = emptyList()
                    )
                }
                trySend(dailyMeal)
            }

            override fun onCancelled(error: DatabaseError) {
                // TODO: 에러 처리
                error.toException().printStackTrace()
            }
        }

        mealsRef.child(dateString).addValueEventListener(listener)

        awaitClose {
            mealsRef.child(dateString).removeEventListener(listener)
        }
    }
} 