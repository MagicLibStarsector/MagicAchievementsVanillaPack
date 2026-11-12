package org.wisp.magicachievements

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.CampaignFleetAPI
import org.magiclib.achievements.MagicAchievement

/**
 * Got your first distress call, which is always friendly.
 */
class DistressCallFriendly : MagicAchievement() {
    companion object {
        /** Set by `DistressCallAbility.spawnFleet` on fleets answering with help. Pirate ambushes don't get it. */
        private const val RESPONDER_MEM_KEY = "\$distressResponse"

        /** The "We'll be grateful for anything you can spare" option from rules.csv. It's only offered by a friendly responder. */
        private const val HELP_OPTION_ID = "dcallHelp"
    }

    /**
     * Checked every frame, not on the 1-2 second interval, because the player can pick the help option faster than that.
     * Almost every frame stops at the first line, because no dialog is open.
     */
    override fun advanceInternal(amount: Float) {
        super.advanceInternal(amount)

        val dialog = Global.getSector().campaignUI?.currentInteractionDialog ?: return
        val fleet = dialog.interactionTarget as? CampaignFleetAPI ?: return
        if (fleet.memoryWithoutUpdate?.getBoolean(RESPONDER_MEM_KEY) != true) return
        if (dialog.optionPanel?.hasOption(HELP_OPTION_ID) != true) return

        // Pass the text panel so the unlock also shows in the open dialog.
        completeAchievement(Global.getSector().playerPerson, dialog.textPanel)
    }
}
