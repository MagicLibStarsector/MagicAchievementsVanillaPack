package org.wisp.magicachievements

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.CargoAPI
import com.fs.starfarer.api.campaign.InteractionDialogAPI
import com.fs.starfarer.api.campaign.econ.Industry
import com.fs.starfarer.api.campaign.econ.MarketAPI
import com.fs.starfarer.api.campaign.listeners.ColonyPlayerHostileActListener
import com.fs.starfarer.api.impl.campaign.rulecmd.salvage.MarketCMD
import org.magiclib.achievements.MagicTargetListAchievement

/**
 * Saturation bombed all vanilla faction homeworlds.
 */
class SatBombEverybodyAchievement : MagicTargetListAchievement(), ColonyPlayerHostileActListener {
    override fun onSaveGameLoaded(isComplete: Boolean) {
        super.onSaveGameLoaded(isComplete)
        if (isComplete) return

        setTargets(
            listOf(
                "chicomoztoc", // Hegemony
                "culann", // Tri-Tachyon
                "gilead", // Luddic Church
                "hesperus", // Luddic Church
                "jangala", // Hegemony
                "kazeron", // Persean League
                "sindria", // Sindrian Diktat
                "volturn", // Sindrian Diktat
            )
                .associateWith { Global.getSector().economy.getMarket(it)?.name ?: it }
        )

        Global.getSector().listenerManager.addListener(this, true)
    }

    /** Also called by [MagicTargetListAchievement] when the last target is done. */
    override fun onDestroyed() {
        super.onDestroyed()
        Global.getSector()?.listenerManager?.removeListener(this)
    }

    override fun reportSaturationBombardmentFinished(dialog: InteractionDialogAPI?, market: MarketAPI?, actionData: MarketCMD.TempData?) {
        // setTargetComplete adds an unknown id as a new target, so only mark markets that are already targets.
        if (market != null && market.id in targets) setTargetComplete(market.id)
    }

    override fun reportRaidForValuablesFinishedBeforeCargoShown(
        dialog: InteractionDialogAPI?, market: MarketAPI?, actionData: MarketCMD.TempData?, cargo: CargoAPI?
    ) {}

    override fun reportRaidToDisruptFinished(dialog: InteractionDialogAPI?, market: MarketAPI?, actionData: MarketCMD.TempData?, industry: Industry?) {}
    override fun reportTacticalBombardmentFinished(dialog: InteractionDialogAPI?, market: MarketAPI?, actionData: MarketCMD.TempData?) {}
}
