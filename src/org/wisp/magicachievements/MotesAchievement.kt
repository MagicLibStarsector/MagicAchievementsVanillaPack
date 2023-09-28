package org.wisp.magicachievements

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.impl.campaign.ghosts.SensorGhostManager
import com.fs.starfarer.api.impl.campaign.ghosts.types.*
import org.magiclib.achievements.MagicTargetListAchievement

/**
 * Encountered eight different hyperspace ghosts.
 */
class MotesAchievement : MagicTargetListAchievement() {
    override fun onApplicationLoaded(isComplete: Boolean) {
        super.onApplicationLoaded(isComplete)
        if (isComplete) return

        setTargets(
            mapOf(
                ChargerGhost::class.java.simpleName to "Charger",
                EchoGhost::class.java.simpleName to "Echo",
                EncounterTricksterGhost::class.java.simpleName to "Encounter Trickster",
                GuideGhost::class.java.simpleName to "Guide",
                LeviathanGhost::class.java.simpleName to "Leviathan",
                LeviathanCalfGhost::class.java.simpleName to "Leviathan Calf",
                MinnowGhost::class.java.simpleName to "Minnow",
                RacerGhost::class.java.simpleName to "Racer",
                RemnantGhost::class.java.simpleName to "Remnant",
                RemoraGhost::class.java.simpleName to "Remora",
                ShipGhost::class.java.simpleName to "Ship",
                StormcallerGhost::class.java.simpleName to "Stormcaller",
                StormTricksterGhost::class.java.simpleName to "Storm Trickster",
                ZigguratGhost::class.java.simpleName to "Mote",
            )
        )
    }

    override fun advanceAfterInterval(amount: Float) {
        super.advanceAfterInterval(amount)
        if (Global.getSector().playerFleet == null) return

        val motesSeen = targets.filter { it.value.isComplete }.keys

        SensorGhostManager.getGhostManager().ghosts
            .map { it.javaClass.simpleName }
            .forEach { newMoteSeen ->
                if (newMoteSeen !in motesSeen) {
                    setTargetComplete(newMoteSeen)
                }
            }
    }

    // Only need to see 8, not all possible ones, so override [MagicTargetListAchievement] logic.
    override fun getMaxProgress(): Float = 8f
}
