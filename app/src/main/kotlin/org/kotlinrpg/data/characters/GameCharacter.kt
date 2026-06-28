package org.kotlinrpg.data.characters

import org.kotlinrpg.data.*

abstract class GameCharacter(
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
        if (item is WearableItem) {
            // TODO:
        }
    }

    fun heal(amount: Int) {
        health = (health + amount).coerceAtMost(maxHealth)
    }

    fun energize(amount: Int) {
        energy = (energy + amount).coerceAtMost(maxEnergy)
    }
}
