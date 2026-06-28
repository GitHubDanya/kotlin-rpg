package org.kotlinrpg.game

import org.kotlinrpg.data.*
import org.kotlinrpg.rendering.SceneRenderer

abstract class State(
    // The scene renderer is used for displaying the banner
    // and also displaying possible actions. It is injected
    // to all scenes.
    val sceneRenderer: SceneRenderer,

    // This function is a callback function for when the
    // scene terminates.
    // Usually, if nextstate is null, the game terminates.
    val onFinish: (nextState: State?) -> Unit,

    // The previous state can be passed here to know where
    // to route after terminating.

    val returnState: State? = null
) {
    // Each State has a list of actions that a player can do.
    abstract val actions: MutableList<GameAction>

    // This variable holds the banner to be rendered at the start
    // of the scene.
    abstract val sceneMessage: GameMessage

    init {
        enter()
    }

    // This function is ran ater the scene initializes.
    open fun enter() {
        sceneRenderer.clearConsole()
        sceneRenderer.renderScene(this)
        sceneRenderer.showActions(this.actions)
    }

    // This function is called whenever the scene should terminate.
    fun terminate(nextState: State?) = onFinish(nextState)

    // This function handles input at any stage of the game.
    open fun handleInput(input: Char) {
        var selectedAction = actions.find { it.key == input }
        selectedAction?.execute?.invoke()
        sceneRenderer.showActions(actions)
    }
}
