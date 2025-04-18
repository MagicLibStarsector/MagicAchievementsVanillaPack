package org.wisp.magicachievements

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.BaseCampaignEventListener
import com.fs.starfarer.api.campaign.BattleAPI
import com.fs.starfarer.api.campaign.CampaignFleetAPI
import com.fs.starfarer.api.util.Misc
import org.magiclib.achievements.MagicAchievement
import kotlin.math.roundToInt

abstract class SlayerMagicAchievement : MagicAchievement() {
    var shipsKilled: Int
        get() = achievementMemory["shipsKilled"] as? Int ?: 0
        set(value) {
            achievementMemory["shipsKilled"] = value
        }

    var listener: Listener? = null

    inner class Listener : BaseCampaignEventListener(false) {
        override fun reportBattleFinished(primaryWinner: CampaignFleetAPI?, battle: BattleAPI?) {
            if (!battle!!.isPlayerInvolved) return

            val recentShipsKilled = battle.nonPlayerSide.sumOf { fleet -> Misc.getSnapshotMembersLost(fleet).count() }
            val involvedFraction = battle.playerInvolvementFraction

            shipsKilled += (recentShipsKilled * involvedFraction).roundToInt()
            if (shipsKilled > maxProgress) {
                this@SlayerMagicAchievement.completeAchievement()
                this@SlayerMagicAchievement.onDestroyed()
            }
        }
    }

    override fun onSaveGameLoaded(isComplete: Boolean) {
        super.onSaveGameLoaded(isComplete)
        if (isComplete) return
        listener = Listener()
        Global.getSector().addTransientListener(listener)
    }

    override fun onDestroyed() {
        Global.getSector().removeListener(listener)
    }

    override fun getProgress(): Float = shipsKilled.toFloat()
    abstract override fun getMaxProgress(): Float
}

class Slayer100MagicAchievement : SlayerMagicAchievement() {
    override fun getMaxProgress(): Float = 100f
}

class Slayer1000MagicAchievement : SlayerMagicAchievement() {
    override fun getMaxProgress(): Float = 1000f
}

class Slayer2000MagicAchievement : SlayerMagicAchievement() {
    override fun getMaxProgress(): Float = 2000f
}

class Slayer5000MagicAchievement : SlayerMagicAchievement() {
    override fun getMaxProgress(): Float = 5000f
}

class Slayer10000MagicAchievement : SlayerMagicAchievement() {
    override fun getMaxProgress(): Float = 10000f
}