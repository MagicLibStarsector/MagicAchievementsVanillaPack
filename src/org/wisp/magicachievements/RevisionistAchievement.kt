package org.wisp.magicachievements

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.input.InputEventAPI
import com.fs.starfarer.api.util.IntervalUtil
import java.util.*

/**
 * Un-lost a battle: the player loads a save and fights the same fleets as in their last battle.
 */
class RevisionistAchievement : VanillaPackAchievement(
    paintjobRewards = listOf("champion_acidTrip", "conquest_acidTrip", "pegasus_acidTrip", "sunder_acidTrip", "hammerhead_acidTrip")
) {
    companion object {
        private const val FLEET_IDS_HASHED_KEY = "magicachievement_revisionist_fleetIdsInBattleHash"
        private const val LOADED_SAVE_KEY = "magicachievement_revisionist_loadedSave"
        private val interval = IntervalUtil(1f, 1f)
    }

    override fun onSaveGameLoaded(isComplete: Boolean) {
        super.onSaveGameLoaded(isComplete)
        if (!isComplete) achievementMemory[LOADED_SAVE_KEY] = true
    }

    override fun advanceInCombat(amount: Float, events: List<InputEventAPI>, isSimulation: Boolean) {
        super.advanceInCombat(amount, events, isSimulation)
        if (isSimulation) return

        // No need to check every frame.
        interval.advance(amount)
        if (!interval.intervalElapsed()) return

        // This line is why I'm using Kotlin instead of Java.
        // Uses each ship's fleet. A captain's fleet is only set for officers, not the player character.
        // Sorted so the order ships were deployed in doesn't change the hash.
        val allFleetIdsInBattle =
            Global.getCombatEngine().ships.mapNotNull { it.fleetMember?.fleetData?.fleet?.id }.distinct().sorted()
        if (allFleetIdsInBattle.size < 2) return // During deployment, the other fleet is not in the battle yet.
        val fleetIdsHashed = Objects.hash(allFleetIdsInBattle)

        if (achievementMemory[FLEET_IDS_HASHED_KEY] as? Int == fleetIdsHashed) {
            // The same fleets as the last battle. After a load, that's the player fighting it again.
            if (achievementMemory[LOADED_SAVE_KEY] == true) completeAchievement()
        } else {
            // A different battle. Remember its fleets, and that it wasn't fought again after a load.
            achievementMemory[FLEET_IDS_HASHED_KEY] = fleetIdsHashed
            achievementMemory[LOADED_SAVE_KEY] = false
        }
    }
}
