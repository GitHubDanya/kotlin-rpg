package org.kotlinrpg

import org.kotlinrpg.data.Constants
import org.kotlinrpg.game.Player
import org.kotlinrpg.rendering.SceneRenderer

fun main() {
    val game = Game(SceneRenderer())

    var player = Player(
        "Player",
        Constants.INITIAL_HEALTH,
        Constants.INITIAL_ENERGY,
        Constants.INITIAL_DAMAGE,
        Constants.INITIAL_CLUMSINESS,
        Constants.INITIAL_MONEY,
        Constants.INITIAL_UPGRADE_POINTS
    )

    game.startGameLoop(player)
}
