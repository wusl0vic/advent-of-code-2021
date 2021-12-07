package day07

import util.InputReader
import kotlin.math.abs
import kotlin.math.min

fun main() {
    val testInput = listOf(16,1,2,0,4,2,7,1,2,14)
    val input = InputReader.readInputAsStringList("day07.dat")[0].split(",").map { it.toInt() }

    val minFuel = align(input, true)
    println("Least fuel comsumption is $minFuel")
}

fun align(crabs: List<Int>, part2: Boolean = false): Int {
    var minFuel = Int.MAX_VALUE
    val min = crabs.minOf { it }
    val max = crabs.maxOf { it }
    for (pos in min..max) {
        minFuel = min(minFuel, alignTo(crabs, pos, part2))
    }
    return minFuel
}

fun alignTo(crabs: List<Int>, pos: Int, part2: Boolean = false): Int {
    var fuel = 0

    crabs.forEach { crab ->
        val distance = abs(crab - pos)
        fuel += if (part2) {
            (0..distance).sum()
        } else {
            distance
        }
    }

    return fuel
}