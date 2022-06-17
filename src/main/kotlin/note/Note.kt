package note

import comment.Comment
import java.time.LocalDate

data class Note(
    val ownerId: Long,
    val title: String,
    val text: String,
    val privacy: Byte = 0,
    val commentPrivacy: Byte = 0,
    val privacyView: String,
    val privacyComment: String,
    val id: Long = 0,
    val date: LocalDate = LocalDate.now(),
    var isDelete: Boolean = false,
    var comments: MutableList<Comment> = mutableListOf<Comment>()
)
