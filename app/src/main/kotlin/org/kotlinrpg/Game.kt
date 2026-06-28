package org.kotlinrpg

import org.kotlinrpg.data.*
import org.kotlinrpg.game.*
import org.kotlinrpg.rendering.SceneRenderer

class Game {
    companion object {
        const val INITIAL_HEALTH = 100
        const val INITIAL_ENERGY = 100
        const val INITIAL_DAMAGE = 20
        const val INITIAL_CLUMSINESS = 0.2f
        const val INITIAL_MONEY = 5f
        const val INITIAL_UPGRADE_POINTS = 5
    }

    var player: Player = Player(
        INITIAL_HEALTH,
        INITIAL_ENERGY,
        INITIAL_DAMAGE,
        INITIAL_CLUMSINESS,
        INITIAL_MONEY,
        INITIAL_UPGRADE_POINTS
    )

    var gameState: State? = null
    var isRunning = true

    private var _sceneRenderer = SceneRenderer()

    fun startGameLoop() {
        gameState = RestState(
            ::handleStateChange
        )

        while (isRunning) {
            val input: Char? = readlnOrNull()?.firstOrNull()
            if (input != null) gameState?.handleInput(input)
        }
    }

    fun initializeState(state: State) {
        gameState = state
    }

    fun handleStateChange(newState: State?) {
        if (newState == null) {
            isRunning = false
            return
        }

        initializeState(newState)
    }
}
