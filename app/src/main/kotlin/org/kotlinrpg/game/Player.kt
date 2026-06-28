package org.kotlinrpg.game

import kotlin.math.max
import org.kotlinrpg.data.*
import org.kotlinrpg.data.characters.GameCharacter

class Player(
    maxHealth: Int,
    maxEnergy: Int,
    damage: Int,
    clumsiness: Float,
    var cash: Float = 0f,
    var upgradePoints: Int = 0
) : GameCharacter(maxHealth, maxEnergy, damage, clumsiness) {
}
