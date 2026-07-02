package org.kotlinrpg.game

import org.kotlinrpg.rendering.*
import org.kotlinrpg.data.*

val shopMessage = GameMessage(Constants.SHOP_ASCII, TextColor.YELLOW)

class ShopState(
    sceneRenderer: SceneRenderer,
    onFinish: (nextState: State?) -> Unit,
    returnState: State,
    val player: Player
) : State(sceneRenderer, onFinish, returnState) {
    override val actions = mutableListOf<GameAction>(
        GameAction('s', "Shop Items", true, ::shopItem),
        GameAction('f', "Finish Shopping", true, ::finishShopping)
    )

    override val sceneMessage = shopMessage

    val items = Items.all

    fun shopItem() {
        GameMessage("You have ${player.cash}$ to spend.", TextColor.GREEN).printFormatted()

        items.forEachIndexed { index, it ->
            if (it is WearableItem && it.isWorn) return@forEachIndexed
            GameMessage("${index + 1} - ${it.price}$. ${it.name} - ${it.description}${if (it is WearableItem) " WEARABLE" else ""}").printFormatted()
        }

        GameMessage("Select an item out of the list (by index): ").printFormatted()
        val item = readlnOrNull()?.toIntOrNull()
        if (item != null) buyItem(items[item - 1])
        else GameMessage("Item not found.").printFormatted()

    }

    fun finishShopping() = terminate(returnState)

    fun buyItem(item: Item) {
        if (player.cash < item.price) {
            GameMessage("You don't have enough cash!", TextColor.RED)
            return
        }

        player.cash -= item.price
        player.addItem(item)
    }
}
