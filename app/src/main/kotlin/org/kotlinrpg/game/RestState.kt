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
    player: Player,
) : State(sceneRenderer, onFinish) {
    override val actions = mutableListOf<GameAction>(
        GameAction('s', "Open Shop", true, ::openShop),
        GameAction('u', "Upgrade Skills", true, ::upgradeSkills),
        GameAction('n', "Finish Resting", true, ::finishResting),
    )

    override val sceneMessage = battleMessage

    override fun handleInput(input: Char) {
        // TODO: maybe do something here idk
        super.handleInput(input)

    }

    fun openShop() {}

    fun upgradeSkills() {}

    fun finishResting() {}
}
