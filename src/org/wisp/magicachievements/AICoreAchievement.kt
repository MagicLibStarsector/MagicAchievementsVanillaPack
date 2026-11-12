package org.wisp.magicachievements

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.impl.campaign.econ.RogueAICore
import com.fs.starfarer.api.util.Misc
import org.magiclib.achievements.MagicAchievement

/**
 * Had an AI core go rogue.
 *
 * The game adds the `rogue_ai_core` condition to a colony when the player orders a refusing AI core admin removed.
 */
class AICoreAchievement : MagicAchievement() {
    override fun advanceAfterInterval(amount: Float) {
        super.advanceAfterInterval(amount)

        if (Global.getSector() == null) {
            return
        }

        if (Misc.getPlayerMarkets(false).any { RogueAICore.get(it) != null }) {
            completeAchievement()
        }
    }
}
