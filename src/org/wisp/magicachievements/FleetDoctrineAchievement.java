package org.wisp.magicachievements;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.DeployedFleetMemberAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.util.Misc;
import org.magiclib.achievements.MagicAchievement;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Deployed ships of (almost) every vanilla design type in one battle.
 */
public class FleetDoctrineAchievement extends MagicAchievement {
    // The design types (manufacturers) in ship_hulls.csv and the hull skins.
    private static final List<String> DESIGN_TYPES = List.of(
            "Hegemony", "High Tech", "Lion's Guard", "Low Tech", "Luddic Church", "Luddic Path", "Midline", "Pirate",
            "Remnant", "Tri-Tachyon", "XIV Battlegroup");

    @Override
    public void advanceInCombat(float amount, List<InputEventAPI> events, boolean isSimulation) {
        if (isSimulation) return;

        Set<String> deployedTypes = new HashSet<>();

        for (DeployedFleetMemberAPI ship : Global.getCombatEngine().getFleetManager(FleetSide.PLAYER).getAllEverDeployedCopy()) {
            // Skip allied ships.
            if (ship.getMember().isAlly())
                continue;

            deployedTypes.add(ship.getMember().getHullSpec().getManufacturer());
        }

        if (deployedTypes.containsAll(DESIGN_TYPES)) {
            completeAchievement();
        }
    }

    @Override
    public String getTooltip() {
        return Misc.getJoined("and", DESIGN_TYPES) + ".";
    }
}
