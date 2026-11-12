package org.wisp.magicachievements

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.PluginPick
import com.fs.starfarer.api.campaign.BaseCampaignPlugin
import com.fs.starfarer.api.campaign.InteractionDialogPlugin
import com.fs.starfarer.api.util.Misc

/**
 * Died in a black hole.
 */
class BlackHoleAchievement : VanillaPackAchievement() {
    // BaseCampaignPlugin is transient, so this never goes into the save.
    private val plugin = object : BaseCampaignPlugin() {
        override fun getId() = "magiclib_black_hole_achievement"

        override fun pickRespawnPlugin(): PluginPick<InteractionDialogPlugin>? {
            val fleet = Global.getSector().playerFleet
            val insideBlackHole = fleet?.containingLocation?.planets.orEmpty()
                .any { it.isStar && it.spec.isBlackHole && Misc.getDistance(it, fleet) < it.radius }
            if (insideBlackHole) completeAchievement()
            return null
        }
    }

    override fun onSaveGameLoaded(isComplete: Boolean) {
        super.onSaveGameLoaded(isComplete)
        if (!isComplete) Global.getSector().registerPlugin(plugin)
    }

    // Not unregistered on completion: that happens inside pickRespawnPlugin, while the game loops over its plugin list,
    // and changing the list there crashes the game (ConcurrentModificationException). Once complete it does nothing.
    override fun onDestroyed() {
        super.onDestroyed()
        Global.getSector()?.unregisterPlugin(plugin.id)
    }
}
