package note

import comment.Comment
import comment.CommentService
import exceptions.*

object NoteService {
    private var notes = mutableListOf<Note>()
    private var lastId = 0L

    fun add(note: Note): Note {
        notes += note.copy(id = getId())
        return notes.last()
    } //Создает новую заметку у текущего пользователя.

    fun createComment(noteId: Long, comment: Comment): Comment {
        for (note in notes) {
            if (note.id == noteId && !note.isDelete) {
                note.comments += comment.copy(id = CommentService.getId())
                return note.comments.last()
            }
        }
        throw NoteNotFoundException()
    } // Добавляет новый комментарий к заметке.

    fun delete(noteId: Long): Boolean {
        for (note in notes) {
            if (note.id == noteId && !note.isDelete) {
                note.isDelete = true
                return true
            }
        }
        throw NoteNotFoundException()
    } // Удаляет заметку текущего пользователя.

    fun deleteComment(noteId: Long, commentId: Long): Boolean {
        for (note in notes) {
            if (note.id == noteId && !note.isDelete) {
                for (comment in note.comments) {
                    if (comment.id == commentId && !comment.isDelete) {
                        comment.isDelete = true
                        return true
                    }
                }
                throw CommentNotFoundException()
            }
        }
        throw NoteNotFoundException()
    } // Удаляет комментарий к заметке.

    fun edit(noteId: Long, note: Note): Boolean {
        for ((index, thisNote) in notes.withIndex()) {
            if (thisNote.id == noteId && !thisNote.isDelete) {
                notes[index] = note.copy(
                    id = thisNote.id,
                    ownerId = thisNote.ownerId,
                    date = thisNote.date
                )
                return true
            }
        }
        throw NoteNotFoundException()
    } // Редактирует заметку текущего пользователя.

    fun editComment(noteId: Long, commentId: Long, comment: Comment): Boolean {
        for (note in notes) {
            if (note.id == noteId && !note.isDelete) {
                for ((index, thisComment) in note.comments.withIndex()) {
                    if (thisComment.id == commentId && !comment.isDelete) {
                        notes[index] = note.copy(
                            id = thisComment.id,
                            ownerId = thisComment.ownerId,
                            date = thisComment.date
                        )
                        return true
                    }
                }
                throw CommentNotFoundException()
            }
        }
        throw NoteNotFoundException()
    } // Редактирует указанный комментарий у заметки.

    fun get() = notes // Возвращает список заметок, созданных пользователем.

    fun getById(noteId: Long): Note {
        for (note in notes) {
            if (note.id == noteId && !note.isDelete)
                return note
        }
        throw NoteNotFoundException()
    } //Возвращает заметку по её id.

    fun getComments(noteId: Long): MutableList<Comment> {
        for (note in notes) {
            if (note.id == noteId && !note.isDelete)
                return note.comments
        }
        throw NoteNotFoundException()
    } //Возвращает список комментариев к заметке.

    fun restoreComment(noteId: Long, commentId: Long): Boolean {
        for (note in notes) {
            if (note.id == noteId && !note.isDelete) {
                for (comment in note.comments) {
                    if (comment.id == commentId && comment.isDelete) {
                        comment.isDelete = false
                        return true
                    }
                }
                throw CommentNotFoundException()
            }
        }
        throw NoteNotFoundException()
    } //Восстанавливает удалённый комментарий.

    private fun getId(): Long {
        lastId += 1
        return lastId
    } // Генерирует id для заметок
}