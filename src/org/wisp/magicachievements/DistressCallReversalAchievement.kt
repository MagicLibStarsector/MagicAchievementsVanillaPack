package org.wisp.magicachievements

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.BattleAPI
import com.fs.starfarer.api.campaign.CampaignEventListener
import com.fs.starfarer.api.campaign.CampaignFleetAPI
import com.fs.starfarer.api.campaign.listeners.FleetEventListener
import com.fs.starfarer.api.impl.campaign.events.nearby.DistressCallPirateAmbushAssignmentAI
import com.fs.starfarer.api.impl.campaign.events.nearby.DistressCallPirateAmbushTrapAssignmentAI
import org.magiclib.achievements.MagicAchievement

class DistressCallReversalAchievement : MagicAchievement(), FleetEventListener {
    override fun onSaveGameLoaded(isComplete: Boolean) {
        super.onSaveGameLoaded(isComplete)
        if (isComplete) return

        if (!Global.getSector().listenerManager.hasListener(this))
            Global.getSector().listenerManager.addListener(this, true)
    }

    override fun reportBattleOccurred(fleet: CampaignFleetAPI?, primaryWinner: CampaignFleetAPI?, battle: BattleAPI?) {
        if (battle?.isPlayerInvolved != true || primaryWinner != Global.getSector().playerFleet)
            return

        if (battle.nonPlayerSideSnapshot?.any {
                it.hasScriptOfClass(DistressCallPirateAmbushTrapAssignmentAI::class.java)
                        || it.hasScriptOfClass(DistressCallPirateAmbushAssignmentAI::class.java)
            } == true
        ) {
            completeAchievement()
            saveChanges()
        }
    }

    override fun reportFleetDespawnedToListener(
        fleet: CampaignFleetAPI?,
        reason: CampaignEventListener.FleetDespawnReason?,
        param: Any?
    ) = Unit
}