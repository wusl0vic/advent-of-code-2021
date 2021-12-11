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
    val riskLevel = lowPoints.sumOf { heightMap[it.first][it.second] + 1 }
    val basins = findBasins(heightMap)
    val largest = basins.sortedDescending().subList(0, 3).reduce { acc, i -> acc * i }
    println("The sum of the risk levels is $riskLevel")
    println("The product of the 3 largest basins is $largest")
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

fun findBasins(heightMap: Array<IntArray>): List<Int> {
    val basinSizes = mutableListOf<Int>()
    val lowPoints = findLowPoints(heightMap)
    for (p in lowPoints) {
        basinSizes.add(countBasins(heightMap, p))
    }

    return basinSizes
}

fun countBasins(heightMap: Array<IntArray>, point: Pair<Int, Int>): Int {
    val checked = mutableSetOf<Pair<Int, Int>>()
    checked.add(point)

    var toCheck = findAdjacents(heightMap, point.first, point.second) - checked
    while (toCheck.isNotEmpty()) {
        val inBasin = toCheck.filter { heightMap[it.first][it.second] < 9 }
        checked.addAll(inBasin)
        toCheck = inBasin.map { findAdjacents(heightMap, it.first, it.second) }.flatMap { it.toSet() }.toSet() - checked
    }

    return checked.size
}

fun findLowPoints(heightMap: Array<IntArray>): List<Pair<Int, Int>> {
    val lowPoints = mutableListOf<Pair<Int, Int>>()
    val rows = heightMap.size
    val cols = heightMap[0].size
    for (r in 0 until rows) {
        for (c in 0 until cols) {
            val current = heightMap[r][c]
            val adjacents = findAdjacents(heightMap, r, c)
            if (adjacents.all { current < heightMap[it.first][it.second] }) {
                lowPoints.add(Pair(r, c))
            }
        }
    }
    return lowPoints
}

fun findAdjacents(heightMap: Array<IntArray>, r: Int, c: Int): Set<Pair<Int, Int>> {
    val adjacents = mutableSetOf<Pair<Int, Int>>()
    if (r > 0) {
        adjacents.add(Pair(r - 1, c))
    }
    if (r + 1 < heightMap.size) {
        adjacents.add(Pair(r + 1, c))
    }
    if (c > 0) {
        adjacents.add(Pair(r, c - 1))
    }
    if (c + 1 < heightMap[0].size) {
        adjacents.add(Pair(r, c + 1))
    }
    return adjacents
}