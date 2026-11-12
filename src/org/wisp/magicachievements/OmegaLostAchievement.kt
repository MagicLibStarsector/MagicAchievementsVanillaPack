package org.wisp.magicachievements

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.BaseCampaignEventListener
import com.fs.starfarer.api.campaign.EngagementResultForFleetAPI
import com.fs.starfarer.api.combat.EngagementResultAPI
import com.fs.starfarer.api.impl.campaign.ids.Entities
import com.fs.starfarer.api.impl.campaign.ids.Factions

/**
 * Lost to <ultra redacted>.
 */
class OmegaLostAchievement : CampaignEventAchievement() {
    override fun createListener() = object : BaseCampaignEventListener(false) {
        override fun reportPlayerEngagement(result: EngagementResultAPI?) {
            if (result != null && !result.didPlayerWin() && result.winnerResult?.isOmegaAtCoronalTap() == true) completeAchievement()
        }
    }
}

/**
 * True when this side deployed at least two Omega cruisers (or capitals, in case a mod swaps them in) and the player
 * fleet is in a system with a coronal tap.
 */
internal fun EngagementResultForFleetAPI.isOmegaAtCoronalTap(): Boolean {
    val atCoronalTap = Global.getSector().playerFleet?.containingLocation?.allEntities.orEmpty()
        .any { it.hasTag(Entities.CORONAL_TAP) }
    // The deployed list is null when the battle was autoresolved.
    val omegaShips = allEverDeployedCopy.orEmpty().mapNotNull { it.member }
        .count { (it.isCruiser || it.isCapital) && it.fleetData?.fleet?.faction?.id == Factions.OMEGA }
    return atCoronalTap && omegaShips >= 2
}
