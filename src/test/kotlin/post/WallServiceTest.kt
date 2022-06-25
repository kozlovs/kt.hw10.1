package post

import exceptions.*
import comment.*
import org.junit.Test

import org.junit.Assert.*

class WallServiceTest {

    @Test
    fun addNoZero() {
        val post = WallService.add(Post(0L, 0L, 0L, 0L, 0L, 0L))
        assertNotEquals(0, post.id)
    }

    @Test
    fun updateExists() {
        val post = WallService.add(Post(0L, 0L, 0L, 0L, 0L, 0L))
        val newPost = post.copy(text = "another text")
        val result = WallService.update(newPost)
        assertTrue(result)
    }

    @Test(expected = PostNotFoundException::class)
    fun updateShouldThrowPostNotFoundException() {
        val post = WallService.add(Post(0L, 0L, 0L, 0L, 0L, 0L))
        WallService.update(post.copy(text = "another text", id = post.id + 1))
    }

    @Test
    fun commentShouldNotThrow() {
        val post = WallService.add(Post(0L, 0L, 0L, 0L, 0L, 0L))
        val comment = Comment(0, post.id, 0, "some thread", 0, null)
        val returnComment = WallService.createComment(post.id, comment)
        assertTrue(returnComment in post.comments)
    }

    @Test(expected = PostNotFoundException::class)
    fun commentShouldThrow() {
        val post = WallService.add(Post(0L, 0L, 0L, 0L, 0L, 0L))
        val falsePostId = post.id + 1
        val comment = Comment(0, falsePostId, 0, "some thread", 0, null)
        WallService.createComment(falsePostId, comment)
    }

    @Test
    fun reportShouldNotThrow() {
        val post = WallService.add(Post(0L, 0L, 0L, 0L, 0L, 0L))
        val comment = Comment(0, post.id, 0, "some thread", 0, null)
        val newComment = WallService.createComment(post.id, comment)
        val report = ReportComment(0, newComment.id, 1)
        val returnReport = WallService.createReport(post.id, newComment.id, report)
        assertEquals(report, returnReport)
    }

    @Test(expected = UnknownReasonException::class)
    fun reportShouldThrowUnknownReasonException() {
        val post = WallService.add(Post(0L, 0L, 0L, 0L, 0L, 0L))
        val comment = Comment(0, post.id, 0, "some thread", 0, null)
        WallService.createComment(post.id, comment)
        val report = ReportComment(0, comment.id, -1)
        WallService.createReport(post.id, comment.id, report)
    }

    @Test(expected = PostNotFoundException::class)
    fun reportShouldThrowPostNotFoundException() {
        val post = WallService.add(Post(0L, 0L, 0L, 0L, 0L, 0L))
        val falsePostId = post.id + 1 // создаем неправильный id
        val comment = Comment(0, falsePostId, 0, "some thread", 0, null)
        WallService.createComment(falsePostId, comment)
    }

    @Test(expected = CommentNotFoundException::class)
    fun reportShouldThrowCommentNotFoundException() {
        val post = WallService.add(Post(0L, 0L, 0L, 0L, 0L, 0L))
        val comment = Comment(0, post.id, 0, "some thread", 0, null)
        WallService.createComment(post.id, comment)
        val falseCommentId = comment.id + 1 // создаем неправильный id
        val report = ReportComment(0, falseCommentId, 1)
        WallService.createReport(post.id, falseCommentId, report)
    }
}