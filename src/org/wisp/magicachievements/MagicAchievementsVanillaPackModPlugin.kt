package org.wisp.magicachievements

import com.fs.starfarer.api.BaseModPlugin
import org.magiclib.achievements.MagicAchievementManager
import org.magiclib.paintjobs.MagicPaintjobManager
import org.magiclib.paintjobs.MagicPaintjobSpec

class MagicAchievementsVanillaPackModPlugin : BaseModPlugin() {
    val modId = "wisp_magicAchievementsVanillaPack"
    val modName = "Magic Achievements: Vanilla Pack"

    override fun onGameLoad(newGame: Boolean) {
        super.onGameLoad(newGame)

        for (spec in Spoilers.getSpoilerAchievementSpecs()) {
            MagicAchievementManager.getInstance().addAchievementSpecs(spec)
        }

        addPaintjobs()
    }

    fun addPaintjobs() {
        MagicPaintjobManager.addPaintJob(
            MagicPaintjobSpec(
                modId = modId,
                modName = modName,
                id = "sra_charybdis_orig",
                hullId = "ms_boss_charybdis",
                name = "Original",
                description = null,
                unlockedAutomatically = true,
                spriteId = "graphics/paintjobs/ms_boss_charybdis.png"
            )
        )
    }
}