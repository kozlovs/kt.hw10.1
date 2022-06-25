package post

import IdGenerator
import comment.Comment
import comment.CommentService
import comment.ReportComment
import exceptions.*

object WallService {
    private var posts = emptyArray<Post>()
    private var reports = emptyArray<ReportComment>()
    private var idGenerator = IdGenerator()

    fun createComment(postId: Long, comment: Comment): Comment {
        val post = posts.find { it.id == postId } ?: throw PostNotFoundException()
        post.comments += comment.copy(id = CommentService.getId())
        return post.comments.last()
    }

    fun createReport(postId: Long, commentId: Long, report: ReportComment): ReportComment {
        if (report.reason < 0 || report.reason > 6) throw UnknownReasonException()
        val post = posts.find { it.id == postId } ?: throw PostNotFoundException()
        val comment = post.comments.find { it.id == commentId } ?: throw CommentNotFoundException()
        reports += report.copy(commentId = comment.id)
        return reports.last()
    }

    fun add(post: Post): Post {
        posts += post.copy(id = idGenerator.getId())
        return posts.last()
    }

    fun update(post: Post): Boolean {
        val thisPost = posts.find { it.id == post.id } ?: throw PostNotFoundException()
        posts[posts.indexOf(thisPost)]= post.copy(id = thisPost.id, ownerId = thisPost.ownerId, date = thisPost.date)
        return true
    }
}