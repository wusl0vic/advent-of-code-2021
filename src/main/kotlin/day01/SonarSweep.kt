package day01

import util.InputReader

val input = listOf(199, 200, 208, 210, 200, 207, 240, 269, 260, 263)
val fileInput = InputReader.readInputAsStringList("day01.dat")

fun main() {
    val intInput = fileInput.filter { it.isNotEmpty() }.map { it.toInt() }
    val increases = countIncreases(intInput)
    val slidingWindowIncs = slidingWindows(intInput)

    println("Had $increases increases")
    println("Had $slidingWindowIncs sliding windows increases")
}

fun countIncreases(measurements: List<Int>): Int {
    var increases = 0
    for (i in 1 until measurements.size) {
        if (measurements[i] > measurements[i - 1])
            increases++
    }
    return increases
}

fun slidingWindows(input: List<Int>): Int {
    var increases = 0
    val windows = getWindows(input)

    for (i in 1 until windows.size) {
        if (windows[i].size == 3 && windows[i].sum() > windows[i - 1].sum())
            increases++
    }

    return increases
}

fun getWindows(input: List<Int>): List<List<Int>> {
    val windows = mutableListOf<List<Int>>()
    for (i in input.indices) {
        if (i + 2 < input.size) {
            windows.add(listOf(input[i], input[i + 1], input[i + 2]))
        }
    }

    return windows
}