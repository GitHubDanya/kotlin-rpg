package org.kotlinrpg.data

object Constants {
    // PLAYER CONSTANTS
    const val INITIAL_HEALTH = 100
    const val INITIAL_ENERGY = 100
    const val INITIAL_DAMAGE = 20
    const val INITIAL_CLUMSINESS = 0.2f
    const val INITIAL_MONEY = 30f
    const val INITIAL_UPGRADE_POINTS = 5

    // COMBAT
    const val ENERGY_ATTACK_COST = 20
    const val RESTING_ENERGY_GAIN = 20
    const val TURN_ENERGY_GAIN = 5
    const val ENEMIES_COUNT = 2

    // REWARDS
    val UPGRADE_POINT_REWARD_RANGE = 1..4
    const val COIN_REWARD_PER_ENEMY = 30
    const val COIN_REWARD_LEVEL_MULTIPLIER = 0.95f

    // ASCII ART
    const val GAME_OVER_ASCII =
        """
           (`-')  _ <-. (`-')   (`-')  _                     (`-') (`-')  _   (`-')           
    .->    (OO ).-/    \(OO )_  ( OO).-/         .->        _(OO ) ( OO).-/<-.(OO )           
 ,---(`-') / ,---.  ,--./  ,-.)(,------.    (`-')----. ,--.(_/,-.\(,------.,------,)          
'  .-(OO ) | \ /`.\ |   `.'   | |  .---'    ( OO).-.  '\   \ / (_/ |  .---'|   /`. '          
|  | .-, \ '-'|_.' ||  |'.'|  |(|  '--.     ( _) | |  | \   /   / (|  '--. |  |_.' |          
|  | '.(_/(|  .-.  ||  |   |  | |  .--'      \|  |)|  |_ \     /_) |  .--' |  .   .'          
|  '-'  |  |  | |  ||  |   |  | |  `---.      '  '-'  '\-'\   /    |  `---.|  |\  \ ,-.,-.,-. 
 `-----'   `--' `--'`--'   `--' `------'       `-----'     `-'     `------'`--' '--''-''-''-' 
                """

    const val BATTLE_ASCII = """
      /| ________________
O|===|* >________________>
      \|

FIGHTING...
"""

    const val WIN_ASCII = """
 __    __  _____   __  __      __      __  ______   __  __  __     
/\ \  /\ \/\  __`\/\ \/\ \    /\ \  __/\ \/\__  _\ /\ \/\ \/\ \    
\ `\`\\/'/\ \ \/\ \ \ \ \ \   \ \ \/\ \ \ \/_/\ \/ \ \ `\\ \ \ \   
 `\ `\ /'  \ \ \ \ \ \ \ \ \   \ \ \ \ \ \ \ \ \ \  \ \ , ` \ \ \  
   `\ \ \   \ \ \_\ \ \ \_\ \   \ \ \_/ \_\ \ \_\ \__\ \ \`\ \ \_\ 
     \ \_\   \ \_____\ \_____\   \ `\___x___/ /\_____\\ \_\ \_\/\_\
      \/_/    \/_____/\/_____/    '\/__//__/  \/_____/ \/_/\/_/\/_/


"""

    const val REST_ASCII = """
           x
.-. _______|
|=|/     /  \
| |_____|_""_|
|_|_[X]_|____|

RESTING...
"""

    const val SHOP_ASCII = """
   _....._
  ';-.--';'
    }===={
  .'  _|_ '.
 /:: (_|_`  \
|::  ,_|_)   |
\::.   |     /
 '::_     _-'
     `````

WELCOME TO THE SHOP!
"""

    const val SKILLS_ASCII =
        """
|     .-.
|    /   \         .-.
|   /     \       /   \       .-.     .-.     _   _
+--/-------\-----/-----\-----/---\---/---\---/-\-/-\/\/---
| /         \   /       \   /     '-'     '-'
|/           '-'         '-'

UPGRADE YOUR SKILLS...
(1 UPGRADE POINT INCREASES A SKILL'S CEILING BY 5)

"""
}
