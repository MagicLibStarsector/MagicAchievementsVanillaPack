package org.wisp.magicachievements

import com.fs.starfarer.api.campaign.PlayerMarketTransaction
import com.fs.starfarer.api.campaign.econ.MarketAPI
import com.fs.starfarer.api.campaign.impl.items.BlueprintProviderItem
import com.fs.starfarer.api.campaign.listeners.ColonyInteractionListener
import com.fs.starfarer.api.impl.campaign.ids.Factions

/**
 * Sold a blueprint to the pirates.
 *
 * Counts any item whose plugin provides blueprints (ship, weapon, fighter and industry blueprints, and blueprint packages,
 * including modded ones that use those plugins), sold at any submarket of a pirate-owned market.
 */
class SoldBpToPiratesAchievement : ListenerAchievement(), ColonyInteractionListener {
    override fun reportPlayerMarketTransaction(transaction: PlayerMarketTransaction?) {
        val soldToPirates = transaction?.market?.factionId == Factions.PIRATES
                // Storage and other free-transfer submarkets aren't a sale.
                && transaction?.submarket?.plugin?.isFreeTransfer == false
        val soldBlueprint = transaction?.sold?.stacksCopy.orEmpty()
            .any { it.isSpecialStack && it.plugin is BlueprintProviderItem }

        if (soldToPirates && soldBlueprint) completeAchievement()
    }

    override fun reportPlayerOpenedMarket(market: MarketAPI?) {}
    override fun reportPlayerClosedMarket(market: MarketAPI?) {}
    override fun reportPlayerOpenedMarketAndCargoUpdated(market: MarketAPI?) {}
}
