data class Location(val lat: Double, val long: Double)

infix fun Double.x(that: Double) = Location(this, that)

operator fun Location.plus(other: Location) = Location(
    lat = lat + other.lat, long = long + other.long
)

fun main() {
    val position = 55.7522 x 37.61557
    val result = position + Location(1.0, 1.0)
    println(result)
}

