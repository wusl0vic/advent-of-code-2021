package day05

import util.InputReader
import java.lang.Integer.max
import java.lang.Integer.min

data class Point(val x: Int, val y: Int) {
    constructor(x: String, y: String): this(x.toInt(), y.toInt())
}
data class Line(val a: Point, val b: Point) {
    fun minX(): Int = min(a.x, b.x)
    fun minY(): Int = min(a.y, b.y)
    fun maxX(): Int = max(a.x, b.x)
    fun maxY(): Int = max(a.y, b.y)
}

fun main() {
    val testInput = """
        0,9 -> 5,9
        8,0 -> 0,8
        9,4 -> 3,4
        2,2 -> 2,1
        7,0 -> 7,4
        6,4 -> 2,0
        0,9 -> 2,9
        3,4 -> 1,4
        0,0 -> 8,8
        5,5 -> 8,2
    """.trimIndent().split("\n")
    val input = InputReader.readInputAsStringList("day05.dat")
    val lines = readLines(input)

    solvePart1(lines)
    solvePart2(lines)
}

fun readLines(input: List<String>): List<Line> {
    val regex = """(\d+),(\d+) -> (\d+),(\d+)""".toRegex()
    val lines = mutableListOf<Line>()
    for (line in input) {
        val (x1, y1, x2, y2) = regex.find(line)!!.destructured
        lines.add(Line(Point(x1, y1), Point(x2, y2)))
    }
    return lines
}

fun solvePart1(lines: List<Line>) {
    val field = calculateField(lines)
//    printField(field)
    val overlaps = countOverlaps(field)
    println("Part 1: had $overlaps overlaps")
}

fun solvePart2(lines: List<Line>) {
    val field = calculateField(lines, true)
//    printField(field)
    val overlaps = countOverlaps(field)

    println("Part 2: Had $overlaps overlaps")
}

fun calculateField(lines: List<Line>, part2: Boolean = false): Array<IntArray> {
    val maxX = lines.maxOf { line -> line.maxX() }
    val maxY = lines.maxOf { line -> line.maxY() }
    val field = Array(maxX + 1) { IntArray(maxY + 1) }

    for (line in lines) {
        if (line.a.x == line.b.x) {
            val from = line.minY()
            val to = line.maxY()
            for (i in from .. to) {
                field[i][line.a.x]++
            }
        } else if (line.a.y == line.b.y) {
            val from = line.minX()
            val to = line.maxX()
            for (i in from .. to) {
                field[line.a.y][i]++
            }
        } else if (part2) {
            val yRange = if (line.a.y > line.b.y) line.a.y.downTo(line.b.y) else line.a.y.rangeTo(line.b.y)
            val xRange = if (line.a.x > line.b.x) line.a.x.downTo(line.b.x) else line.a.x.rangeTo(line.b.x)
            val count = yRange.count()
            val yIt = yRange.iterator()
            val xIt = xRange.iterator()
            for (i in 0 until count) {
                field[yIt.nextInt()][xIt.nextInt()]++
            }
        }
    }

    return field
}

fun countOverlaps(field: Array<IntArray>): Int {
    var overlaps = 0
    for (lines in field) {
        for (line in lines) {
            if (line >= 2)
                overlaps++
        }
    }
    return overlaps
}

fun printField(field: Array<IntArray>) {
    for (lines in field) {
        for (line in lines) {
            print("$line ")
        }
        println()
    }
}