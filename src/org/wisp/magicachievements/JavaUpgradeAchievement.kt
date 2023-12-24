package org.wisp.magicachievements

import com.fs.starfarer.api.characters.PersonAPI
import org.magiclib.achievements.MagicAchievement
import org.magiclib.paintjobs.MagicPaintjobManager

class JavaUpgradeAchievement : MagicAchievement() {
    override fun onSaveGameLoaded(isComplete: Boolean) {
        if (isComplete) {
            onCompleted(null)
            return
        }

        if (!System.getProperty("java.runtime.version").contains("1.7.0", ignoreCase = true)) {
            completeAchievement()
        }
    }

    override fun onCompleted(completedByPlayer: PersonAPI?) {
        MagicPaintjobManager.unlockPaintjob("champion_gold")
    }
}