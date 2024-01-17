package org.wisp.magicachievements;

import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.impl.campaign.ids.People;
import org.magiclib.achievements.MagicAchievement;

/**
 * 100 rep with Sebesty.
 */
public class SebestyenAchievement extends MagicAchievement {
    @Override
    public void advanceAfterInterval(float amount) {
        if (Global.getSector() == null || Global.getCurrentState() != GameState.CAMPAIGN) return;

        PersonAPI sebesty = Global.getSector().getImportantPeople().getPerson(People.SEBESTYEN);
        if (sebesty == null) return;

        if (sebesty.getRelToPlayer().getRepInt() >= 100) {
            completeAchievement();
        }
    }
}
