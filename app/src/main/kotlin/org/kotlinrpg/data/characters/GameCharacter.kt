package org.kotlinrpg.data.characters

import org.kotlinrpg.data.*
import kotlin.random.*

abstract class GameCharacter(
    val name: String,
    var maxHealth: Int,
    var maxEnergy: Int,
    var damage: Int,
    var clumsiness: Float
) {
    var health: Int = maxHealth
    var energy: Int = maxEnergy

    var wearables = mutableListOf<WearableItem>()
    var usables = mutableListOf<UseableItem>()

    fun addItem(item: Item) {
        when (item) {
            is WearableItem -> {
                wearables.addLast(item)
            }

            is UseableItem -> {
                usables.addLast(item)
            }
        }
    }

    fun generateDamageAmount(attackAmount: Int = damage): Int {
        val errorFactor = (damage * clumsiness).toInt()
        return ((damage - errorFactor)..(damage + errorFactor)).random()
    }

    fun damage(amount: Int) {
        health -= amount
    }

    fun heal(amount: Int) {
        health = (health + amount).coerceAtMost(maxHealth)
    }

    fun energize(amount: Int) {
        energy = (energy + amount).coerceAtMost(maxEnergy)
    }
}

abstract class GameEnemy(
    name: String,
    maxHealth: Int,
    maxEnergy: Int,
    damage: Int,
    clumsiness: Float
) : GameCharacter(name, maxHealth, maxEnergy, damage, clumsiness) {
    abstract var attacks: MutableList<GameAttack>

    fun getAvailAttackAtRandom(): GameAttack?
    {
        return attacks
        .filter { it.energy <= energy }
        .randomOrNull()
    }
}
