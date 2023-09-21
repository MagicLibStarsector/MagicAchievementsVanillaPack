package org.wisp.magicachievements;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.listeners.PlayerColonizationListener;
import org.magiclib.achievements.MagicAchievement;

/**
 * Abandoned a colony.
 */
public class AbandonedAchievement extends MagicAchievement implements PlayerColonizationListener {
    @Override
    public void onSaveGameLoaded(boolean isComplete) {
        super.onSaveGameLoaded(isComplete);
        if (isComplete) return;
        Global.getSector().getListenerManager().addListener(this, true);
    }

    @Override
    public void onDestroyed() {
        super.onDestroyed();
        Global.getSector().getListenerManager().removeListener(this);
    }

    @Override
    public void reportPlayerAbandonedColony(MarketAPI colony) {
        completeAchievement();
        saveChanges();
    }

    @Override
    public void reportPlayerColonizedPlanet(PlanetAPI planet) {

    }
}
