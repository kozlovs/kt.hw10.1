import note.Note
import note.NoteService
import post.Post
import post.WallService

fun main() {
    val post = WallService.add(Post(0L, 0L, 0L, 0L, 0L, 0L))
    println(post)
    val note = NoteService.add(Note(0L, "Title", "Text", 0, 0, "text", "text"))
    println(note.isDelete)
    val result1 = NoteService.delete(note.id)
    println(note.isDelete)
    val result2 = NoteService.delete(note.id)
    println(note.isDelete)
    println(result1)
    println(result2)
}