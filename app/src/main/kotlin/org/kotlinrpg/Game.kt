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
        const val INITIAL_MONEY = 500f
        const val INITIAL_UPGRADE_POINTS = 5
    }

    var player: Player = Player(
        "Player",
        INITIAL_HEALTH,
        INITIAL_ENERGY,
        INITIAL_DAMAGE,
        INITIAL_CLUMSINESS,
        INITIAL_MONEY,
        INITIAL_UPGRADE_POINTS
    )

    var gameState: State
    var isRunning = true

    private var _sceneRenderer = SceneRenderer()

    init {
        gameState = RestState(
            _sceneRenderer,
            ::handleStateChange,
            player
        )
    }

    fun startGameLoop() {
        initializeState(gameState)

        while (isRunning) {
            val rawLine = readlnOrNull()

            if (rawLine == null) {
                println("Error: Standard input stream closed. Exiting game loop.")
                isRunning = false
                break
            }

            val input: Char? = rawLine.trim().firstOrNull()?.lowercaseChar()
            //println("Detected input $input")

            if (input != null) {
                gameState.handleInput(input)
            }
        }
    }

    fun initializeState(state: State) {
        gameState = state
        state.enter()
    }

    fun handleStateChange(newState: State?) {
        if (newState == null) {
            isRunning = false
            return
        }

        initializeState(newState)
    }
}
