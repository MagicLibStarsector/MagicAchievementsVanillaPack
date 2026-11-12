package org.wisp.magicachievements

import com.fs.starfarer.api.campaign.BaseCampaignEventListener
import com.fs.starfarer.api.combat.EngagementResultAPI

/**
 * Defeated <ultra redacted> using a maximum of 80 deployment points.
 */
class OmegaLowDpAchievement : CampaignEventAchievement() {
    override fun createListener() = object : BaseCampaignEventListener(false) {
        override fun reportPlayerEngagement(result: EngagementResultAPI?) {
            if (result == null || !result.didPlayerWin() || result.loserResult?.isOmegaAtCoronalTap() != true) return
            // Null when the battle was autoresolved. Without it the deployment points are unknown, so it doesn't count.
            val playerShips = result.winnerResult?.allEverDeployedCopy ?: return

            val playerDeploymentPoints = playerShips.sumOf { (it.member?.deploymentPointsCost ?: 0f).toDouble() }
            if (playerDeploymentPoints <= 80) completeAchievement()
        }
    }
}
