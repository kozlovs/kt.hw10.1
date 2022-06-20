class IdGenerator {
    private var lastId = 0L

    fun getId(): Long {
        lastId += 1
        return lastId
    }
}