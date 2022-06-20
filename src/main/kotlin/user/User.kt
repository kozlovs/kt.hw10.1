package user

data class User(val name: String, val surname: String, var age: Int, val id: Long = 0, var isDeleted: Boolean = false)