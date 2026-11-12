package org.wisp.magicachievements;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.impl.campaign.ids.People;
import org.magiclib.achievements.MagicAchievement;

/**
 * 100 rep with Sebestyen.
 */
public class SebestyenAchievement extends MagicAchievement {
    @Override
    public void advanceAfterInterval(float amount) {
        PersonAPI sebestyen = Global.getSector().getImportantPeople().getPerson(People.SEBESTYEN);
        if (sebestyen == null) return;

        if (sebestyen.getRelToPlayer().getRepInt() >= 100) {
            completeAchievement();
        }
    }
}
