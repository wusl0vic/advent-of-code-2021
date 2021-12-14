package day14

import util.InputReader
import java.math.BigInteger

data class Rule(val adj: String, val ins: Char) {
    override fun toString(): String {
        return "$adj -> $ins"
    }
}

fun main() {
    val testInput = """
        NNCB

        CH -> B
        HH -> N
        CB -> H
        NH -> C
        HB -> C
        HC -> B
        HN -> C
        NN -> C
        BH -> H
        NC -> B
        NB -> B
        BN -> B
        BB -> N
        BC -> B
        CC -> N
        CN -> C
    """.trimIndent().split("\n")
    val input = InputReader.readInputAsStringList("day14.dat")

    val template = getTemplate(input)
    val rules = getRules(input)

//    val polymer = execute(template, rules, 40)
//    val counts = polymer.groupingBy { it }.eachCount()
//    val max = counts.maxOf { it.value }
//    val min = counts.minOf { it.value }

    val counts = applyRules2(template, rules, 40)
    val max = counts.maxOf { it.value }
    val min = counts.minOf { it.value }

    println("$max - $min = ${max - min}")

}

fun getTemplate(input: List<String>): String = input[0]

fun getRules(input: List<String>): List<Rule> {
    val ruleRegex = """([A-Z]{2}) -> ([A-Z])""".toRegex()
    return input.drop(2).map { Rule(ruleRegex.find(it)!!.destructured.component1(), ruleRegex.find(it)!!.destructured.component2()[0]) }
}

fun execute(polymer: String, rules: List<Rule>, steps: Int) : String {
    var res = polymer
    for (i in 1..steps) {
        res = applyRules(res, rules)
    }
    return res
}

fun applyRules2(template: String, rules: List<Rule>, steps: Int): Map<Char, Long> {
    val pairCounts = mutableMapOf<String, Long>()
    val charCounts = mutableMapOf<Char, Long>()

    template.forEach { charCounts[it] = charCounts.getOrDefault(it, 0) + 1 }
    rules.forEach { rule ->
        val i = template.windowed(2) {
            if (it == rule.adj)
                1
            else
                0
        }.sum()
        pairCounts[rule.adj] = i.toLong()
    }

    for (i in 1..steps) {
        val toAdd = mutableMapOf<Rule, Long>()
        rules.forEach {
            if (pairCounts[it.adj]!! > 0) {
                toAdd[it] = pairCounts[it.adj]!!
            }
        }
        toAdd.forEach {
            pairCounts[it.key.adj] = pairCounts[it.key.adj]!! - it.value
            pairCounts["" + it.key.adj[0] + it.key.ins] = pairCounts["" + it.key.adj[0] + it.key.ins]!! + it.value
            pairCounts["" + it.key.ins + it.key.adj[1]] = pairCounts["" + it.key.ins + it.key.adj[1]]!! + it.value
            charCounts[it.key.ins] = charCounts.getOrDefault(it.key.ins, 0) + it.value
        }
    }
    return charCounts
}

fun applyRules(polymer: String, rules: List<Rule>): String {
    var updated = StringBuilder(polymer)
    val matchingRules = rules.filter { polymer.contains(it.adj) }
    var i = 0
    var j = 1
    var startIndex = 0
    while (j < updated.length) {
        if (matchingRules.map { it.adj }.contains(updated.substring(i, j + 1))) {
            val rule = matchingRules.find { it.adj == updated.substring(i, j + 1) }
            if (rule != null) {
                updated = updated.insert(updated.indexOf(rule.adj, startIndex) + 1, rule.ins)
                i++
                j++
            }
        }
        startIndex = j
        i++
        j++
    }
    return updated.toString()
}