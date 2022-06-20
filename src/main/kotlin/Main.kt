import note.Note
import note.NoteService
import post.Post
import post.WallService

fun main() {
    val list = mutableListOf(
        Post(0L, 0L, 0L, 0L, 0L, 0L, id = 0),
        Post(0L, 0L, 0L, 0L, 0L, 0L, id = 1, likes = 1),
        Post(0L, 0L, 0L, 0L, 0L, 0L, id = 2, likes = 2)
    )
    list.filter { it.likes > 0 }

    val result = "один" to "второй"
//    val post = WallService.add(Post(0L, 0L, 0L, 0L, 0L, 0L))
//    println(post)
//    val note = NoteService.add(Note(0L, "Title", "Text", 0, 0, "text", "text"))
//    println(note.isDelete)
//    val result1 = NoteService.delete(note.id)
//    println(note.isDelete)
//    val result2 = NoteService.delete(note.id)
//    println(note.isDelete)
//    println(result1)
//    println(result2)
}