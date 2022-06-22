package chat

import java.time.LocalDate

data class Massage(
    val fromId: Long,
    val toId: Long,
    val text: String,
    var isRead: Boolean = false,
    var isEdit: Boolean = false,
    var id: Long,
    val date: LocalDate = LocalDate.now()
)
