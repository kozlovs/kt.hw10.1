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
        for (user in users) {
            if (user.id == userId && !user.isDeleted) {
                user.isDeleted = true
                return true
            }
        }
        throw UserNotFoundException()
    }

    fun restore(userId: Long): Boolean {
        for (user in users) {
            if (user.id == userId && user.isDeleted) {
                user.isDeleted = false
                return true
            }
        }
        throw UserNotFoundException()
    }

    fun get() = users

    fun getById(userId: Long): User {
        for (user in users) {
            if (user.id == userId && !user.isDeleted)
                return user
        }
        throw UserNotFoundException()
    }

    fun edit(userId: Long, user: User): Boolean {
        for ((index, thisUser) in users.withIndex()) {
            if (thisUser.id == userId && !thisUser.isDeleted) {
                users[index] = user.copy(id = thisUser.id)
                return true
            }
        }
        throw UserNotFoundException()
    }
}

