package org.kotlinrpg.game

import org.kotlinrpg.data.*
import org.kotlinrpg.rendering.*

const val restAscii = """
           x
.-. _______|
|=|/     /  \
| |_____|_""_|
|_|_[X]_|____|

RESTING...
"""

val restMessage = GameMessage(restAscii)

const val MEAL_PRICE = 10
const val MEAL_HEALTH_GAIN = 40
const val MEAL_ENERGY_GAIN = 40

class RestState(
    sceneRenderer: SceneRenderer,
    onFinish: (nextState: State?) -> Unit,
    val player: Player,
) : State(sceneRenderer, onFinish) {
    override val actions = mutableListOf<GameAction>(
        GameAction('a', "Stats", true, ::showStats),
        GameAction('e', "Buy Some Food", true, ::buyFood),
        GameAction('s', "Open Shop", true, ::openShop),
        GameAction('u', "Upgrade Skills", true, ::upgradeSkills),
        GameAction('i', "Inventory", true, ::manageInventory),
        GameAction('n', "Finish Resting", true, ::finishResting),
    )

    override val sceneMessage = restMessage

    override fun enter() {
        sceneRenderer.clearConsole()
        sceneRenderer.renderScene(this)

        if (player.upgradePoints != 0) GameMessage(
            "You have unspent upgarde points! Spend them by pressing [u]\n",
            TextColor.CYAN
        ).printFormatted()

        sceneRenderer.showActions(this.actions)
        sceneTerminated = false
    }

    fun buyFood() {
        GameMessage("You can spend $MEAL_PRICE coins to eat a meal.\nA meal grants the following:").printFormatted()
        GameMessage("+${MEAL_HEALTH_GAIN}HP, +${MEAL_ENERGY_GAIN}EN", TextColor.GREEN).printFormatted()
        GameMessage("Would you like to eat? [y/n]").printFormatted()

        val prompt = readlnOrNull()?.firstOrNull()
        if (prompt != 'y') return

        if (player.cash < MEAL_PRICE) {
            GameMessage("You dont have enough money to buy food.", TextColor.YELLOW).printFormatted()
            return
        }

        eatFood()

        GameMessage("You enjoy a meal and regain some strength.").printFormatted()
    }

    private fun eatFood() {
        player.cash -= MEAL_PRICE
        player.heal(MEAL_HEALTH_GAIN)
        player.energize(MEAL_ENERGY_GAIN)
    }

    fun openShop() {
        val nextState = ShopState(sceneRenderer, onFinish, this, player)
        terminate(nextState)
    }

    fun upgradeSkills() {
        val nextState = SkillsState(sceneRenderer, onFinish, this, player)
        terminate(nextState)
    }

    fun manageInventory() {
        printCurrentInventory()

        GameMessage("Select a wearable item (w#) or a usable item (u#). e.g: w3 or u12:").printFormatted()

        val prompt = readlnOrNull()
        val type = prompt?.firstOrNull()
        var index = prompt?.drop(1)?.toIntOrNull()

        if (prompt.isNullOrEmpty()) return

        if (type == null || (type != 'u' && type != 'w') || index == null) {
            GameMessage(
                "Invalid Syntax! Format your selection like so: [w/u][index of item]" +
                        " where w stands for wearable and u stands for usable.", TextColor.YELLOW
            ).printFormatted()
            return
        }

        // Convert human index (1, 2, 3...) to code index (0, 1, 2...)
        index--

        val item = getItemByIndexAndType(index, type)

        if (item == null)
            GameMessage("This item does not exist :(").printFormatted()
        else
            player.useItem(item)
    }


    fun printCurrentInventory() {
        GameMessage("WEARABLES: ").printFormatted()
        player.wearables.forEachIndexed { i, it ->
            GameMessage(
                "${i + 1}. ${it.name}",
                if (it.isWorn) TextColor.CYAN else TextColor.WHITE
            ).printFormatted()
        }
        GameMessage("\nUSABLES: ").printFormatted()
        player.usables.forEachIndexed { i, it -> GameMessage("${i + 1}. ${it.name}").printFormatted() }
    }

    private fun getItemByIndexAndType(index: Int, type: Char): Item? {
        return when (type) {
            'u' -> player.usables.getOrNull(index)
            'w' -> player.wearables.getOrNull(index)
            else -> null
        }
    }

    fun showStats() {
        player.showStats()
        GameMessage(
            "Upgrade Points: ${player.upgradePoints}",
            if (player.upgradePoints != 0) TextColor.CYAN else TextColor.WHITE
        ).printFormatted()
        GameMessage("\nNote that the effects of your wearables are only applied in battle.").printFormatted()
    }

    fun finishResting() {
        val nextState = BattleState(sceneRenderer, onFinish, this, player)
        terminate(nextState)
    }
}
