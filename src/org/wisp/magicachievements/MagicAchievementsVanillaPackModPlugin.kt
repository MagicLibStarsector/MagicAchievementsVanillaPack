package org.wisp.magicachievements

import com.fs.starfarer.api.BaseModPlugin
import org.magiclib.achievements.MagicAchievementManager

class MagicAchievementsVanillaPackModPlugin : BaseModPlugin() {
    override fun onGameLoad(newGame: Boolean) {
        super.onGameLoad(newGame)

        for (spec in Spoilers.getSpoilerAchievementSpecs()) {
            MagicAchievementManager.getInstance().addAchievementSpecs(spec)
        }
    }
}