class IdGenerator {
    private var lastId = 0L

    fun getId() = ++lastId

    fun lastId() = lastId
}