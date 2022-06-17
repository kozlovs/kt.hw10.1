package comment

object CommentService {
    private var lastId = 0L

    fun getId(): Long {
        lastId += 1
        return lastId
    }
}