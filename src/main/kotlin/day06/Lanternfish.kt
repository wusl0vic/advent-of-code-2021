package day06

import util.InputReader

data class Fish(var age: Int, var first: Boolean = false) {
    fun getDecreasedFish(): List<Fish> {
        return when (age) {
            0 -> listOf(Fish(6), Fish(8, true))
            else -> listOf(Fish(age - 1))
        }
    }

    override fun toString(): String {
        return age.toString()
    }
}

fun main() {
    val testInput = listOf(3,4,3,1,2).map { Fish(it) }
    val input = InputReader.readInputAsStringList("day06.dat")[0].split(",").map { Fish(it.toInt()) }

    val sum = simulateLanternfish(input, 256)
    println("had $sum fish after 256 days")
}

fun simulateLanternfish(initialState: List<Fish>, days: Int = 80): Long {
    var state = mutableMapOf<Fish, Long>()
    for (fish in initialState) {
        val count = state.getOrDefault(fish, 0)
        state[fish] = count + 1
    }
    for (i in 1..days) {
        val updatedFish = mutableMapOf<Fish, Long>()
        state.forEach { (fish, c) ->
            for (f in fish.getDecreasedFish()) {
                val count = updatedFish.getOrDefault(f, 0)
                updatedFish[f] = c + count
            }
        }
        state = updatedFish
    }
    return state.map { it.value }.sum()
}