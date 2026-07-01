package org.kotlinrpg.game

import org.kotlinrpg.Game
import org.kotlinrpg.data.*
import org.kotlinrpg.rendering.SceneRenderer

const val skillsAscii =
    """
|     .-.
|    /   \         .-.
|   /     \       /   \       .-.     .-.     _   _
+--/-------\-----/-----\-----/---\---/---\---/-\-/-\/\/---
| /         \   /       \   /     '-'     '-'
|/           '-'         '-'

UPGRADE YOUR SKILLS...
(1 UPGRADE POINT INCREASES A SKILL'S CEILING BY 5)

"""

val skillsMessage = GameMessage(skillsAscii, TextColor.CYAN)

class SkillsState(
    sceneRenderer: SceneRenderer,
    onFinish: (nextState: State?) -> Unit,
    returnState: State,
    val player: Player
) : State(sceneRenderer, onFinish, returnState) {

    override val actions = mutableListOf<GameAction>(
        GameAction('h', "Invest in Health", true, ::upgradeHealth),
        GameAction('e', "Invest in Energy", true, ::upgradeEnergy),
        GameAction('f', "Finish upgrading", true, ::finishUpgrading)
    )

    override val sceneMessage = skillsMessage

    override fun enter() {
        sceneRenderer.clearConsole()
        sceneRenderer.renderScene(this)

        if (player.upgradePoints != 0) GameMessage(
            "You have ${player.upgradePoints} unspent upgarde points!",
            TextColor.CYAN
        ).printFormatted()

        sceneRenderer.showActions(this.actions)
        sceneTerminated = false
    }

    private fun tryUpgrade(upgradeAction: () -> Unit) {
        if (player.upgradePoints == 0) {
            GameMessage("You don't have enough upgrade points!", TextColor.YELLOW).printFormatted()
            return
        }

        upgradeAction()
        player.upgradePoints--

        GameMessage(
            "Upgraded successfully. ${player.upgradePoints} upgrade points left.",
            TextColor.GREEN
        ).printFormatted()
    }

    fun upgradeHealth() = tryUpgrade { player.maxHealth += 5 }
    fun upgradeEnergy() = tryUpgrade { player.maxEnergy += 5 }

    fun finishUpgrading() = terminate(returnState)
}
