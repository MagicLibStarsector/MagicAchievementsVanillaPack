package org.wisp.magicachievements

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.characters.PersonAPI
import com.fs.starfarer.api.impl.campaign.plog.PlaythroughLog
import org.magiclib.achievements.MagicAchievement
import org.magiclib.paintjobs.MagicPaintjobManager

/**
 * Built in 20 hullmods.
 */
class BuiltInsAchievement : MagicAchievement() {
    private val key = "magiclib_builtInsAchievement_playerBuiltSmods"
    private val numberNeeded = 20
    override fun onSaveGameLoaded(isComplete: Boolean) {
        if (isComplete) {
            onCompleted(null)
            return
        }
    }

    override fun onCompleted(completedByPlayer: PersonAPI?) {
        MagicPaintjobManager.unlockPaintjob("fury_aquatic")
    }

    override fun advanceAfterInterval(amount: Float) {
        super.advanceAfterInterval(amount)

        val existing: MutableMap<String, MutableSet<String>> =
            builtInsInstalled()
        val countBeforeChange = existing.values.flatten().size

        PlaythroughLog.getInstance().sModsInstalled
            .groupBy({ it.member.id }, { it.sMods })
            .forEach { (shipId, smodIds) ->
                existing[createId(shipId)] =
                    (existing[createId(shipId)] ?: mutableSetOf()).apply { addAll(smodIds.flatten()) }
            }

        memory[key] = existing

        // If the count didn't change, no reason to continue further.
        if (existing.values.flatten().size == countBeforeChange) {
            return
        }

        if (existing.values.flatten().size >= numberNeeded) {
            completeAchievement()
        }

    }

    private fun builtInsInstalled(): MutableMap<String, MutableSet<String>> =
        (memory[key] as? MutableMap<String, MutableSet<String>>?) ?: mutableMapOf()

    private fun createId(shipId: String) = "${Global.getSector().playerPerson.id}-$shipId"

    override fun getHasProgressBar() = true

    override fun getMaxProgress() = numberNeeded.toFloat()

    override fun getProgress() = builtInsInstalled().values.flatten().size.toFloat()
}