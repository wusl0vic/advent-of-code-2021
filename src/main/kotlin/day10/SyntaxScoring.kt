package day10

import util.InputReader

val openSyms = setOf('(', '[', '{', '<')
val closeSyms = setOf(')', ']', '}', '>')

typealias Stack<T> = MutableList<T>
inline fun <T> Stack<T>.push(item: T) = add(item)
fun <T> Stack<T>.pop(): T? = if (isNotEmpty()) removeAt(lastIndex) else null
fun <T> Stack<T>.peek(): T? = if (isNotEmpty()) this[lastIndex] else null

fun main() {
    val testInput = """
        [({(<(())[]>[[{[]{<()<>>
        [(()[<>])]({[<{<<[]>>(
        {([(<{}[<>[]}>{[]{[(<()>
        (((({<>}<{<{<>}{[]{[]{}
        [[<[([]))<([[{}[[()]]]
        [{[{({}]{}}([{[{{{}}([]
        {<[[]]>}<{[{[{[]{()[[[]
        [<(<(<(<{}))><([]([]()
        <{([([[(<>()){}]>(<<{{
        <{([{{}}[<[[[<>{}]]]>[]]
    """.trimIndent().split("\n")
    val input = InputReader.readInputAsStringList("day10.dat")

    val score = calculateSyntaxErrorScore(input)
    println("Syntax error score = $score")
}

fun calculateSyntaxErrorScore(input: List<String>): Long {
    val corrupted = findCorruptedChars(input)
    var score = 0L
    corrupted.forEach { c ->
        score += when(c) {
            ')' -> 3
            ']' -> 57
            '}' -> 1197
            '>' -> 25137
            else -> 0
        }
    }
    return score
}

fun findCorruptedChars(input: List<String>): List<Char> {
    val corrupted = mutableListOf<Char>()
    input.forEach { line ->
        val c = checkLine(line)
        if (c != null) {
            corrupted.add(c)
        }
    }
    return corrupted
}

fun checkLine(line: String): Char? {
    val open: Stack<Char> = mutableListOf()
    var corruption: Char? = null
    for (c in line) {
        val prev = open.peek()
        if (prev != null && closeSyms.contains(c)) {
            val ok = when (prev) {
                '(' -> c == ')'
                '[' -> c == ']'
                '{' -> c == '}'
                '<' -> c == '>'
                else -> false
            }
            if (ok) {
                open.pop()
            } else {
                corruption = c
                break
            }
        } else {
            open.push(c)
        }
    }
    return corruption
}