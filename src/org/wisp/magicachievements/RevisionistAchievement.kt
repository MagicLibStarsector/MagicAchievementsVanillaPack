package org.wisp.magicachievements

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.characters.PersonAPI
import com.fs.starfarer.api.input.InputEventAPI
import com.fs.starfarer.api.util.IntervalUtil
import org.magiclib.achievements.MagicAchievement
import org.magiclib.paintjobs.MagicPaintjobManager
import java.util.*

/**
 * This achievement is unlocked when the player reloads a save and fights the same battle again.
 */
class RevisionistAchievement : MagicAchievement() {
    companion object {
        private const val FLEET_IDS_HASHED_KEY = "magicachievement_revisionist_fleetIdsInBattleHash"
        private const val LOADED_SAVE_KEY = "magicachievement_revisionist_loadedSave"
        private val interval = IntervalUtil(1f, 1f)
    }

    override fun onSaveGameLoaded(isComplete: Boolean) {
        super.onSaveGameLoaded(isComplete)
        if (isComplete) {
            // If the achievement is complete but the paintjobs are not unlocked, unlock them.
            onCompleted(null)
            return
        }
        achievementMemory[LOADED_SAVE_KEY] = true
    }

    override fun advanceInCombat(amount: Float, events: List<InputEventAPI>, isSimulation: Boolean) {
        super.advanceInCombat(amount, events, isSimulation)
        if (isSimulation || isComplete) return

        // No need to check every frame.
        interval.advance(amount)

        if (interval.intervalElapsed()) {
            // This line is why I'm using Kotlin instead of Java.
            val allFleetIdsInBattle =
                Global.getCombatEngine().ships.mapNotNull { it.fleetMember?.captain?.fleet?.id }.distinct()
            val fleetIdsHashed = Objects.hash(allFleetIdsInBattle)
            if (allFleetIdsInBattle.size < 2) return // During deployment, the other fleet is not in the battle yet.

            // If the fleet ids hash is the same as what was stored, then the player is either
            // in the same battle as last `advanceInCombat`, or in a reloaded battle with the same fleets.
            if (achievementMemory[FLEET_IDS_HASHED_KEY] as? Int == fleetIdsHashed) {
                // If the "loaded save" is true, then they are fighting the same fleets after (re)loading a save.
                if (achievementMemory[LOADED_SAVE_KEY] == true) {
                    completeAchievement()
                }
            } else {
                // If the fleet ids hash is different, then the player is in a different battle.
                // Store the new hash and reset the "loaded save" flag.
                achievementMemory[FLEET_IDS_HASHED_KEY] = fleetIdsHashed
                achievementMemory[LOADED_SAVE_KEY] = false
            }
        }
    }

    override fun onCompleted(completedByPlayer: PersonAPI?) {
        MagicPaintjobManager.unlockPaintjob("champion_acidTrip")
        MagicPaintjobManager.unlockPaintjob("conquest_acidTrip")
        MagicPaintjobManager.unlockPaintjob("pegasus_acidTrip")
        MagicPaintjobManager.unlockPaintjob("sunder_acidTrip")
        MagicPaintjobManager.unlockPaintjob("hammerhead_acidTrip")
    }
}
