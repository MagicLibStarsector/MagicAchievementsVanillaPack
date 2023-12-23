package org.wisp.magicachievements;

import com.fs.starfarer.api.campaign.CustomCampaignEntityAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.impl.campaign.ids.Entities;
import com.fs.starfarer.api.util.Misc;
import org.magiclib.achievements.MagicAchievement;
import org.magiclib.paintjobs.MagicPaintjobManager;

/**
 * Using a fusion lamp.
 */
public class PowerOfTheSunAchievement extends MagicAchievement {
    @Override
    public void onSaveGameLoaded(boolean isComplete) {
        super.onSaveGameLoaded(isComplete);
        // If player already has the achievement, unlock the paintjob just in case it wasn't unlocked.
        if (isComplete) {
            onCompleted(null);
        }
    }

    @Override
    public void advanceAfterInterval(float amount) {
        super.advanceAfterInterval(amount);

        for (MarketAPI market : Misc.getPlayerMarkets(false)) {
            for (CustomCampaignEntityAPI curr : market.getContainingLocation().getCustomEntities()) {
                if (curr.getCustomEntityType().equals(Entities.FUSION_LAMP) && curr.getOrbitFocus() == market.getPrimaryEntity()) {
                    completeAchievement();
                }
            }
        }
    }

    @Override
    public void onCompleted(PersonAPI completedByPlayer) {
        super.onCompleted(completedByPlayer);
        MagicPaintjobManager.unlockPaintjob("falcon_phoenix");
    }
}
