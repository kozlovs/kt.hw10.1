package comment

import attachments.Attachment
import java.time.LocalDate

data class Comment(
    val ownerId: Long,
    val parentId: Long,
    val fromId: Long,
    val thread: String,
    val replyToUser: Long?,
    val replyToComment: Long?,
    val attachments: MutableList<Attachment> = mutableListOf(),
    val parentsStack: MutableList<Comment> = mutableListOf(),
    val text: String = "",
    val id: Long = 0,
    var isDeleted: Boolean = false,
    val date: LocalDate = LocalDate.now()
)