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
    val attachments: Array<Attachment> = emptyArray(),
    val parentsStack: Array<Comment> = emptyArray(),
    val text: String = "",
    val id: Long = 0,
    var isDelete: Boolean = false,
    val date: LocalDate = LocalDate.now()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Comment

        if (id != other.id) return false
        if (fromId != other.fromId) return false
        if (thread != other.thread) return false
        if (replyToUser != other.replyToUser) return false
        if (replyToComment != other.replyToComment) return false
        if (!attachments.contentEquals(other.attachments)) return false
        if (!parentsStack.contentEquals(other.parentsStack)) return false
        if (text != other.text) return false
        if (date != other.date) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + fromId.hashCode()
        result = 31 * result + thread.hashCode()
        result = 31 * result + (replyToUser?.hashCode() ?: 0)
        result = 31 * result + (replyToComment?.hashCode() ?: 0)
        result = 31 * result + attachments.contentHashCode()
        result = 31 * result + parentsStack.contentHashCode()
        result = 31 * result + text.hashCode()
        result = 31 * result + date.hashCode()
        return result
    }
}