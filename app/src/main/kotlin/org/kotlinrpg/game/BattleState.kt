package org.kotlinrpg.game

import org.kotlinrpg.data.*
import org.kotlinrpg.data.characters.*
import org.kotlinrpg.rendering.SceneRenderer
import kotlin.math.roundToInt

const val battleAscii = """
      /| ________________
O|===|* >________________>
      \|

FIGHTING...
"""

const val winAscii = """
 __    __  _____   __  __      __      __  ______   __  __  __     
/\ \  /\ \/\  __`\/\ \/\ \    /\ \  __/\ \/\__  _\ /\ \/\ \/\ \    
\ `\`\\/'/\ \ \/\ \ \ \ \ \   \ \ \/\ \ \ \/_/\ \/ \ \ `\\ \ \ \   
 `\ `\ /'  \ \ \ \ \ \ \ \ \   \ \ \ \ \ \ \ \ \ \  \ \ , ` \ \ \  
   `\ \ \   \ \ \_\ \ \ \_\ \   \ \ \_/ \_\ \ \_\ \__\ \ \`\ \ \_\ 
     \ \_\   \ \_____\ \_____\   \ `\___x___/ /\_____\\ \_\ \_\/\_\
      \/_/    \/_____/\/_____/    '\/__//__/  \/_____/ \/_/\/_/\/_/


"""

val battleMessage = GameMessage(battleAscii, TextColor.RED)
val winMessage = GameMessage(winAscii, TextColor.GREEN)

// COMBAT
const val ENERGY_ATTACK_COST = 20
const val RESTING_ENERGY_GAIN = 20
const val TURN_ENERGY_GAIN = 5
const val ENEMIES_COUNT = 2

// REWARDS
val UPGRADE_POINT_REWARD_RANGE = 1..4
const val COIN_REWARD_PER_ENEMY = 30
const val COIN_REWARD_LEVEL_MULTIPLIER = 0.95f

class BattleState(
    sceneRenderer: SceneRenderer,
    onFinish: (nextState: State?) -> Unit,
    returnState: State,
    val player: Player,
) : State(sceneRenderer, onFinish, returnState) {

    override val actions = mutableListOf<GameAction>(
        GameAction('f', "Check Field", true, ::displayEnemies),
        GameAction('s', "Stats", true, ::checkStats),
        GameAction('u', "Use an item", true, ::selectUseItem),
        GameAction('a', "Attack", true, ::attack),
        GameAction('r', "Rest", true, ::rest)
    )

    override val sceneMessage = battleMessage

    val enemies = generateEnemies(ENEMIES_COUNT)
    val originalPlayer: Player = Player(player)

    override fun enter() {
        player.wearables.forEach { if (it.isWorn) it.wear }

        sceneRenderer.clearConsole()
        sceneRenderer.renderScene(this)
        displayEnemies()
        sceneRenderer.showActions(this.actions)
        sceneTerminated = false
    }

    fun generateEnemies(amount: Int): MutableList<GameEnemy> {
        val res = mutableListOf<GameEnemy>()
        repeat(amount) { res.addLast(generateEnemy()) }
        return res
    }

    fun generateEnemy(): GameEnemy {
        val randomNumber = (1..ENEMIES_COUNT).random()
        return when (randomNumber) {
            1 -> Troll(player = player)
            2 -> Goblin(player = player)
            else -> Troll(player = player)
        }
    }

    fun checkStats() = player.showStats()

    fun displayEnemies() {
        enemies.forEachIndexed { i, it ->
            GameMessage("     ${i}. ${it.name}     ").printFormatted()
            GameMessage("HP: ${it.health}/${it.maxHealth}").printFormatted()
            GameMessage("EN: ${it.energy}/${it.maxEnergy}\n").printFormatted()
        }
    }

    fun selectUseItem() {
        player.usables.forEachIndexed { i, it -> GameMessage("${i + 1}. ${it.name} - ${it.description}").printFormatted() }
        GameMessage("\nSelect an item:").printFormatted()

        val input = readlnOrNull()?.toIntOrNull() ?: return

        val selected = player.usables.getOrNull(input - 1)

        if (selected == null) {
            GameMessage("This item does not exist", TextColor.YELLOW).printFormatted()
            return
        }

        GameMessage("You use a ${selected.name}...").printFormatted()
        player.useItem(selected)
    }

    fun attack() {
        GameMessage("Select an enemy:\n").printFormatted()

        enemies.forEachIndexed { i, it ->
            GameMessage("${i + 1}. ${it.name}  ${it.health}/${it.maxHealth}HP  ${it.energy}/${it.maxEnergy}EN").printFormatted()
        }

        val input = readlnOrNull()?.toIntOrNull() ?: return

        val selected = enemies.getOrNull(input - 1)

        if (selected == null) {
            GameMessage("This enemy does not exist", TextColor.YELLOW).printFormatted()
            return
        }

        player.energy -= ENERGY_ATTACK_COST
        val damage = player.generateDamageAmount()
        selected.damage(damage)

        GameMessage("${selected.name} HIT by $damage points.", TextColor.GREEN).printFormatted()

        if (selected.health <= 0) {
            GameMessage("${selected.name} ELIMINATED", TextColor.CYAN).printFormatted()
            enemies.remove(selected)
        }

        println()
        advanceTurn()
    }

    fun rest() {
        GameMessage(
            "You decide to rest. You replenish $RESTING_ENERGY_GAIN energy points.\n",
            TextColor.CYAN
        ).printFormatted()

        player.energize(RESTING_ENERGY_GAIN)

        advanceTurn()
    }

    fun advanceTurn() {
        letEnemiesAttack()
        energizeEnemies()
        player.energize(TURN_ENERGY_GAIN)

        if (player.health <= 0) terminate(null)

        if (enemies.isEmpty()) finishFight()
    }

    fun finishFight() {
        winMessage.printFormatted()
        resetPlayerEffects()
        generateAndRewardRewards()
        player.level++
        readln()
        terminate(returnState)
    }

    private fun resetPlayerEffects() {
        player.maxHealth = originalPlayer.maxHealth
        player.maxEnergy = originalPlayer.maxEnergy
        player.damage = originalPlayer.damage
        player.clumsiness = originalPlayer.clumsiness
    }

    private fun generateAndRewardRewards() {
        val rewardedUpPoints = UPGRADE_POINT_REWARD_RANGE.random()
        var rewardedCash = (COIN_REWARD_PER_ENEMY * ENEMIES_COUNT) * (COIN_REWARD_LEVEL_MULTIPLIER * player.level)
        rewardedCash = (rewardedCash * 100).roundToInt() / 100.0f

        player.cash += rewardedCash
        player.upgradePoints += rewardedUpPoints

        GameMessage("You won ${rewardedCash}$!", TextColor.YELLOW).printFormatted()
        GameMessage("You won $rewardedUpPoints upgrade points!", TextColor.CYAN).printFormatted()

        readln()

        terminate(returnState)
    }

    private fun letEnemiesAttack() {
        enemies.forEach {
            val attack = it.getAvailAttackAtRandom()
            if (attack == null)
                GameMessage("${it.name} is out of energy...\n").printFormatted()
            else {
                GameMessage("${it.name} USES ${attack.name}!").printFormatted()
                attack.message?.printFormatted()
                attack.attack(player).printFormatted()
                it.energy -= attack.energy
                println()
            }
        }
    }

    private fun energizeEnemies() {
        enemies.forEach {
            it.energize(TURN_ENERGY_GAIN)
        }
    }
}

