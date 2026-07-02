package org.kotlinrpg

import org.kotlinrpg.data.*
import org.kotlinrpg.game.*
import org.kotlinrpg.rendering.SceneRenderer

class Game(
    private var _sceneRenderer: SceneRenderer
) {
    var gameState: State? = null
    var isRunning = true

    fun initializeStartScene(player: Player) {
        gameState = RestState(
            _sceneRenderer,
            ::handleStateChange,
            player
        )
    }

    fun startGameLoop(player: Player) {
        initializeStartScene(player)

        val currentGameState = gameState
            ?: throw NullPointerException("There was an error initializing the start state of the game.")

        initializeState(currentGameState)

        while (isRunning) {
            val rawLine = readlnOrNull()

            if (rawLine == null) {
                println("Error: Standard input stream closed. Exiting game loop.")
                isRunning = false
                break
            }

            val input: Char? = rawLine.trim().firstOrNull()?.lowercaseChar()

            if (input != null) {
                currentGameState.handleInput(input)
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
            GameMessage(Constants.GAME_OVER_ASCII, TextColor.RED).printFormatted()
            return
        }

        initializeState(newState)
    }
}
