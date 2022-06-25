package note

import IdGenerator
import comment.Comment
import comment.CommentService
import exceptions.*

object NoteService {
    private var notes = mutableListOf<Note>()
    private var idGenerator = IdGenerator()

    fun add(note: Note): Note {
        notes += note.copy(id = idGenerator.getId())
        return notes.last()
    } //Создает новую заметку у текущего пользователя.

    fun createComment(noteId: Long, comment: Comment): Comment {
        val note = notes.find { it.id == noteId && !it.isDeleted } ?: throw NoteNotFoundException()
        note.comments += comment.copy(id = CommentService.getId())
        return note.comments.last()
    } // Добавляет новый комментарий к заметке.

    fun delete(noteId: Long): Boolean {
        val note = notes.find { it.id == noteId && !it.isDeleted } ?: throw NoteNotFoundException()
        note.isDeleted = true
        return true
    } // Удаляет заметку текущего пользователя.

    fun deleteComment(noteId: Long, commentId: Long): Boolean {
        val note = notes.find { it.id == noteId && !it.isDeleted } ?: throw NoteNotFoundException()
        val comment = note.comments.find { it.id == commentId && !it.isDeleted } ?: throw CommentNotFoundException()
        comment.isDeleted = true
        return true
    } // Удаляет комментарий к заметке.

    fun edit(noteId: Long, note: Note): Boolean {
        val thisNote = notes.find { it.id == noteId && !it.isDeleted } ?: throw NoteNotFoundException()
        notes[notes.indexOf(thisNote)] = note.copy(
            id = thisNote.id,
            ownerId = thisNote.ownerId,
            date = thisNote.date
        )
        return true
    } // Редактирует заметку текущего пользователя.

    fun editComment(noteId: Long, commentId: Long, comment: Comment): Boolean {
        val note = notes.find { it.id == noteId && !it.isDeleted } ?: throw NoteNotFoundException()
        val thisComment =
            note.comments.find { it.id == commentId && !comment.isDeleted } ?: throw CommentNotFoundException()
        note.comments[note.comments.indexOf(thisComment)] = comment.copy(
            id = thisComment.id,
            ownerId = thisComment.ownerId,
            date = thisComment.date
        )
        return true
    } // Редактирует указанный комментарий у заметки.

    fun get() = notes // Возвращает список заметок, созданных пользователем.

    fun getById(noteId: Long) =
        notes.find { it.id == noteId && !it.isDeleted } ?: throw NoteNotFoundException() //Возвращает заметку по её id.

    fun getComments(noteId: Long): MutableList<Comment> {
        val note = notes.find { it.id == noteId && !it.isDeleted } ?: throw NoteNotFoundException()
        return note.comments
    } //Возвращает список комментариев к заметке.

    fun restoreComment(noteId: Long, commentId: Long): Boolean {
        val note = notes.find { it.id == noteId && !it.isDeleted } ?: throw NoteNotFoundException()
        val comment = note.comments.find { it.id == commentId && it.isDeleted } ?: throw CommentNotFoundException()
        comment.isDeleted = false
        return true
    } //Восстанавливает удалённый комментарий.
}