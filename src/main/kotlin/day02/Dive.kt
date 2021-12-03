package day02

import util.InputReader

fun main() {
//    val input = listOf("forward 5", "down 5", "forward 8", "up 3", "down 8", "forward 2")
    val input = InputReader.readInputAsStringList("day02.dat").filter { it.isNotEmpty() }
    val inputPair = input.map { i -> i.split(" ") }.map { i -> Pair(i.get(0), i.get(1).toInt()) }

    val (h, d) = calculatePosition(inputPair)
    println("1: ($h, $d) -> ${h * d}")

    val (h2, d2) = calculatePosUsingAim(inputPair)
    println("2: ($h2, $d2) -> ${h2 * d2}")
}

fun calculatePosition(input: List<Pair<String, Int>>): Pair<Int, Int> {
    var h = 0
    var d = 0
    for ((dir, value) in input) {
        when (dir) {
            "forward" -> h += value
            "up" -> d -= value
            "down" -> d += value
            else -> print("incorrect instruction '$dir'")
        }
    }
    return Pair(h, d)
}

fun calculatePosUsingAim(input: List<Pair<String, Int>>): Pair<Int, Int> {
    var h = 0
    var d = 0
    var aim = 0
    for ((dir, value) in input) {
        when (dir) {
            "forward" -> {
                h += value
                d += aim * value
            }
            "up" -> aim -= value
            "down" -> aim += value
        }
    }

    return Pair(h, d)
}