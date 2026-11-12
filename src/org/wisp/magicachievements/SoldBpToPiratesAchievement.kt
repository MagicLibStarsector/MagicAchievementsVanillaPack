package org.wisp.magicachievements

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.PlayerMarketTransaction
import com.fs.starfarer.api.campaign.econ.MarketAPI
import com.fs.starfarer.api.campaign.impl.items.BlueprintProviderItem
import com.fs.starfarer.api.campaign.listeners.ColonyInteractionListener
import com.fs.starfarer.api.impl.campaign.ids.Factions
import org.magiclib.achievements.MagicAchievement

/**
 * Sold a blueprint to the pirates.
 *
 * Counts any item whose plugin provides blueprints (ship, weapon, fighter and industry blueprints, and blueprint packages,
 * including modded ones that use those plugins), sold at any submarket of a pirate-owned market.
 */
class SoldBpToPiratesAchievement : MagicAchievement(), ColonyInteractionListener {
    override fun onSaveGameLoaded(isComplete: Boolean) {
        super.onSaveGameLoaded(isComplete)
        if (isComplete) return

        if (!Global.getSector().listenerManager.hasListener(this))
            Global.getSector().listenerManager.addListener(this, true)
    }

    override fun onDestroyed() {
        super.onDestroyed()
        Global.getSector()?.listenerManager?.removeListener(this)
    }

    override fun reportPlayerMarketTransaction(transaction: PlayerMarketTransaction?) {
        try {
            transaction ?: return
            if (transaction.market?.factionId != Factions.PIRATES) return
            val submarketPlugin = transaction.submarket?.plugin ?: return
            // Storage and other free-transfer submarkets aren't a sale.
            if (submarketPlugin.isFreeTransfer) return

            val soldBlueprint = transaction.sold?.stacksCopy.orEmpty()
                .any { it.isSpecialStack && it.plugin is BlueprintProviderItem }

            if (soldBlueprint) {
                completeAchievement()
                onDestroyed()
            }
        } catch (e: Exception) {
            logger.warn(e.message, e)
        }
    }

    override fun reportPlayerOpenedMarket(market: MarketAPI?) = Unit
    override fun reportPlayerClosedMarket(market: MarketAPI?) = Unit
    override fun reportPlayerOpenedMarketAndCargoUpdated(market: MarketAPI?) = Unit
}
