package day12

import util.InputReader


typealias Vertex = Map.Entry<String, List<String>>
typealias Graph = Map<String, Vertex>
typealias Path = List<String>

fun main() {
    val testInput = """
        start-A
        start-b
        A-c
        A-b
        b-d
        A-end
        b-end
    """.trimIndent().split("\n")

    val graph = parseInput(InputReader.readInputAsStringList("day12.dat"))
    val paths = graph.findPaths(allowedDoubleVisitsOfSmallCaves = 1)
    println("Found ${paths.size} paths")
}

fun Graph.findPaths(node: String = "start", path: Path = listOf(), allowedDoubleVisitsOfSmallCaves: Int): List<Path> =
    when {
        node == "end" -> listOf(path + node)
        node == "start" && path.isNotEmpty() -> listOf()
        noMoreVisit(path, node, allowedDoubleVisitsOfSmallCaves) -> listOf()
        else -> get(node)!!.value.flatMap { findPaths(it, path + node, allowedDoubleVisitsOfSmallCaves) }
    }

fun noMoreVisit(path: Path, node: String, allowedDoubleVisitsOfSmallCaves: Int) =
    path.contains(node) && node[0].isLowerCase() &&
            path.groupingBy { it }.eachCount().count { it.key[0].isLowerCase() && it.value >= 2 } == allowedDoubleVisitsOfSmallCaves

fun parseInput(input: List<String>): Graph {
    return input.map { it.trim().split("-") } // ((start), (A))
        .flatMap { listOf(it, it.reversed()) } // ((start, A), (A, start))
        .groupBy({it[0]}, {it[1]}) // { start -> (A), A -> (start) }
        .mapValues { it }
}