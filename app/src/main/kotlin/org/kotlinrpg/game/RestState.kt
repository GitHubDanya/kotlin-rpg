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

class RestState(
    sceneRenderer: SceneRenderer,
    onFinish: (nextState: State?) -> Unit,
    val player: Player,
) : State(sceneRenderer, onFinish) {
    override val actions = mutableListOf<GameAction>(
        GameAction('a', "Stats", true, ::showStats),
        GameAction('s', "Open Shop", true, ::openShop),
        GameAction('u', "Upgrade Skills", true, ::upgradeSkills),
        GameAction('i', "Inventory", true, ::manageInventory),
        GameAction('n', "Finish Resting", true, ::finishResting),
    )

    override val sceneMessage = restMessage

    override fun enter()
    {
        sceneRenderer.clearConsole()
        sceneRenderer.renderScene(this)

        if (player.upgradePoints != 0) GameMessage("You have unspent upgarde points! Spend them by pressing [u]\n", TextColor.CYAN).printFormatted()

        sceneRenderer.showActions(this.actions)
        sceneTerminated = false
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
        GameMessage("WEARABLES: ").printFormatted()
        player.wearables.forEachIndexed { i, it ->
            GameMessage(
                "${i + 1}. ${it.name}",
                if (it.isWorn) TextColor.CYAN else TextColor.WHITE
            ).printFormatted()
        }
        GameMessage("\nUSABLES: ").printFormatted()
        player.usables.forEachIndexed { i, it -> GameMessage("${i + 1}. ${it.name}").printFormatted() }
        GameMessage("Select a wearable item (w#) or a usable item (u#). e.g: w3 or u12:").printFormatted()

        val prompt = readlnOrNull()
        val type = prompt?.firstOrNull()
        var index = prompt?.drop(1)?.toIntOrNull()

        if (prompt.isNullOrEmpty()) return

        if (type == null || (type != 'u' && type != 'w') || index == null) {
            GameMessage(
                "Invalid Syntax! Format your selection like so: [w/u][index of item]" +
                        " where w stands for wearable and u stnads for usable.", TextColor.YELLOW
            ).printFormatted()
            return
        }

        index--

        val item: Item? = when (type) {
            'u' -> player.usables.getOrNull(index)
            'w' -> player.wearables.getOrNull(index)
            else -> null
        }

        if (item == null) {
            GameMessage("This item does not exist :(").printFormatted()
            return
        }

        when (item) {
            is UseableItem -> {
                item.apply(player)
                GameMessage("Used a ${item.name}", TextColor.CYAN).printFormatted()
            }

            is WearableItem -> {
                item.isWorn = !item.isWorn
                if (item.isWorn) GameMessage("Equipped ${item.name}", TextColor.GREEN).printFormatted()
                else GameMessage("Unequipped ${item.name}", TextColor.RED).printFormatted()
            }
        }
    }

    fun showStats() {
        GameMessage("YOUR STATS:").printFormatted()
        GameMessage("LEVEL ${player.level}\n").printFormatted()
        val healthColor = getRelationalColor(player.health, player.maxHealth)
        val energyColor = getRelationalColor(player.energy, player.maxEnergy)
        GameMessage("Health: ${player.health}/${player.maxHealth}", healthColor).printFormatted()
        GameMessage("Energy: ${player.energy}/${player.maxEnergy}\n", energyColor).printFormatted()

        GameMessage("Damage: ${player.damage}").printFormatted()
        GameMessage("Clumsiness: ${player.clumsiness}\n").printFormatted()

        GameMessage("Cash: ${player.cash}").printFormatted()
        GameMessage("Upgrade Points: ${player.upgradePoints}",
        if (player.upgradePoints != 0) TextColor.CYAN else TextColor.WHITE).printFormatted()
    }

    private fun getRelationalColor(x: Int, max: Int): TextColor {
        if (max == 0) return TextColor.WHITE 

        val ratio = x.toFloat() / max

        // Clean threshold checks from highest to lowest
        return when {
            ratio >= 1f   -> TextColor.CYAN
            ratio >= 0.5f -> TextColor.WHITE
            ratio >= 0.3f -> TextColor.YELLOW
            ratio >= 0f   -> TextColor.RED
            else          -> TextColor.WHITE // Handles negative values
        }
}

    fun finishResting() {
    }
}
