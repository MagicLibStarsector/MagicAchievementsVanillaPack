package org.wisp.magicachievements

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.impl.campaign.ids.Items
import org.magiclib.achievements.MagicTargetListAchievement
import org.magiclib.kotlin.getFactionMarkets

/**
 * Installed every colony item.
 */
class ColonyItemsAchievement : MagicTargetListAchievement() {
    override fun onApplicationLoaded(isComplete: Boolean) {
        super.onApplicationLoaded(isComplete)
        if (isComplete) return

        setTargets(
            listOf(
                Items.BIOFACTORY_EMBRYO,
                Items.CATALYTIC_CORE,
                Items.CORONAL_PORTAL,
                Items.CRYOARITHMETIC_ENGINE,
                Items.DEALMAKER_HOLOSUITE,
                Items.DRONE_REPLICATOR,
                Items.FULLERENE_SPOOL,
                Items.MANTLE_BORE,
                Items.ORBITAL_FUSION_LAMP,
                Items.PLASMA_DYNAMO,
                Items.PRISTINE_NANOFORGE,
                Items.SOIL_NANITES,
                Items.SYNCHROTRON,
                Items.CORRUPTED_NANOFORGE,
            )
                .associateWith { Global.getSettings().getSpecialItemSpec(it)?.name ?: it }
        )
    }

    override fun advanceAfterInterval(amount: Float) {
        super.advanceAfterInterval(amount)

        val installed = Global.getSector().playerFaction?.getFactionMarkets().orEmpty()
            .flatMap { it.industries.orEmpty() }
            .flatMap { it.installableItems.orEmpty() }
            .mapNotNullTo(HashSet()) { it.currentlyInstalledItemData?.id }
        val counted = targets.filterValues { it.isComplete }.keys
        (installed - counted).forEach { setTargetComplete(it) }
    }
}
