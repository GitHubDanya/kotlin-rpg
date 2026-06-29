package org.kotlinrpg.data.characters

import org.kotlinrpg.data.*
import org.kotlinrpg.game.*

class Goblin(
    name: String = "GOBLIN",
    maxHealth: Int = 25,
    maxEnergy: Int = 30,
    damage: Int = 10,
    clumsiness: Float = 0.2f,
    val player: Player
) : GameEnemy(name, maxHealth, maxEnergy, damage, clumsiness) {
    override var attacks = mutableListOf<GameAttack>(
        GameAttack("Bite", 10, GameMessage("You got bit!")) {
            var damageAmount = generateDamageAmount(10)
            player.damage(damageAmount)
            return@GameAttack GameMessage("-${damageAmount}HP", TextColor.RED)
        },
        GameAttack("Rest", 10, null) {
            this.heal(10)
            return@GameAttack GameMessage("+10HP", TextColor.YELLOW)
        }
    )
}

class Troll(
    name: String = "TROLL",
    maxHealth: Int = 50,
    maxEnergy: Int = 20,
    damage: Int = 10,
    clumsiness: Float = 0.4f,
    val player: Player
) : GameEnemy(name, maxHealth, maxEnergy, damage, clumsiness) {
    override var attacks = mutableListOf<GameAttack>(
        GameAttack("Sneeze", 5, GameMessage("Some snot hit you!")) {
            var damageAmount = generateDamageAmount(5)
            player.damage(damageAmount)
            return@GameAttack GameMessage("-${damageAmount}HP", TextColor.RED)
        },
        GameAttack("Growl", 10, GameMessage("Your ears start ringing!")) {
            player.clumsiness += 0.05f
            return@GameAttack GameMessage("+0.05 clumsiness", TextColor.YELLOW)
        }
    )
}
