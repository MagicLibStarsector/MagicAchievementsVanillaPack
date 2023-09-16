package org.wisp.magicachievements;

import com.fs.starfarer.api.Global;
import org.magiclib.achievements.MagicAchievement;
import org.magiclib.util.MagicMisc;

public class TestAchievement extends MagicAchievement {

    @Override
    public void advanceAfterInterval(float amount) {
        if (MagicMisc.getElapsedDaysSinceGameStart() > 5) {
            completeAchievement(Global.getSector().getPlayerPerson());
            saveChanges();
        }
    }

    @Override
    public Float getProgress() {
        if (isComplete())
            return getMaxProgress();
        return Math.min(MagicMisc.getElapsedDaysSinceGameStart(), getMaxProgress());
    }

    @Override
    public Float getMaxProgress() {
        return 5f;
    }
}
