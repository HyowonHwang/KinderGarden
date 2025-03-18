import androidx.annotation.Keep

@Keep
data class VideoContentDto(
    val id: Int = Int.MIN_VALUE,
    val name: String = "",
    val description: String = "",
    val image: String = "",
    val contentUrl: String = ""
)

fun VideoContentDto.toDomainModel(): VideoContent {
    return VideoContent(
        id = id,
        name = name,
        description = description,
        image = image,
        contentUrl = contentUrl
    )
}

