package org.wisp.magicachievements;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DeployedFleetMemberAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.mission.FleetSide;
import org.magiclib.achievements.MagicAchievement;

import java.util.List;

/**
 * Deploy 30 Fighter Bays.
 */
public class FighterBaysAchievement extends MagicAchievement {

    @Override
    public void advanceInCombat(float amount, List<InputEventAPI> events, boolean isSimulation) {
        if (isSimulation) return;
        int countNeeded = 30;

        CombatEngineAPI combatEngine = Global.getCombatEngine();

        List<DeployedFleetMemberAPI> playerShips = combatEngine.getFleetManager(FleetSide.PLAYER).getAllEverDeployedCopy();
        int fighterBaysCount = 0;

        for (DeployedFleetMemberAPI ship : playerShips) {
            // Skip allied ships.
            if (ship.getMember().isAlly())
                continue;

            fighterBaysCount += ship.getMember().getVariant().getFittedWings().size();
        }

        if (fighterBaysCount >= countNeeded) {
            completeAchievement();
        }
    }
}
