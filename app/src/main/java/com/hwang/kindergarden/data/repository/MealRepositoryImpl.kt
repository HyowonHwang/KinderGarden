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
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MealRepositoryImpl @Inject constructor(
    private val database: FirebaseDatabase
) : MealRepository {
    private val mealsRef = database.getReference("meals")

    override suspend fun getMealsForDate(date: String): Result<DailyMeal> = try {
        val dateString = date
        val snapshot = mealsRef.child(dateString).get().await()

        Result.success(
            if (snapshot.exists()) {
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
        )
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun addMeal(meal: MealItem): Result<Boolean> {
        val dateString = meal.date
        val mealRef = mealsRef.child(dateString).child(meal.id)
        return try {
            mealRef.setValue(meal).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateMeal(meal: MealItem): Result<Boolean> {
        val dateString = meal.date
        val mealRef = mealsRef.child(dateString).child(meal.id)
        return try {
            mealRef.setValue(meal).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteMeal(date: String, mealId: String): Result<Boolean> {
        val mealRef = mealsRef.child(date).child(mealId)
        return try {
            mealRef.removeValue().await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
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