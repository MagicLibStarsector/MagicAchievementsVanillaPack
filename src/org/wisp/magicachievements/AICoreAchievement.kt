package org.wisp.magicachievements

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.rules.MemKeys
import com.fs.starfarer.api.impl.campaign.econ.RogueAICore
import com.fs.starfarer.api.util.Misc
import org.magiclib.achievements.MagicAchievement

/**
 * Had an AI core go rogue.
 *
 *
 * Mmm, delicious spaghetti code.
 */
class AICoreAchievement : MagicAchievement() {
    override fun advanceAfterInterval(amount: Float) {
        super.advanceAfterInterval(amount)

        if (Global.getSector() == null) {
            return
        }

        val dialog = Global.getSector().campaignUI.currentInteractionDialog ?: return

        val localMem = dialog.plugin?.memoryMap.orEmpty()?.get(MemKeys.LOCAL) ?: return

        val optionSelected = localMem["\$option"] ?: return

        if (optionSelected.toString().startsWith("RACA_")) {
            completeAchievement()
        }

        for (playerMarket in Misc.getPlayerMarkets(false)) {
            if (RogueAICore.get(playerMarket) != null) {
                completeAchievement()
            }
        }
    }
}
