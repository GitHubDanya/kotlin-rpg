package org.kotlinrpg.rendering

import org.kotlinrpg.data.*
import org.kotlinrpg.game.*

class SceneRenderer {
    fun renderScene(state: State) {
        clearConsole()
        state.sceneMessage.printFormatted()
    }

    fun showActions(actions: MutableList<GameAction>) {
        var allowedActions = actions.filter { it.allowed }

        println("Available actions: ")

        if (allowedActions.isEmpty())
            print("None :/")
        else
            allowedActions.forEach { action ->
                print("[${action.key}] ${action.name}  ")
            }

        println()
    }

    fun clearConsole() {
        print("\u001b[H\u001b[2J")
        System.out.flush()
    }
}
