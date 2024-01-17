package org.wisp.magicachievements;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DeployedFleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.mission.FleetSide;
import org.magiclib.achievements.MagicAchievement;

import java.util.List;

/**
 * Deploy 10 ships with Safety Overrides.
 */
public class SafetiesOffAchievement extends MagicAchievement {

    @Override
    public void advanceInCombat(float amount, List<InputEventAPI> events, boolean isSimulation) {
        if (isSimulation) return;
        int countNeeded = 10;

        CombatEngineAPI combatEngine = Global.getCombatEngine();

        List<DeployedFleetMemberAPI> playerShips = combatEngine.getFleetManager(FleetSide.PLAYER).getAllEverDeployedCopy();
        if (playerShips.size() < countNeeded) return;
        int safetyOverridesCount = 0;

        for (DeployedFleetMemberAPI ship : playerShips) {
            // Skip allied ships.
            if (ship.getMember().isAlly())
                continue;

            if (ship.getMember().getVariant().hasHullMod(HullMods.SAFETYOVERRIDES)) {
                safetyOverridesCount++;
            }
        }

        if (safetyOverridesCount >= countNeeded) {
            completeAchievement();
        }
    }
}
