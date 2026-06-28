package org.kotlinrpg.data

object Items {
    val SWORD = WearableItem(
        "Simple Sword", "+10 ATK", 15f, false,
        { it.damage += 10 }, { it.damage -= 10 })
    val SHIELD = WearableItem(
        "Simple Shield", "+20 HP", 15f, false,
        { it.maxHealth += 20 }, { it.maxHealth -= 10 })

    val HEALTH_POTION = UseableItem("Basic Health Potion", "+20 HP", 10f)
    { it.heal(20) }
    val BIG_HEALTH_POTION = UseableItem("Big Health Potion", "+50 HP", 20f)
    { it.heal(50) }

    var all: List<Item> = listOf(
        SWORD,
        SHIELD,
        HEALTH_POTION,
        BIG_HEALTH_POTION
    )
}
