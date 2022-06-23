package chat

import IdGenerator
import exceptions.*

object ChatService {
    private var chats = mutableListOf<Chat>()
    private var idChatGenerator = IdGenerator()
    private var idMassageGenerator = IdGenerator()

    fun addChat(userId: Long, newChat: Chat, massage: Massage): Chat {
        val usersChat = getUsersChats(userId)
        for (chat in usersChat) {
            if ((chat.ownerId1 == newChat.ownerId1 && chat.ownerId2 == newChat.ownerId1) ||
                (chat.ownerId1 == newChat.ownerId2 && chat.ownerId2 == newChat.ownerId1)
            ) {
                chat.massages += massage.copy(id = idMassageGenerator.getId())
                return chat
            }
        }
        newChat.massages += massage.copy(id = idMassageGenerator.getId())
        chats += newChat.copy(id = idChatGenerator.getId(), ownerId1 = massage.fromId, ownerId2 = massage.toId)
        return chats.last()
    }

    fun addMassage(userId: Long, chatId: Long, massage: Massage): Massage {
        val usersChat = getChats(userId)
        for (chat in usersChat) {
            if (chat.id == chatId) {
                chat.massages += massage.copy(id = idMassageGenerator.getId())
                return chat.massages.last()
            }
        }
        throw ChatNotFoundException()
    }

    fun deleteChat(userId: Long, chatId: Long): Boolean {
        val usersChat = getChats(userId)
        for (chat in ArrayList(usersChat)) {
            if (chat.id == chatId) {
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

    fun deleteMassage(userId: Long, chatId: Long, massageId: Long): Boolean {
        val usersChat = getChats(userId)
        for (chat in usersChat) {
            if (chat.id == chatId) {
                for (massage in ArrayList(chat.massages)) {
                    if (massage.id == massageId) {
                        chat.massages.remove(massage)
                        if (chat.massages.isEmpty()) deleteChat(userId, chatId)
                        return true
                    }
                }
                throw MassageNotFoundException()
            }
        }
        throw ChatNotFoundException()
    }

    fun editChat(userId: Long, chatId: Long, chat: Chat): Boolean {
        val usersChat = getChats(userId)
        for ((index, thisChat) in usersChat.withIndex()) {
            if (thisChat.id == chatId) {
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

    fun editMassage(userId: Long, chatId: Long, massageId: Long, massage: Massage): Boolean {
        val usersChat = getChats(userId)
        for (chat in usersChat) {
            if (chat.id == chatId) {
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
        val result =
            chats.filter { it.ownerId1 == userId || it.ownerId2 == userId }
        if (result.isEmpty()) throw ChatNotFoundException()
        return result
    }

    fun getUsersChats(userId: Long) =
        chats.filter { (it.ownerId1 == userId || it.ownerId2 == userId) && it.massages.isNotEmpty() && !it.massages.last().isRead }

    fun getMassages(userId: Long, chatId: Long, lastMassageId: Long, countOfMassages: Int): List<Massage> {
        val usersChat = getChats(userId)
        for (chat in usersChat) {
            if (chat.id == chatId) {
                for (massage in chat.massages) {
                    if (massage.id == lastMassageId) {
                        val firstIndex = chat.massages.indexOf(massage)
                        val lastIndex =
                            if ((firstIndex + countOfMassages - 1) > chat.massages.size) chat.massages.size else (firstIndex + countOfMassages - 1)
                        val result = chat.massages.filterIndexed { index, _ -> index in firstIndex..lastIndex }
                        result.forEach { it.isRead = true }
                        return result
                    }
                }
                throw MassageNotFoundException()
            }
        }
        throw ChatNotFoundException()
    }

    fun getChatById(userId: Long, chatId: Long): Chat {
        val usersChat = getChats(userId)
        for (chat in usersChat) {
            if (chat.id == chatId)
                return chat
        }
        throw ChatNotFoundException()
    }

    fun getMassageById(userId: Long, chatId: Long, massageId: Long): Massage {
        val usersChat = getChats(userId)
        for (chat in usersChat) {
            if (chat.id == chatId) {
                for (massage in chat.massages) {
                    if (massage.id == massageId)
                        return massage
                }
                throw MassageNotFoundException()
            }
        }
        throw ChatNotFoundException()
    }

    fun getUnreadChatsCount(userId: Long): Int {
        var resultCount = 0
        val resultChats = chats.filter {
            (it.ownerId1 == userId || it.ownerId2 == userId)
        }
        if (resultChats.isEmpty()) return 0

        for (chat in resultChats) {
            for (massage in chat.massages) {
                if (!massage.isRead && massage.toId == userId) resultCount++
            }
        }
        return resultCount
    }
//      &&
    fun clear() {
        chats.clear()
        idChatGenerator = IdGenerator()
        idMassageGenerator = IdGenerator()
    }
}