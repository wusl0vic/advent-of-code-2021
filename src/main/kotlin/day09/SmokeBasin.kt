package day09

import util.InputReader

fun main() {
    val testInput = """
        2199943210
        3987894921
        9856789892
        8767896789
        9899965678
    """.trimIndent().split("\n")
    val input = InputReader.readInputAsStringList("day09.dat")

    val heightMap = createHeightMap(input)
    val lowPoints = findLowPoints(heightMap)
    val riskLevel = lowPoints.sumOf { it + 1 }
    println("The sum of the risk levels is $riskLevel")
}

fun createHeightMap(input: List<String>): Array<IntArray> {
    val rows = input.size
    val cols = input[0].length
    val heightMap = Array(rows) { IntArray(cols) }

    input.forEachIndexed { i, line ->
        line.forEachIndexed { j, c -> heightMap[i][j] = c.digitToInt() }
    }

    return heightMap
}

fun findLowPoints(heightMap: Array<IntArray>): List<Int> {
    val lowPoints = mutableListOf<Int>()
    val rows = heightMap.size
    val cols = heightMap[0].size
    for (r in 0 until rows) {
        for (c in 0 until cols) {
            val current = heightMap[r][c]
            val adjacents = findAdjacents(heightMap, r, c)
            if (adjacents.all { current < it }) {
                lowPoints.add(current)
            }
        }
    }
    return lowPoints
}

fun findAdjacents(heightMap: Array<IntArray>, r: Int, c: Int): Set<Int> {
    val adjacents = mutableSetOf<Int>()
    if (r > 0) {
        adjacents.add(heightMap[r - 1][c])
    }
    if (r + 1 < heightMap.size) {
        adjacents.add(heightMap[r + 1][c])
    }
    if (c > 0) {
        adjacents.add(heightMap[r][c - 1])
    }
    if (c + 1 < heightMap[0].size) {
        adjacents.add(heightMap[r][c + 1])
    }
    return adjacents
}