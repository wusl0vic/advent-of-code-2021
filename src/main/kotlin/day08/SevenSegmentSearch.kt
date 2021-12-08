package day08

import util.InputReader

data class Entry(val signalPattern: List<String>, val outputValue: List<String>)


fun main() {
    val testInput = """
        be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
        edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
        fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
        fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
        aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
        fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
        dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
        bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
        egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
        gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce
    """.trimIndent()

    val testPatterns = parseInput(testInput.split("\n"))
    val patterns = parseInput(InputReader.readInputAsStringList("day08.dat"))

    val easyDigits = countEasyDigits(patterns)
    println("has $easyDigits easy digits")

    val wiring = parseWiring(testPatterns[0].signalPattern)

    val sum = solvePart2(patterns)
    println("part 2 = $sum")
}

fun parseInput(input: List<String>): List<Entry> {
    val entries = mutableListOf<Entry>()
    input.map { it.split(" | ") }
        .forEach { pair -> entries.add(Entry(pair[0].split(" "), pair[1].split(" "))) }
    return entries
}

fun countEasyDigits(entries: List<Entry>): Int {
    var ones = 0
    var fours = 0
    var sevens = 0
    var eights = 0
    entries.map { it.outputValue }
        .forEach { output ->
            output.forEach { value ->
                when(value.length) {
                    2 -> ones++
                    4 -> fours++
                    3 -> sevens++
                    7 -> eights++
                }
            }
        }
    return ones + fours + sevens + eights
}

fun parseSignalPattern(signalPattern: List<String>): CharArray {
    val digit = CharArray(10)
    val rhs = mutableSetOf<Char>()
    val lhs = mutableSetOf<Char>()
    signalPattern.filter { it.length == 2 || it.length == 3 }
        .sortedBy { it.length }
        .forEach { signal ->
            when (signal.length) {
                2 -> {
                    rhs.add(signal[0])
                    rhs.add(signal[1])
                }
                3 -> {
                    signal.forEach { c ->
                        if (!rhs.contains(c)) {
                            digit[0] = c
                        }
                    }
                }
            }
        }

    return digit
}

fun parseWiring(entry: List<String>): Map<Int, Set<Char>> {
    val wiring = mutableMapOf<Int, Set<Char>>()
    val chars = entry.map { it.toSet() }

    wiring[1] = chars.filter { it.size == 2 }.toList()[0]
    wiring[4] = chars.filter { it.size == 4 }.toList()[0]
    wiring[7] = chars.filter { it.size == 3 }.toList()[0]
    wiring[8] = chars.filter { it.size == 7 }.toList()[0]

    wiring[3] = chars.filter { it.size == 5 }
        .filter { wiring[4]!!.intersect(it.toSet()).size == 3 }
        .filter { wiring[4]!!.intersect(it.toSet()).intersect(wiring[1]!!).size == 2 }.toList()[0]
    wiring[2] = chars.filter { it.size == 5 }
        .filter { wiring[4]!!.intersect(it.toSet()).size == 2 }.toList()[0]
    wiring[5] = chars.filter { it.size == 5 }
        .filter { wiring[4]!!.intersect(it.toSet()).size == 3 }
        .filter { wiring[4]!!.intersect(it.toSet()).intersect(wiring[1]!!).size == 1 }.toList()[0]

    wiring[0] = chars.filter { it.size == 6 }
        .filter { (it.toSet() - wiring[1]!!).size == 4 }
        .filter { (it.toSet() - wiring[4]!!).size == 3 }.toList()[0]
    wiring[6] = chars.filter { it.size == 6 }
        .filter { (it.toSet() - wiring[1]!!).size == 5 }.toList()[0]
    wiring[9] = chars.filter { it.size == 6 }
        .filter { (it.toSet() - wiring[1]!!).size == 4 }
        .filter { (it.toSet() - wiring[4]!!).size == 2 }.toList()[0]

    return wiring
}

fun solvePart2(input: List<Entry>): Long {
    var res = 0L
    input.forEach { entry ->
        val wiring = parseWiring(entry.signalPattern)
        res += calculateOutputValue(entry.outputValue, wiring)
    }
    return res
}

fun calculateOutputValue(outputs: List<String>, wiring: Map<Int, Set<Char>>): Int {
    var digitStr = ""
    outputs.forEach { output -> digitStr += calculateDigit(output, wiring).toString() }
    return digitStr.toInt()
}

fun calculateDigit(output: String, wiring: Map<Int, Set<Char>>): Int {
    return wiring.filterValues { it == output.toSet() }.keys.toList()[0]
}