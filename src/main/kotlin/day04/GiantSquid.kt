package day04

import util.InputReader

data class BingoNumber(val nr: Int, var marked: Boolean)

fun main() {

    val testInput = """
7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1

22 13 17 11  0
 8  2 23  4 24
21  9 14 16  7
 6 10  3 18  5
 1 12 20 15 19

 3 15  0  2 22
 9 18 13 17  5
19  8  7 25 23
20 11 10 24  4
14 21 16 12  6

14 21 17 24  4
10 16 15  9 19
18  8 23 26 20
22 11 13  6  5
 2  0 12  3  7
    """.trimIndent()

//    val bingoNrs = readBingoInput(testInput.split("\n"))
//    val boards = readBoards(testInput.split("\n"))

    val input = InputReader.readInputAsStringList("day04.dat")
    val bingoNrs = readBingoInput(input)
    val boards = readBoards(input)

    val score = solvePart1(bingoNrs, boards)
    val losingScore = solvePart2(bingoNrs, boards)

    println("the score is $score")
    println("the score of the last winning board is $losingScore")
}

fun solvePart1(bingoNumbers: List<Int>, boards: List<Array<Array<BingoNumber>>>): Int {
    for (nr in bingoNumbers) {
        markBoards(nr, boards)
        val winningBoard = checkForBingo(boards)
        if (winningBoard != null) {
            return nr * countUnmarkedNumbers(winningBoard)
        }
    }
    return -1
}

fun solvePart2(bingoNumbers: List<Int>, boards: List<Array<Array<BingoNumber>>>): Int {
    resetBoards(boards)
    val winMap = mutableMapOf<Int, Array<Array<BingoNumber>>>()
    for (board in boards) {
        for (nr in bingoNumbers) {
            markBoard(nr, board)
            val winningBoard = checkForBingo(board)
            if (winningBoard != null) {
                winMap[bingoNumbers.indexOf(nr)] = winningBoard
                break
            }
        }
        resetBoard(board)
    }
    val i = winMap.maxOf { it.key }
    resetBoards(boards)
    val board = winMap[i]
    if (board != null) {
        for (nr in bingoNumbers) {
            markBoard(nr, board)
            val winning = checkForBingo(board)
            if (winning != null) {
                return bingoNumbers[i] * countUnmarkedNumbers(board)
            }
        }
    }
    return bingoNumbers[i] * countUnmarkedNumbers(winMap[i])
}

fun resetBoards(boards: List<Array<Array<BingoNumber>>>) {
    for (board in boards)
        resetBoard(board)
}

fun resetBoard(board: Array<Array<BingoNumber>>) = board.flatten().forEach { it.marked = false }

fun checkForBingo(boards: List<Array<Array<BingoNumber>>>): Array<Array<BingoNumber>>? {
    for (board in boards) {
        val winning = checkForBingo(board)
        if (winning != null)
            return winning
    }
    return null
}

fun checkForBingo(board: Array<Array<BingoNumber>>): Array<Array<BingoNumber>>? {
    for (r in 0..4) {
        var horizontalWin = true
        var verticalWin = true
        for (c in 0..4) {
            horizontalWin = horizontalWin && board[r][c].marked
            verticalWin = verticalWin && board[c][r].marked
        }
        if (horizontalWin || verticalWin)
            return board
    }
    return null
}

fun countUnmarkedNumbers(board: Array<Array<BingoNumber>>?): Int {
    return board?.flatten()?.filter { !it.marked }?.sumOf { it.nr } ?: 0
}

fun markBoards(number: Int, boards: List<Array<Array<BingoNumber>>>) {
    for (board in boards) {
        markBoard(number, board)
    }
}

fun markBoard(number: Int, board: Array<Array<BingoNumber>>) {
    for (r in 0..4) {
        for (c in 0..4) {
            if (board[r][c].nr == number) {
                board[r][c].marked = true
            }
        }
    }
}

fun readBingoInput(input: List<String>): List<Int> {
    return input[0].split(",").map { it.toInt() }
}

fun readBoards(input: List<String>): List<Array<Array<BingoNumber>>> {
    val boards = mutableListOf<Array<Array<BingoNumber>>>()
    var j = 0
    var board = Array(5) { arrayOf<BingoNumber>() }
    for (i in 2 until input.size) {
        val line = input[i]
        if (line.isEmpty()) {
            boards.add(board)
            j = 0
            board = Array(5) { arrayOf() }
        } else {
            val row = line.split(" ").filter { it.isNotEmpty() }.map { BingoNumber(it.toInt(), false) }.toTypedArray()
            board[j++] = row
        }
    }
    boards.add(board)

    return boards
}