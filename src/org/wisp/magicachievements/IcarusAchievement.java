package org.wisp.magicachievements;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.util.Misc;
import org.magiclib.achievements.MagicAchievement;

/**
 * Flew into a sun.
 */
public class IcarusAchievement extends MagicAchievement {
    @Override
    public void advanceAfterInterval(float amount) {
        CampaignFleetAPI playerFleet = Global.getSector().getPlayerFleet();

        // Check if the player is inside a star's radius.
        for (PlanetAPI planet : playerFleet.getContainingLocation().getPlanets()) {
            if (planet.isNormalStar() && Misc.getDistance(planet, playerFleet) < planet.getRadius()) {
                completeAchievement();
                return;
            }
        }
    }
}
