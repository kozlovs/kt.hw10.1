package post

import comment.Comment
import comment.ReportComment
import exceptions.*

object WallService {
    private var posts = emptyArray<Post>()
    private var comments = emptyArray<Comment>()
    private var reports = emptyArray<ReportComment>()
    private var lastId = 0L

    fun createComment(postId: Long, comment: Comment): Comment {
        for (post in posts) {
            if (post.id == postId) {
                comments += comment
                return comments.last()
            }
        }
        throw PostNotFoundException()
    }

    fun createReport(postId: Long, commentId: Long, report: ReportComment) : ReportComment {
        if (report.reason < 0 || report.reason > 6) throw UnknownReasonException()
        for (post in posts) {
            if (post.id == postId) {
                for (comment in comments) {
                    if (comment.id == commentId) {
                        reports += report
                        return reports.last()
                    }
                }
                throw CommentNotFoundException()
            }
        }
        throw PostNotFoundException()
    }

    fun add(post: Post): Post {
        posts += post.copy(id = getId())
        return posts.last()
    }

    fun update(post: Post): Boolean {
        for ((index, thisPost) in posts.withIndex()) {
            if (thisPost.id == post.id) {
                posts[index] = post.copy(
                    id = thisPost.id,
                    ownerId = thisPost.ownerId,
                    date = thisPost.date
                )
                return true
            }
        }
        return false
    }

    private fun getId(): Long {
        lastId += 1
        return lastId
    }
}