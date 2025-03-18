import java.util.UUID


data class DailyMeal(
    val date: String, // YYYY-MM-DD
    val meals: List<MealItem> = emptyList() // 하루 식단 리스트
)

data class MealItem(
    val id: String = UUID.randomUUID().toString(),
    val date: String,
    // Breakfast, Luanch, Dinner, Snack
    val type: String,
    val contents: String = ""
)