package org.kotlinrpg.game

import org.kotlinrpg.rendering.*
import org.kotlinrpg.data.*

const val shopAscii = """
   _....._
  ';-.--';'
    }===={
  .'  _|_ '.
 /:: (_|_`  \
|::  ,_|_)   |
\::.   |     /
 '::_     _-'
     `````

WELCOME TO THE SHOP!
"""

val shopMessage = GameMessage(shopAscii, TextColor.YELLOW)

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

    val items = Items.all

    override val sceneMessage = shopMessage

    fun shopItem() {
        GameMessage("You have ${player.cash}$ to spend.", TextColor.GREEN).printFormatted()

        items.forEachIndexed { index, it ->
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
