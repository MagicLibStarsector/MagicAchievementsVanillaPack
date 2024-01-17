package org.wisp.magicachievements;

import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.Global;
import org.magiclib.achievements.MagicAchievement;

/**
 * Won the duel.
 */
public class DuelAchievement extends MagicAchievement {
    @Override
    public void advanceAfterInterval(float amount) {
        if (Global.getSector() == null || Global.getCurrentState() != GameState.CAMPAIGN) return;
        String refKey = "$soe_wonDuel";

        if (Global.getSector().getPlayerMemoryWithoutUpdate().getBoolean(refKey)) {
            completeAchievement();
        }
    }
}
