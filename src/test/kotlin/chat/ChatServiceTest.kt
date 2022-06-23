package chat

import exceptions.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import user.User
import user.UserService

class ChatServiceTest{

    @Before
    fun clear() {
        ChatService.clear()
    }

    @Test
    fun addChatNoZero() {
        val user1 = UserService.add(User("Ivan", "Ivanov", 20))
        val user2 = UserService.add(User("Lena", "Petrova", 19))
        val text = "Hi"
        ChatService.addChat(user1.id, Chat(user1.id, user2.id), Massage(user1.id, user2.id, text))
        assertTrue(ChatService.getChats(user1.id).isNotEmpty())
    }

    @Test
    fun addMassageNoZero() {
        val user1 = UserService.add(User("Ivan", "Ivanov", 20))
        val user2 = UserService.add(User("Lena", "Petrova", 19))
        val massage1 = Massage(user1.id, user2.id, "Hi")
        val massage2 = Massage(user2.id, user1.id, "Hi! How are you?")
        val chat = ChatService.addChat(user1.id, Chat(user1.id, user2.id), massage1)
        val sendMassage = ChatService.addMassage(user2.id, chat.id, massage2)
        assertEquals(massage2.text, sendMassage.text)
    }

    @Test(expected = ChatNotFoundException::class)
    fun addMassageShouldThrowChatNotFoundException() {
        val user1 = UserService.add(User("Ivan", "Ivanov", 20))
        val user2 = UserService.add(User("Lena", "Petrova", 19))
        val massage1 = Massage(user1.id, user2.id, "Hi")
        val massage2 = Massage(user2.id, user1.id, "Hi! How are you?")
        val chat = ChatService.addChat(user1.id, Chat(user1.id, user2.id), massage1)
        val falseChatId = chat.id + 1
        ChatService.addMassage(user2.id, falseChatId, massage2)
    }

    @Test
    fun deleteChatShouldNotThrowException() {
        val user1 = UserService.add(User("Ivan", "Ivanov", 20))
        val user2 = UserService.add(User("Lena", "Petrova", 19))
        val massage1 = Massage(user1.id, user2.id, "Hi")
        val chat = ChatService.addChat(user1.id, Chat(user1.id, user2.id), massage1)
        val result = ChatService.deleteChat(user1.id, chat.id)
        assertTrue(result)
    }

    @Test(expected = ChatNotFoundException::class)
    fun deleteChatShouldThrowChatNotFoundException() {
        val user1 = UserService.add(User("Ivan", "Ivanov", 20))
        val user2 = UserService.add(User("Lena", "Petrova", 19))
        val massage1 = Massage(user1.id, user2.id, "Hi")
        val chat = ChatService.addChat(user1.id, Chat(user1.id, user2.id), massage1)
        val falseChatId = chat.id + 1
        ChatService.deleteChat(user1.id, falseChatId)
    }

    @Test
    fun deleteMassageShouldNotThrowException() {
        val user1 = UserService.add(User("Ivan", "Ivanov", 20))
        val user2 = UserService.add(User("Lena", "Petrova", 19))
        val massage1 = Massage(user1.id, user2.id, "Hi")
        val massage2 = Massage(user2.id, user1.id, "Hi! How are you?")
        val chat = ChatService.addChat(user1.id, Chat(user1.id, user2.id), massage1)
        val sendMassage = ChatService.addMassage(user2.id, chat.id, massage2)
        val result = ChatService.deleteMassage(user1.id, chat.id, sendMassage.id)
        assertTrue(result)
    }

    @Test(expected = ChatNotFoundException::class)
    fun deleteMassageShouldThrowChatNotFoundException() {
        val user1 = UserService.add(User("Ivan", "Ivanov", 20))
        val user2 = UserService.add(User("Lena", "Petrova", 19))
        val massage1 = Massage(user1.id, user2.id, "Hi")
        val massage2 = Massage(user2.id, user1.id, "Hi! How are you?")
        val chat = ChatService.addChat(user1.id, Chat(user1.id, user2.id), massage1)
        val sendMassage = ChatService.addMassage(user2.id, chat.id, massage2)
        val falseChatId = chat.id + 1
        ChatService.deleteMassage(user1.id, falseChatId, sendMassage.id)
    }

    @Test(expected = MassageNotFoundException::class)
    fun deleteMassageShouldThrowMassageNotFoundException() {
        val user1 = UserService.add(User("Ivan", "Ivanov", 20))
        val user2 = UserService.add(User("Lena", "Petrova", 19))
        val massage1 = Massage(user1.id, user2.id, "Hi")
        val massage2 = Massage(user2.id, user1.id, "Hi! How are you?")
        val chat = ChatService.addChat(user1.id, Chat(user1.id, user2.id), massage1)
        val sendMassage = ChatService.addMassage(user2.id, chat.id, massage2)
        val falseMassageId = sendMassage.id + 1
        ChatService.deleteMassage(user1.id, chat.id, falseMassageId)
    }

    @Test
    fun editChatShouldNotThrowException() {
        val user1 = UserService.add(User("Ivan", "Ivanov", 20))
        val user2 = UserService.add(User("Lena", "Petrova", 19))
        val massage1 = Massage(user1.id, user2.id, "Hi")
        val massage2 = Massage(user2.id, user1.id, "Hi! How are you?")
        val chat = ChatService.addChat(user1.id, Chat(user1.id, user2.id), massage1)
        val newChat = chat.copy(massages = mutableListOf(massage2))
        var result = false
        if (chat != newChat) {
            result = ChatService.editChat(user1.id, chat.id, newChat)
        }
        assertTrue(result)
    }

    @Test(expected = ChatNotFoundException::class)
    fun editChatShouldThrowChatNotFoundException() {
        val user1 = UserService.add(User("Ivan", "Ivanov", 20))
        val user2 = UserService.add(User("Lena", "Petrova", 19))
        val massage1 = Massage(user1.id, user2.id, "Hi")
        val massage2 = Massage(user2.id, user1.id, "Hi! How are you?")
        val chat = ChatService.addChat(user1.id, Chat(user1.id, user2.id), massage1)
        val newChat = chat.copy(massages = mutableListOf(massage2))
        val falseChatId = chat.id + 1
        ChatService.editChat(user1.id, falseChatId, newChat)
    }

    @Test
    fun editMassageShouldNotThrowException() {
        val user1 = UserService.add(User("Ivan", "Ivanov", 20))
        val user2 = UserService.add(User("Lena", "Petrova", 19))
        val massage1 = Massage(user1.id, user2.id, "Hi")
        val massage2 = Massage(user2.id, user1.id, "Hi! How are you?")
        val chat = ChatService.addChat(user1.id, Chat(user1.id, user2.id), massage1)
        val sendMassage = ChatService.addMassage(user2.id, chat.id, massage2)
        var result = false
        if (sendMassage != massage1) {
            result = ChatService.editMassage(user1.id, chat.id, sendMassage.id, massage1)
        }
        assertTrue(result)
    }

    @Test(expected = ChatNotFoundException::class)
    fun editMassageShouldThrowChatNotFoundException() {
        val user1 = UserService.add(User("Ivan", "Ivanov", 20))
        val user2 = UserService.add(User("Lena", "Petrova", 19))
        val massage1 = Massage(user1.id, user2.id, "Hi")
        val massage2 = Massage(user2.id, user1.id, "Hi! How are you?")
        val chat = ChatService.addChat(user1.id, Chat(user1.id, user2.id), massage1)
        val sendMassage = ChatService.addMassage(user2.id, chat.id, massage2)
        val falseChatId = chat.id + 1
        ChatService.editMassage(user1.id, falseChatId, sendMassage.id, massage1)
    }

    @Test(expected = MassageNotFoundException::class)
    fun editMassageShouldThrowMassageNotFoundException() {
        val user1 = UserService.add(User("Ivan", "Ivanov", 20))
        val user2 = UserService.add(User("Lena", "Petrova", 19))
        val massage1 = Massage(user1.id, user2.id, "Hi")
        val massage2 = Massage(user2.id, user1.id, "Hi! How are you?")
        val chat = ChatService.addChat(user1.id, Chat(user1.id, user2.id), massage1)
        val sendMassage = ChatService.addMassage(user2.id, chat.id, massage2)
        val falseMassageId = sendMassage.id + 1
        ChatService.editMassage(user1.id, chat.id, falseMassageId, massage1)
    }

    @Test
    fun getChatNoZero() {
        val user1 = UserService.add(User("Ivan", "Ivanov", 20))
        val user2 = UserService.add(User("Lena", "Petrova", 19))
        val text = "Hi"
        ChatService.addChat(user1.id, Chat(user1.id, user2.id), Massage(user1.id, user2.id, text))
        val result = ChatService.getChats(user1.id)
        assertNotNull(result)
    }

    @Test(expected = ChatNotFoundException::class)
    fun getChatShouldThrowChatNotFoundException() {
        val user1 = UserService.add(User("Ivan", "Ivanov", 20))
        val user2 = UserService.add(User("Lena", "Petrova", 19))
        val text = "Hi"
        ChatService.addChat(user1.id, Chat(user1.id, user2.id), Massage(user1.id, user2.id, text))
        val falseUserId = user2.id + 1
        ChatService.getChats(falseUserId)
    }

    @Test
    fun getUsersChatNoZero() {
        val user1 = UserService.add(User("Ivan", "Ivanov", 20))
        val user2 = UserService.add(User("Lena", "Petrova", 19))
        val text = "Hi"
        ChatService.addChat(user1.id, Chat(user1.id, user2.id), Massage(user1.id, user2.id, text))
        val result = ChatService.getUsersChats(user1.id)
        assertNotNull(result)
    }

    @Test
    fun getMassagesShouldNotThrowException() {
        val user1 = UserService.add(User("Ivan", "Ivanov", 20))
        val user2 = UserService.add(User("Lena", "Petrova", 19))
        val massage1 = Massage(user1.id, user2.id, "Hi")
        val massage2 = Massage(user2.id, user1.id, "Hi! How are you?")
        val chat = ChatService.addChat(user1.id, Chat(user1.id, user2.id), massage1)
        val sendMassage = ChatService.addMassage(user2.id, chat.id, massage2)
        val result = ChatService.getMassages(user2.id, chat.id, sendMassage.id, 6)
        assertNotNull(result)
    }

    @Test(expected = ChatNotFoundException::class)
    fun getMassagesShouldThrowChatNotFoundException() {
        val user1 = UserService.add(User("Ivan", "Ivanov", 20))
        val user2 = UserService.add(User("Lena", "Petrova", 19))
        val massage1 = Massage(user1.id, user2.id, "Hi")
        val massage2 = Massage(user2.id, user1.id, "Hi! How are you?")
        val chat = ChatService.addChat(user1.id, Chat(user1.id, user2.id), massage1)
        val sendMassage = ChatService.addMassage(user2.id, chat.id, massage2)
        val falseChatId = chat.id + 1
        ChatService.getMassages(user2.id, falseChatId, sendMassage.id, 6)
    }

    @Test(expected = MassageNotFoundException::class)
    fun getMassagesShouldThrowMassageNotFoundException() {
        val user1 = UserService.add(User("Ivan", "Ivanov", 20))
        val user2 = UserService.add(User("Lena", "Petrova", 19))
        val massage1 = Massage(user1.id, user2.id, "Hi")
        val massage2 = Massage(user2.id, user1.id, "Hi! How are you?")
        val chat = ChatService.addChat(user1.id, Chat(user1.id, user2.id), massage1)
        val sendMassage = ChatService.addMassage(user2.id, chat.id, massage2)
        val falseMassageId = sendMassage.id + 1
        ChatService.getMassages(user2.id, chat.id, falseMassageId, 6)
    }

    @Test
    fun getChatByIdShouldNotThrowException() {
        val user1 = UserService.add(User("Ivan", "Ivanov", 20))
        val user2 = UserService.add(User("Lena", "Petrova", 19))
        val massage1 = Massage(user1.id, user2.id, "Hi")
        val chat = ChatService.addChat(user1.id, Chat(user1.id, user2.id), massage1)
        val result = ChatService.getChatById(user2.id, chat.id)
        assertNotNull(result)
    }

    @Test(expected = ChatNotFoundException::class)
    fun getChatByIdShouldThrowChatNotFoundException() {
        val user1 = UserService.add(User("Ivan", "Ivanov", 20))
        val user2 = UserService.add(User("Lena", "Petrova", 19))
        val massage1 = Massage(user1.id, user2.id, "Hi")
        val chat = ChatService.addChat(user1.id, Chat(user1.id, user2.id), massage1)
        val falseChatId = chat.id + 1
        ChatService.getChatById(user2.id, falseChatId)
    }

    @Test
    fun getMassageByIdShouldNotThrowException() {
        val user1 = UserService.add(User("Ivan", "Ivanov", 20))
        val user2 = UserService.add(User("Lena", "Petrova", 19))
        val massage1 = Massage(user1.id, user2.id, "Hi!")
        val massage2 = Massage(user2.id, user1.id, "Hi! How are you?")
        val chat = ChatService.addChat(user1.id, Chat(user1.id, user2.id), massage1)
        val massageExpect = ChatService.addMassage(user2.id, chat.id, massage2)
        val result = ChatService.getMassageById(user2.id, chat.id, massageExpect.id)
        assertEquals(massageExpect, result)
    }

    @Test(expected = ChatNotFoundException::class)
    fun getMassageByIdShouldThrowChatNotFoundException() {
        val user1 = UserService.add(User("Ivan", "Ivanov", 20))
        val user2 = UserService.add(User("Lena", "Petrova", 19))
        val massage1 = Massage(user1.id, user2.id, "Hi!")
        val massage2 = Massage(user2.id, user1.id, "Hi! How are you?")
        val chat = ChatService.addChat(user1.id, Chat(user1.id, user2.id), massage1)
        val massageExpect = ChatService.addMassage(user2.id, chat.id, massage2)
        val falseChatId = chat.id + 1
        ChatService.getMassageById(user2.id, falseChatId, massageExpect.id)
    }

    @Test(expected = MassageNotFoundException::class)
    fun getMassageByIdShouldThrowMassageNotFoundException() {
        val user1 = UserService.add(User("Ivan", "Ivanov", 20))
        val user2 = UserService.add(User("Lena", "Petrova", 19))
        val massage1 = Massage(user1.id, user2.id, "Hi!")
        val massage2 = Massage(user2.id, user1.id, "Hi! How are you?")
        val chat = ChatService.addChat(user1.id, Chat(user1.id, user2.id), massage1)
        val massageExpect = ChatService.addMassage(user2.id, chat.id, massage2)
        val falseMassageId = massageExpect.id + 1
        ChatService.getMassageById(user2.id, chat.id, falseMassageId)
    }

    @Test
    fun getUnreadChatsCountNoZero() {
        val user1 = UserService.add(User("Ivan", "Ivanov", 20))
        val user2 = UserService.add(User("Lena", "Petrova", 19))
        val massage1 = Massage(user1.id, user2.id, "Hi!")
        val massage2 = Massage(user2.id, user1.id, "Hi! How are you?")
        val chat = ChatService.addChat(user1.id, Chat(user1.id, user2.id), massage1)
        ChatService.addMassage(user2.id, chat.id, massage2)
        val result = ChatService.getUnreadChatsCount(user2.id)
        assertEquals(1, result)
    }
}