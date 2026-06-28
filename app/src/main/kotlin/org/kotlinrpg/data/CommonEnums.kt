package org.kotlinrpg.data

enum class TextColor(val ansiCode: String) {
    WHITE("\u001B[0m"),
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    CYAN("\u001B[36m")
}

enum class GameState {
    BATTLE,
    REST,
}

data class GameMessage(
    val text: String,
    val color: TextColor = TextColor.WHITE
) {
    fun printFormatted() {
        println("${color.ansiCode}$text\u001B[0m")
    }
}
