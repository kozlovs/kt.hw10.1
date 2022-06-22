package chat

import comment.Comment
import java.time.LocalDate

data class Chat(
    val massage: Massage,
    val ownerId1: Long,
    val ownerId2: Long,
    val title: String,
    val text: String,
    val privacy: Byte = 0,
    val commentPrivacy: Byte = 0,
    val privacyView: String,
    val privacyComment: String,
    val id: Long = 0,
    val date: LocalDate = LocalDate.now(),
    var isDeleted: Boolean = false,
    var comments: MutableList<Comment> = mutableListOf<Comment>()
) {
    var massages = mutableListOf(massage)
    // TODO: 20.06.2022 дописать
 //   infix fun Mto()
// to
}