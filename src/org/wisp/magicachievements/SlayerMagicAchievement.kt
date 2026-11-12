package org.wisp.magicachievements

import com.fs.starfarer.api.campaign.BaseCampaignEventListener
import com.fs.starfarer.api.campaign.BattleAPI
import com.fs.starfarer.api.campaign.CampaignFleetAPI
import com.fs.starfarer.api.util.Misc
import kotlin.math.roundToInt

/**
 * Destroyed [killsNeeded] enemy ships. Scaled by the player's contribution to the battle.
 */
abstract class SlayerMagicAchievement(private val killsNeeded: Int) : CampaignEventAchievement() {
    var shipsKilled: Int
        get() = achievementMemory["shipsKilled"] as? Int ?: 0
        set(value) {
            achievementMemory["shipsKilled"] = value
        }

    override fun createListener() = object : BaseCampaignEventListener(false) {
        override fun reportBattleFinished(primaryWinner: CampaignFleetAPI?, battle: BattleAPI?) {
            if (battle?.isPlayerInvolved != true) return

            val shipsLost = battle.nonPlayerSide.sumOf { fleet -> Misc.getSnapshotMembersLost(fleet).count() }
            shipsKilled += (shipsLost * battle.playerInvolvementFraction).roundToInt()
            if (shipsKilled >= killsNeeded) completeAchievement()
        }
    }

    override fun getProgress(): Float = shipsKilled.toFloat()
    override fun getMaxProgress(): Float = killsNeeded.toFloat()
}

class Slayer100MagicAchievement : SlayerMagicAchievement(100)
class Slayer1000MagicAchievement : SlayerMagicAchievement(1000)
class Slayer2000MagicAchievement : SlayerMagicAchievement(2000)
class Slayer5000MagicAchievement : SlayerMagicAchievement(5000)
class Slayer10000MagicAchievement : SlayerMagicAchievement(10000)
