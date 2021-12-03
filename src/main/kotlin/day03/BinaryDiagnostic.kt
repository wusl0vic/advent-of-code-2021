package day03

import util.InputReader

data class PowerConsumption(val ɣ: Int, val ε: Int) {
    override fun toString(): String {
        return "ɣ = $ɣ, ε = $ε -> consumption = ${ɣ * ε}"
    }
}

data class LifeSupportRating(val oxygenGeneratorRating: Int, val co2ScrubberRating: Int) {
    override fun toString(): String {
        return "LifeSupportRating: O2 generator rating ($oxygenGeneratorRating) * CO2ScrubberRating ($co2ScrubberRating) = ${oxygenGeneratorRating * co2ScrubberRating}"
    }
}

fun main() {
    val str = """
        00100
        11110
        10110
        10111
        10101
        01111
        00111
        11100
        10000
        11001
        00010
        01010
    """.trimIndent()
//    val input = str.lines()
    val input = InputReader.readInputAsStringList("day03.dat")
    println("${calculatePowerConsumption(input)}")

    println("${calculateLifeSupportRating(input)}")
}

fun calculatePowerConsumption(input: List<String>): PowerConsumption {
    val len = input[0].length
    val ones = IntArray(len)
    val zeroes = IntArray(len)

    for (line in input) {
        for (i in 0 until len) {
            when (line[i]) {
                '0' -> zeroes[i]++
                '1' -> ones[i]++
            }
        }
    }

    var gammaStr = ""
    for (i in 0 until len) {
        gammaStr += if (ones[i] > zeroes[i]) "1" else "0"
    }

    val gamma = gammaStr.toInt(2)
    // don't judge
    val epsilon = Integer.toBinaryString(gamma.inv()).takeLast(len).toInt(2)

    return PowerConsumption(gamma, epsilon)
}

fun calculateLifeSupportRating(input: List<String>): LifeSupportRating {
    val len = input[0].length
    var (zeroes, ones) = countBits(input)

    var oxyList = input
    for (i in 0 until len) {
        val mostCommon = if (ones[i] >= zeroes[i]) "1" else "0"
        oxyList = oxyList.filter { it[i].toString() == mostCommon }
        if (oxyList.size == 1) {
            break
        }
        val counts = countBits(oxyList)
        zeroes = counts.first
        ones = counts.second
    }

    var co2List = input;
    for (i in 0 until len) {
        val leastCommon = if (zeroes[i] <= ones[i]) "0" else "1"
        co2List = co2List.filter { it[i].toString() == leastCommon }
        if (co2List.size == 1)
            break
        val counts = countBits(co2List)
        zeroes = counts.first
        ones = counts.second
    }

    return LifeSupportRating(oxyList[0].toInt(2), co2List[0].toInt(2))
}

fun countBits(input: List<String>): Pair<IntArray, IntArray> {
    val len = input[0].length
    val zeroes = IntArray(len)
    val ones = IntArray(len)
    for (i in 0 until len ) {
        for (line in input) {
            when (line[i]) {
                '0' -> zeroes[i]++
                '1' -> ones[i]++
            }
        }
    }
    return Pair(zeroes, ones)
}