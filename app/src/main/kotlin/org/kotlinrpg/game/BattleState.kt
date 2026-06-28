package org.kotlinrpg.game

import org.kotlinrpg.data.*
import org.kotlinrpg.rendering.SceneRenderer

const val battleAscii = """
      /| ________________
O|===|* >________________>
      \|

FIGHTING...
"""

val battleMessage = GameMessage(battleAscii, TextColor.RED)

class BattleState(
    onFinish: (nextState: State?) -> Unit,
    player: Player,
    sceneRenderer: SceneRenderer
) : State(onFinish, sceneRenderer) {
    override val actions = mutableListOf<GameAction>(
        GameAction('a', "Attack", true, ::attack)
    )

    override val sceneMessage = battleMessage

    fun attack() {}
}

