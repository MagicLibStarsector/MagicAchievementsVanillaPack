package org.wisp.magicachievements

import com.fs.starfarer.api.campaign.PlanetAPI
import com.fs.starfarer.api.campaign.listeners.SurveyPlanetListener

/**
 * Surveyed a planet with no resources: none of its conditions need a survey to be seen.
 * (No vanilla planet has zero conditions.)
 */
class NoConditionsAchievement : ListenerAchievement(), SurveyPlanetListener {
    override fun reportPlayerSurveyedPlanet(planet: PlanetAPI?) {
        val conditions = planet?.market?.conditions ?: return
        if (conditions.none { it.requiresSurveying() }) completeAchievement()
    }
}
