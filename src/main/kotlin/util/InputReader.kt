package util

import java.io.File

class InputReader {
    companion object {
        fun readInputAsStringList(fileName: String): List<String> {
            val file = javaClass.classLoader.getResource(fileName).readText()
            return file.lines()
        }
    }


}