package org.kotlinrpg.data

import org.kotlinrpg.data.characters.GameCharacter

data class GameAction(
    val key: Char,
    val name: String,
    val allowed: Boolean = true,
    val execute: () -> Unit
)

data class GameAttack(
    val name: String,
    val energy: Int,
    val message: GameMessage?,
    val attack: (GameCharacter) -> GameMessage, // Returns an overview of the attack's effect
)

interface Item {
    val name: String
    val description: String
    val price: Float
}

data class UseableItem(
    override val name: String,
    override val description: String,
    override val price: Float,
    val apply: (GameCharacter) -> Unit
) : Item

data class WearableItem(
    override val name: String,
    override val description: String,
    override val price: Float,
    var isWorn: Boolean,
    val wear: (GameCharacter) -> Unit,
    val takeOff: (GameCharacter) -> Unit
) : Item
