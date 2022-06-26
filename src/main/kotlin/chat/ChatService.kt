package chat

import IdGenerator
import exceptions.*

object ChatService {
    private var chats = mutableListOf<Chat>()
    private var idChatGenerator = IdGenerator()
    private var idMassageGenerator = IdGenerator()

    fun addChat(userId: Long, newChat: Chat, massage: Massage): Chat {
        val chat = getUsersChats(userId).find {
            (it.ownerId1 == newChat.ownerId1 && it.ownerId2 == newChat.ownerId1) ||
                    (it.ownerId1 == newChat.ownerId2 && it.ownerId2 == newChat.ownerId1)
        }
        return if (chat != null) {
            chat.massages += massage.copy(id = idMassageGenerator.getId())
            chat
        } else {
            newChat.massages += massage.copy(id = idMassageGenerator.getId())
            chats += newChat.copy(id = idChatGenerator.getId(), ownerId1 = massage.fromId, ownerId2 = massage.toId)
            chats.last()
        }
    }

    fun addMassage(userId: Long, chatId: Long, massage: Massage): Massage {
        val chat = getChats(userId).find { it.id == chatId } ?: throw ChatNotFoundException()
        chat.massages += massage.copy(id = idMassageGenerator.getId())
        return chat.massages.last()
    }

    fun deleteChat(userId: Long, chatId: Long): Boolean {
        val chat = getChats(userId).find { it.id == chatId } ?: throw ChatNotFoundException()
        chat.massages.clear()
        chats.remove(chat)
        return true
    }

    fun deleteMassage(userId: Long, chatId: Long, massageId: Long): Boolean {
        val chat = getChats(userId).find { it.id == chatId } ?: throw ChatNotFoundException()
        val massage = chat.massages.find { it.id == massageId } ?: throw MassageNotFoundException()
        chat.massages.remove(massage)
        if (chat.massages.isEmpty()) deleteChat(userId, chatId)
        return true
    }

    fun editChat(userId: Long, chatId: Long, chat: Chat): Boolean {
        val thisChat = getChats(userId).find { it.id == chatId } ?: throw ChatNotFoundException()
        chats[chats.indexOf(thisChat)] = chat.copy(
            id = thisChat.id,
            ownerId1 = thisChat.ownerId1,
            ownerId2 = thisChat.ownerId2,
            date = thisChat.date
        )
        return true
    }

    fun editMassage(userId: Long, chatId: Long, massageId: Long, massage: Massage): Boolean {
        val chat = getChats(userId).find { it.id == chatId } ?: throw ChatNotFoundException()
        val thisMassage = chat.massages.find { it.id == massageId } ?: throw MassageNotFoundException()
        chat.massages[chat.massages.indexOf(thisMassage)] = massage.copy(
            id = thisMassage.id,
            fromId = thisMassage.fromId,
            toId = thisMassage.toId,
            isEdit = true,
            date = thisMassage.date
        )
        return true
    }

    fun getChats(userId: Long): List<Chat> {
        val result = chats.filter { it.ownerId1 == userId || it.ownerId2 == userId }
        if (result.isEmpty()) throw ChatNotFoundException()
        return result
    }

    fun getUsersChats(userId: Long) =
        chats.filter { (it.ownerId1 == userId || it.ownerId2 == userId) && it.massages.isNotEmpty() && !it.massages.last().isRead }

    fun getMassages(userId: Long, chatId: Long, lastMassageId: Long, countOfMassages: Int): List<Massage> {
        val chat = getChats(userId).find { it.id == chatId } ?: throw ChatNotFoundException()
        val lastMassage = chat.massages.find { it.id == lastMassageId } ?: throw MassageNotFoundException()
        val firstIndex = chat.massages.indexOf(lastMassage)
        val lastIndex =
            if ((firstIndex + countOfMassages - 1) > chat.massages.size) chat.massages.size else (firstIndex + countOfMassages - 1)
        val result = chat.massages.filterIndexed { index, _ -> index in firstIndex..lastIndex }
        result.forEach { it.isRead = true }
        return result
    }

    fun getChatById(userId: Long, chatId: Long) =
        getChats(userId).find { it.id == chatId } ?: throw ChatNotFoundException()

    fun getMassageById(userId: Long, chatId: Long, massageId: Long): Massage {
        val chat = getChats(userId).find { it.id == chatId } ?: throw ChatNotFoundException()
        return chat.massages.find { it.id == massageId } ?: throw MassageNotFoundException()
    }

    fun getUnreadChatsCount(userId: Long) = chats
        .filter { (it.ownerId1 == userId || it.ownerId2 == userId) }
        .count { chat -> chat.massages.any { !it.isRead && it.toId == userId } }

    fun clear() {
        chats.clear()
        idChatGenerator = IdGenerator()
        idMassageGenerator = IdGenerator()
    }
}