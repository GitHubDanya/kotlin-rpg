package org.kotlinrpg.game

import kotlin.math.max
import org.kotlinrpg.data.*
import org.kotlinrpg.data.characters.GameCharacter

class Player(
    name: String = "Player",
    maxHealth: Int,
    maxEnergy: Int,
    damage: Int,
    clumsiness: Float,
    var cash: Float = 0f,
    var upgradePoints: Int = 0,
    var level: Int = 1,
) : GameCharacter(name, maxHealth, maxEnergy, damage, clumsiness) {

    constructor(other: Player) : this(
        maxHealth = other.maxHealth,
        maxEnergy = other.maxEnergy,
        damage = other.damage,
        clumsiness = other.clumsiness,
        cash = other.cash,
        upgradePoints = other.upgradePoints,
        level = other.level
    ) {
        this.health = other.health
        this.energy = other.energy
    }

    fun showStats() {
        GameMessage("YOUR STATS:").printFormatted()
        GameMessage("LEVEL ${level}\n").printFormatted()
        val healthColor = getRelationalColor(health, maxHealth)
        val energyColor = getRelationalColor(energy, maxEnergy)
        GameMessage("Health: ${health}/${maxHealth}", healthColor).printFormatted()
        GameMessage("Energy: ${energy}/${maxEnergy}\n", energyColor).printFormatted()

        GameMessage("Damage: $damage").printFormatted()
        GameMessage("Clumsiness: ${clumsiness}\n").printFormatted()

        GameMessage("Cash: $cash").printFormatted()
    }

    private fun getRelationalColor(x: Int, max: Int): TextColor {
        if (max == 0) return TextColor.WHITE

        val ratio = x.toFloat() / max

        return when {
            ratio >= 1f -> TextColor.CYAN
            ratio >= 0.5f -> TextColor.WHITE
            ratio >= 0.3f -> TextColor.YELLOW
            ratio >= 0f -> TextColor.RED
            else -> TextColor.WHITE
        }
    }
}
