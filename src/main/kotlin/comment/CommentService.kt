package comment

import IdGenerator

object CommentService {
    private val idGenerator = IdGenerator()

    fun getId() = idGenerator.getId()
}