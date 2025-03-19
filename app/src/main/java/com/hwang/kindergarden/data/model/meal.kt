import java.util.UUID

enum class MealType(val value: String) {
    BREAKFAST("Breakfast"), LUNCH("Launch"), DINNER("Dinner"), SNACK("Snack");

    fun toKoreanString(): String = when (this) {
        BREAKFAST -> "아침"
        LUNCH -> "점심"
        DINNER -> "저녁"
        SNACK -> "간식"
    }
}

data class MealData(
    val type: MealType,
    val content: String = "",
    val thumbnailUrl: String = ""
)

data class DailyMeal(
    val date: String = "", // YYYY-MM-DD
    val meals: List<MealItem> = emptyList() // 하루 식단 리스트
)

data class MealItem(
    val id: String = UUID.randomUUID().toString(),
    val date: String = "", // YYYY-MM-DD
    // Breakfast, Luanch, Dinner, Snack
    val type: String = "",
    val contents: String = "",
    val thumbnailUrl: String = ""
)
