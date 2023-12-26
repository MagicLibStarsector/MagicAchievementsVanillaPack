package org.wisp.magicachievements

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.BaseCampaignEventListener
import com.fs.starfarer.api.combat.EngagementResultAPI
import com.fs.starfarer.api.impl.campaign.ids.Entities
import com.fs.starfarer.api.impl.campaign.ids.Factions
import org.magiclib.achievements.MagicAchievement

/**
 * Lost to <ultra redacted>.
 */
class OmegaLostAchievement : MagicAchievement() {

    inner class OmegaLostAchievementListener : BaseCampaignEventListener(false) {
        override fun reportPlayerEngagement(result: EngagementResultAPI?) {
            super.reportPlayerEngagement(result)
            result ?: return

            try {
                if (!result.didPlayerWin()) {
                    // Check that there's a coronal tap in the system
                    if (Global.getSector().playerFleet.containingLocation?.allEntities?.any { it.hasTag(Entities.CORONAL_TAP) } != true)
                        return

                    // Check that player fought two Omega cruisers (or capitals, in case a mod replaces the cruisers with capitals for some reason)
                    if (result.winnerResult.allEverDeployedCopy.map { it.member }
                            .count { (it.isCruiser || it.isCapital) && it.fleetData.fleet.faction.id == Factions.OMEGA } < 2)
                        return

                    completeAchievement()
                    onDestroyed()
                }
            } catch (e: Exception) {
                logger.warn(e.message, e)
            }
        }
    }

    override fun onSaveGameLoaded(isComplete: Boolean) {
        super.onSaveGameLoaded(isComplete)
        if (isComplete) return
        Global.getSector().removeTransientScriptsOfClass(OmegaLostAchievementListener::class.java)
        Global.getSector()?.addTransientListener(OmegaLostAchievementListener())
    }

    override fun onDestroyed() {
        super.onDestroyed()
        Global.getSector().removeTransientScriptsOfClass(OmegaLostAchievementListener::class.java)
    }
}
