package chat

import IdGenerator
import exceptions.*

object ChatService {
    private var chats = mutableListOf<Chat>()
    private var idChatGenerator = IdGenerator()
    private var idMassageGenerator = IdGenerator()

    fun add(chat: Chat): Chat {
        chats += chat.copy(id = idChatGenerator.getId())
        return chats.last()
    }

    fun sendMassage(chatId: Long, massage: Massage): Massage {
        for (chat in chats) {
            if (chat.id == chatId && !chat.isDeleted) {
                chat.massages += massage.copy(id = idMassageGenerator.getId())
                return chat.massages.last()
            }
        }
        throw ChatNotFoundException()
    }

    fun delete(chatId: Long): Boolean {
        for (chat in chats) {
            if (chat.id == chatId && !chat.isDeleted) {
                chat.isDeleted = true
                return true
            }
        }
        throw ChatNotFoundException()
    }

    fun deleteMassage(chatId: Long, massageId: Long): Boolean {
        for (chat in chats) {
            if (chat.id == chatId && !chat.isDeleted) {
                for (massage in ArrayList(chat.massages)) {
                    if (massage.id == massageId) {
                        chat.massages.remove(massage)
                        return true
                    }
                }
                throw MassageNotFoundException()
            }
        }
        throw ChatNotFoundException()
    }

//    fun edit(chatId: Long, chat: Chat): Boolean {
//        for ((index, thisNote) in notes.withIndex()) {
//            if (thisNote.id == noteId && !thisNote.isDelete) {
//                notes[index] = note.copy(
//                    id = thisNote.id,
//                    ownerId = thisNote.ownerId,
//                    date = thisNote.date
//                )
//                return true
//            }
//        }
//        throw NoteNotFoundException()
//    }

    fun editMassage(chatId: Long, massageId: Long, massage: Massage): Boolean {
        for (chat in chats) {
            if (chat.id == chatId && !chat.isDeleted) {
                for ((index, thisMassage) in chat.massages.withIndex()) {
                    if (thisMassage.id == massageId) {
                        chat.massages[index] = massage.copy(
                            id = thisMassage.id,
                            fromId = thisMassage.fromId,
                            toId = thisMassage.toId,
                            isEdit = true
                        )
                        return true
                    }
                }
                throw MassageNotFoundException()
            }
        }
        throw ChatNotFoundException()
    }

    fun get() = chats

    fun getById(chatId: Long): Chat {
        for (chat in chats) {
            if (chat.id == chatId && !chat.isDeleted)
                return chat
        }
        throw ChatNotFoundException()
    }

    fun getMassages(chatId: Long): MutableList<Massage> {
        for (chat in chats) {
            if (chat.id == chatId && !chat.isDeleted)
                return chat.massages
        }
        throw ChatNotFoundException()
    }

    fun restore(chatId: Long): Boolean {
        for (chat in chats) {
            if (chat.id == chatId && chat.isDeleted) {
                chat.isDeleted = false
                return true
            }
        }
        throw ChatNotFoundException()
    }
}