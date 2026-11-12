package org.wisp.magicachievements;

import com.fs.starfarer.api.Global;
import org.magiclib.achievements.MagicAchievement;

/**
 * Won the duel.
 */
public class DuelAchievement extends MagicAchievement {
    @Override
    public void advanceAfterInterval(float amount) {
        // Set by the rule `soeFightWin`.
        if (Global.getSector().getPlayerMemoryWithoutUpdate().getBoolean("$soe_wonDuel")) {
            completeAchievement();
        }
    }
}
