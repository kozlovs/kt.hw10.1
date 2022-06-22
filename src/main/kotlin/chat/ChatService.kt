package chat

import IdGenerator
import exceptions.*

object ChatService {
    private var chats = mutableListOf<Chat>()
    private var idChatGenerator = IdGenerator()
    private var idMassageGenerator = IdGenerator()

    fun addChat(chat: Chat, massage: Massage): Chat {
        chat.massages.add(massage)
        chats += chat.copy(id = idChatGenerator.getId(), ownerId1 = massage.fromId, ownerId2 = massage.toId)
        return chats.last()
    }

    fun addMassage(chatId: Long, massage: Massage): Massage {
        for (chat in chats) {
            if (chat.id == chatId && !chat.isDeleted) {
                chat.massages.add(massage)
                return chat.massages.last()
            }
        }
        throw ChatNotFoundException()
    }

    fun deleteChat(chatId: Long): Boolean {
        for (chat in ArrayList(chats)) {
            if (chat.id == chatId && !chat.isDeleted) {
                if (chat.massages.isNotEmpty()) {
                    for (massage in ArrayList(chat.massages)) {
                        chat.massages.remove(massage)
                    }
                }
                chats.remove(chat)
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
                        if (chat.massages.isEmpty()) deleteChat(chatId)
                        return true
                    }
                }
                throw MassageNotFoundException()
            }
        }
        throw ChatNotFoundException()
    }

    fun editChat(chatId: Long, chat: Chat): Boolean {
        for ((index, thisChat) in chats.withIndex()) {
            if (thisChat.id == chatId && !thisChat.isDeleted) {
                chats[index] = chat.copy(
                    id = thisChat.id,
                    ownerId1 = thisChat.ownerId1,
                    ownerId2 = thisChat.ownerId2,
                    date = thisChat.date
                )
                return true
            }
        }
        throw ChatNotFoundException()
    }

    fun editMassage(chatId: Long, massageId: Long, massage: Massage): Boolean {
        for (chat in chats) {
            if (chat.id == chatId && !chat.isDeleted) {
                for ((index, thisMassage) in chat.massages.withIndex()) {
                    if (thisMassage.id == massageId) {
                        chat.massages[index] = massage.copy(
                            id = thisMassage.id,
                            fromId = thisMassage.fromId,
                            toId = thisMassage.toId,
                            isEdit = true,
                            date = thisMassage.date
                        )
                        return true
                    }
                }
                throw MassageNotFoundException()
            }
        }
        throw ChatNotFoundException()
    }

    fun getChats(userId: Long): List<Chat> {
        return chats.filter { !it.isDeleted && (it.ownerId1 == userId || it.ownerId2 == userId) }
        // TODO: 21.06.2022 дописать
    }

    fun getMassages(chatId: Long, lastMassageId: Long): MutableList<Massage> {
        for (chat in chats) {
            if (chat.id == chatId && !chat.isDeleted)
                for (massage in chat.massages) {
                    if (!massage.isRead) massage.isRead = true
                }
                return chat.massages
        }
        throw ChatNotFoundException()
        // TODO: 21.06.2022 дописать
    }

    fun getById(chatId: Long): Chat {
        for (chat in chats) {
            if (chat.id == chatId && !chat.isDeleted)
                return chat
        }
        throw ChatNotFoundException()
    }

    fun getMassageById(chatId: Long, massageId: Long): Massage {
        for (chat in chats) {
            if (chat.id == chatId && !chat.isDeleted) {
                for (massage in chat.massages) {
                    if (massage.id == massageId)
                        return massage
                }
                throw MassageNotFoundException()
            }
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

    fun getUnreadChatsCount(userId: Long): Int {
        val resultChats = chats.filter {
            !it.isDeleted
                    && (it.ownerId1 == userId || it.ownerId2 == userId)
                    && !it.massages.last().isRead
        }
        return resultChats.size
    }
}