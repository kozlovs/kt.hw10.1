package chat

import java.time.LocalDate
import java.time.LocalDateTime

data class Chat(
    val ownerId1: Long,
    val ownerId2: Long,
    val id: Long = 0,
    val date: LocalDateTime = LocalDateTime.now(),
    var massages: MutableList<Massage> = mutableListOf<Massage>()
)