package day13

import util.InputReader

data class Dot(val x: Int, val y: Int) {
    override fun toString(): String {
        return "($x,$y)"
    }
}

data class FoldInstruction(val along: Char, val pos: Int) {
    override fun toString(): String {
        return "$along=$pos"
    }
}

typealias Grid = Array<CharArray>

fun main() {

    val testInput = """
        6,10
        0,14
        9,10
        0,3
        10,4
        4,11
        6,0
        6,12
        4,1
        0,13
        10,12
        3,4
        3,0
        8,4
        1,10
        2,14
        8,10
        9,0

        fold along y=7
        fold along x=5
    """.trimIndent().split("\n")
    val input = InputReader.readInputAsStringList("day13.dat")

    val parsed = parseInput(input)
    var grid = createGrid(parsed.first)
//    printGrid(grid)

    val folded = fold(grid, parsed.second[0])
    val dots = countDots(folded)
    println("Had $dots dots after first fold")

    for (instr in parsed.second) {
        grid = fold(grid, instr)
    }
    printGrid(grid)

//    val folded = fold(grid, FoldInstruction('y', 7))
//    println("had ${countDots(folded)} dots after first fold")
//    println()
//    println()
//    printGrid(folded)
//
//    println()
//    println()
//    printGrid(fold(folded, FoldInstruction('x', 5)))
}

fun parseInput(input: List<String>): Pair<List<Dot>, List<FoldInstruction>> {
    val dots = mutableListOf<Dot>()
    val instructions = mutableListOf<FoldInstruction>()
    val folRegex = """fold along ([xy])=(\d+)""".toRegex()
    input.filter { it.isNotBlank() }.forEach { line ->
        if (line[0].isDigit()) {
            dots.add(Dot(line.split(",")[0].toInt(), line.split(",")[1].toInt()))
        } else {
            val (along, pos) = folRegex.find(line)!!.destructured
            instructions.add(FoldInstruction(along[0], pos.toInt()))
        }
    }
    return Pair(dots, instructions)
}

fun createGrid(dots: List<Dot>): Grid {
    val grid = Array(dots.maxOf { it.y } + 1) { CharArray(dots.maxOf { it.x  + 1}) { '.' } }
    dots.forEach { dot -> grid[dot.y][dot.x] = '#' }
    return grid
}

fun fold(grid: Grid, instruction: FoldInstruction): Grid =
    when (instruction.along) {
        'x' -> foldHorizontally(grid, instruction.pos)
        'y' -> foldVertically(grid, instruction.pos)
        else -> error("incorrect instruction")
    }

fun foldVertically(grid: Grid, pos: Int): Grid {
    val newGrid = Array(grid.size - pos - 1) { CharArray(grid[0].size) { '.' } }
    var i = grid.size - 1
    for (r in newGrid.indices) {
        for (c in grid[0].indices) {
            newGrid[r][c] = if (grid[r][c] == '#' || grid[i][c] == '#') '#' else '.'
        }
        i--
    }
    return newGrid
}

fun foldHorizontally(grid: Grid, pos: Int): Grid {
    val newGrid = Array(grid.size) { CharArray(grid[0].size - pos - 1) }
    var i = grid[0].size - 1
    for (r in newGrid.indices) {
        for (c in newGrid[0].indices) {
            newGrid[r][c] = if (grid[r][c] == '#' || grid[r][i] == '#') '#' else '.'
            i--
        }
        i = grid[0].size - 1
    }
    return newGrid
}

fun printGrid(grid: Grid) {
    for (r in grid.indices) {
        for (c in grid[0].indices) {
            print(grid[r][c])
        }
        println()
    }
}

fun countDots(grid: Grid) =
    grid.flatMap { it.toList() }.count { it == '#' }