package org.wisp.magicachievements

import com.fs.starfarer.api.campaign.PlanetAPI
import com.fs.starfarer.api.campaign.listeners.SurveyPlanetListener

/**
 * Surveyed a planet with 75% hazard or better.
 */
class ParadiseAchievement : ListenerAchievement(), SurveyPlanetListener {
    override fun reportPlayerSurveyedPlanet(planet: PlanetAPI?) {
        val hazard = planet?.market?.hazardValue ?: return
        if (hazard <= 0.75f) completeAchievement()
    }
}
