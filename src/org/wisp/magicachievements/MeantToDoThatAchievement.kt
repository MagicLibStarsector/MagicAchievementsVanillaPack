package org.wisp.magicachievements

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.combat.CombatEntityAPI
import com.fs.starfarer.api.combat.ShipAPI
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI
import com.fs.starfarer.api.combat.listeners.DamageListener
import com.fs.starfarer.api.input.InputEventAPI

/**
 * Destroyed an enemy ship with your own ship's explosion: hull damage from the dead player ship kills its target.
 */
class MeantToDoThatAchievement : VanillaPackAchievement(paintjobRewards = listOf("cryo_phaeton")) {
    // A ship's DamageListener hears damage dealt to that ship, so every ship gets this one listener.
    private val damageListener = object : DamageListener {
        override fun reportDamageApplied(source: Any?, target: CombatEntityAPI?, result: ApplyDamageResultAPI?) {
            if (Global.getSettings().isDevMode || result == null || result.isDps || result.damageToHull <= 0f) return
            if (target !is ShipAPI || source !is ShipAPI || source != Global.getCombatEngine().playerShip) return
            if (target.isAlive || source.isAlive) return

            logger.info("Damage (${result.damageToHull} ${result.type.name} to hull) applied to ${target.id} by ${source.id}, with both dead.")
            completeAchievement()
            Global.getCombatEngine().ships.forEach { it.removeListener(this) }
        }
    }

    override fun advanceInCombat(amount: Float, events: List<InputEventAPI>, isSimulation: Boolean) {
        super.advanceInCombat(amount, events, isSimulation)
        if (isSimulation) return

        for (ship in Global.getCombatEngine().ships) {
            if (ship.isAlive && !ship.hasListener(damageListener)) ship.addListener(damageListener)
        }
    }
}
