package org.wisp.magicachievements

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.BattleAPI
import com.fs.starfarer.api.campaign.CampaignEventListener
import com.fs.starfarer.api.campaign.CampaignFleetAPI
import com.fs.starfarer.api.campaign.listeners.FleetEventListener
import com.fs.starfarer.api.impl.campaign.events.nearby.DistressCallPirateAmbushAssignmentAI
import com.fs.starfarer.api.impl.campaign.events.nearby.DistressCallPirateAmbushTrapAssignmentAI

/**
 * Turned around a bogus distress call: beat the pirate ambush that sent it.
 */
class DistressCallReversalAchievement : ListenerAchievement(paintjobRewards = listOf("fury_racing")), FleetEventListener {
    override fun reportBattleOccurred(fleet: CampaignFleetAPI?, primaryWinner: CampaignFleetAPI?, battle: BattleAPI?) {
        if (battle?.isPlayerInvolved != true || primaryWinner != Global.getSector().playerFleet) return

        val beatAmbush = battle.nonPlayerSideSnapshot.orEmpty().any {
            it.hasScriptOfClass(DistressCallPirateAmbushTrapAssignmentAI::class.java)
                    || it.hasScriptOfClass(DistressCallPirateAmbushAssignmentAI::class.java)
        }
        if (beatAmbush) completeAchievement()
    }

    override fun reportFleetDespawnedToListener(fleet: CampaignFleetAPI?, reason: CampaignEventListener.FleetDespawnReason?, param: Any?) {}
}
