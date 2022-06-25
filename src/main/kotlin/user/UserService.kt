package user

import IdGenerator
import exceptions.UserNotFoundException

object UserService {
    private var users = mutableListOf<User>()
    private var idGenerator = IdGenerator()

    fun add(user: User): User {
        users += user.copy(id = idGenerator.getId())
        return users.last()
    }

    fun delete(userId: Long): Boolean {
        val user = getById(userId)
        user.isDeleted = true
        return true
    }

    fun restore(userId: Long): Boolean {
        val user = users.find { it.id == userId && it.isDeleted } ?: throw UserNotFoundException()
        user.isDeleted = false
        return true
    }

    fun get() = users

    fun getById(userId: Long) = users.find { it.id == userId && !it.isDeleted } ?: throw UserNotFoundException()

    fun edit(userId: Long, user: User): Boolean {
        val thisUser = getById(userId)
        users[users.indexOf(thisUser)] = user.copy(id = thisUser.id)
        return true
    }
}