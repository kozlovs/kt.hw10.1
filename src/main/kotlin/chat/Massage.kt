package chat

data class Massage(
    val fromId: Long,
    val toId: Long,
    val text: String,
    var isRead: Boolean = false,
    var isEdit: Boolean = false,
    var id: Long
)
