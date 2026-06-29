package org.kotlinrpg.game

import org.kotlinrpg.data.*
import org.kotlinrpg.rendering.SceneRenderer

abstract class State(
    // The scene renderer is used for displaying the banner
    // and also displaying possible actions. It is injected
    // to all scenes.
    val sceneRenderer: SceneRenderer,

    // This function is a callback function for when the
    // scene terminates, to let the central Game class know
    // that a scene change occured. If nextState is null,
    // the game ends.
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

    var sceneTerminated = false

    // This function is ran ater the scene initializes.
    open fun enter() {
        sceneRenderer.clearConsole()
        sceneRenderer.renderScene(this)
        sceneRenderer.showActions(this.actions)
        sceneTerminated = false
    }

    // This function is called whenever the scene should terminate.
    fun terminate(nextState: State?) {
        if (nextState == null && returnState != null)
            onFinish(returnState)
        else
            onFinish(nextState)

        sceneTerminated = true
    }

    // This function handles input at any stage of the game.
    open fun handleInput(input: Char) {
        var selectedAction = actions.find { it.key == input }
        println()
        selectedAction?.execute?.invoke()
        println()
        if (!sceneTerminated) sceneRenderer.showActions(actions)
    }
}
