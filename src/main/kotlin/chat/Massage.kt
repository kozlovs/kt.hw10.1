package chat

import java.time.LocalDate
import java.time.LocalDateTime

data class Massage(
    val fromId: Long,
    val toId: Long,
    val text: String,
    var isRead: Boolean = false,
    var isEdit: Boolean = false,
    var id: Long = 0L,
    val date: LocalDateTime = LocalDateTime.now()
)
