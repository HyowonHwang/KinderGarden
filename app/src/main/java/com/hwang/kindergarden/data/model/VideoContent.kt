data class VideoContent(
    val id: Int,
    val name: String,
    val description: String,
    val image: String = "",
    val contentUrl: String = ""
)

data class VideoContentList(
    val videoContentList: List<VideoContent> = emptyList()
)

