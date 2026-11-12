package org.wisp.magicachievements

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.impl.campaign.plog.PlaythroughLog
import org.json.JSONArray
import org.magiclib.kotlin.toStringList

/**
 * Built in 20 hullmods, counted across all saves. Each hullmod counts once per ship and player character.
 */
class BuiltInsAchievement : VanillaPackAchievement(paintjobRewards = listOf("fury_aquatic")) {
    // A flat list of "<player id>-<ship id>-<hullmod id>". MagicLib stores memory as JSON, so a list comes back as a
    // JSONArray after a load. (The old key held a map, which came back as a JSONObject and was never read again.)
    private val key = "magiclib_builtInsAchievement_builtInIds"
    private val numberNeeded = 20

    override fun advanceAfterInterval(amount: Float) {
        super.advanceAfterInterval(amount)

        val builtIns = builtInsInstalled()
        val countBefore = builtIns.size
        val playerId = Global.getSector().playerPerson.id
        for (record in PlaythroughLog.getInstance().sModsInstalled) {
            val shipId = record.member?.id ?: continue
            record.sMods.forEach { builtIns += "$playerId-$shipId-$it" }
        }
        if (builtIns.size == countBefore) return

        achievementMemory[key] = builtIns.toList()
        if (builtIns.size >= numberNeeded) completeAchievement()
    }

    private fun builtInsInstalled(): MutableSet<String> = when (val stored = achievementMemory[key]) {
        is JSONArray -> stored.toStringList().toMutableSet()
        is Collection<*> -> stored.filterIsInstance<String>().toMutableSet()
        else -> mutableSetOf()
    }

    override fun getHasProgressBar() = true
    override fun getMaxProgress() = numberNeeded.toFloat()
    override fun getProgress() = builtInsInstalled().size.toFloat()
}
