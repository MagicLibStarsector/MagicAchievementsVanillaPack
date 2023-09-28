package org.wisp.magicachievements

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.CargoAPI
import com.fs.starfarer.api.campaign.InteractionDialogAPI
import com.fs.starfarer.api.campaign.econ.Industry
import com.fs.starfarer.api.campaign.econ.MarketAPI
import com.fs.starfarer.api.campaign.listeners.ColonyPlayerHostileActListener
import com.fs.starfarer.api.impl.campaign.rulecmd.salvage.MarketCMD
import org.magiclib.achievements.MagicTargetListAchievement

class SatBombEverybodyAchievement : MagicTargetListAchievement(), ColonyPlayerHostileActListener {
    override fun onSaveGameLoaded(isComplete: Boolean) {
        super.onSaveGameLoaded(isComplete)
        if (isComplete) return

        setTargets(
            listOf(
                "chicomoztoc",
                "gilead",
                "hesperus", // TODO ?
                "jangala",
                "volturn",
            )
                .associateWith { Global.getSector().economy.getMarket(it)?.name ?: it }
        )

        Global.getSector().listenerManager.addListener(this, true)
    }

    override fun onDestroyed() {
        super.onDestroyed()
        Global.getSector().listenerManager.removeListener(this)
    }

    override fun reportSaturationBombardmentFinished(
        dialog: InteractionDialogAPI?,
        market: MarketAPI?,
        actionData: MarketCMD.TempData?
    ) {
        market ?: return
        actionData ?: return

        if (market.id !in targets.keys || market.id in targets.filterValues { it.isComplete })
            return

        setTargetComplete(market.id)
    }

    override fun reportRaidForValuablesFinishedBeforeCargoShown(
        dialog: InteractionDialogAPI?,
        market: MarketAPI?,
        actionData: MarketCMD.TempData?,
        cargo: CargoAPI?
    ) = Unit

    override fun reportRaidToDisruptFinished(
        dialog: InteractionDialogAPI?,
        market: MarketAPI?,
        actionData: MarketCMD.TempData?,
        industry: Industry?
    ) = Unit

    override fun reportTacticalBombardmentFinished(
        dialog: InteractionDialogAPI?,
        market: MarketAPI?,
        actionData: MarketCMD.TempData?
    ) = Unit
}