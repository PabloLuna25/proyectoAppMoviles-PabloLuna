package Data

data class Reminder(
    val id: String = "",
    val uid: String = "",
    val title: String = "",
    val description: String = "",
    val year: Int = 0,
    val month: Int = 0,
    val day: Int = 0,
    val hour: Int = 0,
    val minute: Int = 0,
    val imageUrl: String? = null
)
