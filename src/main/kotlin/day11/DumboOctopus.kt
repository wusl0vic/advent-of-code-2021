package day11

import util.InputReader

typealias Grid = Array<IntArray>

fun main() {
    val testInput = """
        5483143223
        2745854711
        5264556173
        6141336146
        6357385478
        4167524645
        2176841721
        6882881134
        4846848554
        5283751526
    """.trimIndent().split("\n")

    val testInput2 = """
        11111
        19991
        19191
        19991
        11111
    """.trimIndent().split("\n")

    val input = InputReader.readInputAsStringList("day11.dat");

    val grid = parseInput(input)
    val res = simulate(grid, 100)
    val sync = findSynchronized(parseInput(input))
    println("Had $res flashes, synchronized after step $sync")
}

fun parseInput(input: List<String>): Grid {
    val grid = Array(input.size) { IntArray(input[0].length) }
    input.forEachIndexed { i, line ->
        grid[i] = line.map { it.toString().toInt() }.toIntArray()
    }
    return grid
}

fun simulate(grid: Grid, steps: Int): Int {
    var flashes = 0
    for (i in 1 .. steps) {
        flashes += executeStep(grid)
    }
    return flashes
}

fun findSynchronized(grid: Grid): Int {
    var step = 0
    var synchronized = false
    while (!synchronized) {
        step++
        executeStep(grid)
        synchronized = grid.flatMap { it.asList() }.all { it == 0 }
    }
    return step
}

fun executeStep(grid: Grid): Int {
    val flashed = mutableSetOf<Pair<Int, Int>>()
    var res = 0

    for (r in grid.indices) {
        for (c in grid[0].indices) {
            if (++grid[r][c] > 9) {
                flashed.add(Pair(r, c))
            }
        }
    }

    flash(grid, flashed)

    for (r in grid.indices) {
        for (c in grid[0].indices) {
            if (grid[r][c] > 9) {
                res++
                grid[r][c] = 0
            }
        }
    }
    return res
}

fun flash(grid: Grid, flashed: MutableSet<Pair<Int, Int>>) {
    var toFlash = flashed.toMutableSet()
    while (toFlash.isNotEmpty()) {
        val tmp = mutableSetOf<Pair<Int, Int>>()
        for ((i, j) in toFlash) {
            findAdjacents(grid, i, j).forEach { grid[it.first][it.second]++ }
            tmp.addAll(findAdjacents(grid, i, j).filter { grid[it.first][it.second] == 10 })
        }
        toFlash = tmp
    }
}

fun findAdjacents(grid: Grid, r: Int, c: Int): Set<Pair<Int, Int>> {
    val adjacents = mutableSetOf<Pair<Int, Int>>()
    if (r > 0) {
        adjacents.add(Pair(r - 1, c))
    }
    if (r + 1 < grid.size) {
        adjacents.add(Pair(r + 1, c))
    }
    if (c > 0) {
        adjacents.add(Pair(r, c - 1))
    }
    if (c + 1 < grid[0].size) {
        adjacents.add(Pair(r, c + 1))
    }
    if (r > 0 && c + 1 < grid[0].size) {
        adjacents.add(Pair(r - 1, c + 1))
    }
    if (c + 1 < grid[0].size && r + 1 < grid.size) {
        adjacents.add(Pair(r + 1, c + 1))
    }
    if (r > 0 && c > 0) {
        adjacents.add(Pair(r - 1, c - 1))
    }
    if (r + 1 < grid.size && c > 0) {
        adjacents.add(Pair(r + 1, c - 1))
    }
    return adjacents
}