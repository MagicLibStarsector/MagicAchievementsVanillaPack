package org.wisp.magicachievements

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.PlanetAPI
import com.fs.starfarer.api.campaign.listeners.SurveyPlanetListener
import org.magiclib.achievements.MagicAchievement
import org.magiclib.achievements.MagicAchievementRarity
import org.magiclib.achievements.MagicAchievementSpec
import org.magiclib.achievements.MagicAchievementSpoilerLevel
import org.magiclib.util.MagicMisc
import org.magiclib.util.MagicVariables

internal object Spoilers {
    @JvmStatic
    fun getSpoilerAchievementSpecs(): List<MagicAchievementSpec> {
        return listOf(
            OverInvestedAchievementSpec()
        )
    }
}

/**
 * The part that would normally be in the csv, but in code to hide it better from prying eyes.
 */
internal class OverInvestedAchievementSpec : MagicAchievementSpec(
    modId = MagicVariables.MAGICLIB_ID,
    modName = Global.getSettings().modManager.getModSpec(MagicVariables.MAGICLIB_ID).name,
    id = "overinvested",
    name = "Overinvested",
    description = "Played the same save for 30 cycles.",
    tooltip = null,
    script = OverInvestedAchievement::class.java.name,
    image = null,
    spoilerLevel = MagicAchievementSpoilerLevel.Hidden,
    rarity = MagicAchievementRarity.Epic
)

/**
 * The logic for the achievement, which is always in code.
 */
internal class OverInvestedAchievement : MagicAchievement() {
    override fun advanceAfterInterval(amount: Float) {
        if (MagicMisc.getElapsedDaysSinceGameStart() > (365 * 30)) {
            completeAchievement()
        }
    }
}

/**
 * The part that would normally be in the csv, but in code to hide it better from prying eyes.
 * Unused, can't really get it without mods.
 */
internal class OldEarthAchievementSpec : MagicAchievementSpec(
    modId = MagicVariables.MAGICLIB_ID,
    modName = Global.getSettings().modManager.getModSpec(MagicVariables.MAGICLIB_ID).name,
    id = "oldearth",
    name = "Old Earth",
    description = "Surveyed a planet with 50% hazard or better.",
    tooltip = null,
    script = "org.wisp.magicachievements.OldEarthAchievement",
    image = null,
    spoilerLevel = MagicAchievementSpoilerLevel.Hidden,
    rarity = MagicAchievementRarity.Legendary
)

internal class OldEarthAchievement : MagicAchievement(), SurveyPlanetListener {
    override fun onSaveGameLoaded(isComplete: Boolean) {
        super.onSaveGameLoaded(isComplete)
        if (isComplete) return
        Global.getSector().listenerManager.addListener(this, true)
    }

    override fun onDestroyed() {
        super.onDestroyed()
        Global.getSector().listenerManager.removeListener(this)
    }

    override fun reportPlayerSurveyedPlanet(planet: PlanetAPI?) {
        if ((planet?.market?.hazardValue ?: 0f) <= 0.50f) {
            completeAchievement()
        }
    }
}