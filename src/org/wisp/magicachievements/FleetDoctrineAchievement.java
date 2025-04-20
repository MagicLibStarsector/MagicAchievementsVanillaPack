package org.wisp.magicachievements;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DeployedFleetMemberAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.util.Misc;
import org.magiclib.achievements.MagicAchievement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FleetDoctrineAchievement extends MagicAchievement {
    private final Set<String> allTechTypes = new HashSet<>();


    @Override
    public void onSaveGameLoaded(boolean isComplete) {
        super.onSaveGameLoaded(isComplete);
        if (isComplete) return;
        allTechTypes.clear();

        // Grab column from ship_hulls.csv and then search skins folder for "TECH", paste into VS Code, wrangle.
        allTechTypes.add("Hegemony");
        allTechTypes.add("High Tech");
        allTechTypes.add("Lion's Guard");
        allTechTypes.add("Low Tech");
        allTechTypes.add("Luddic Church");
        allTechTypes.add("Luddic Path");
        allTechTypes.add("Midline");
        allTechTypes.add("Pirate");
        allTechTypes.add("Remnant");
        allTechTypes.add("Tri-Tachyon");
        allTechTypes.add("XIV Battlegroup");
    }

    @Override
    public void advanceInCombat(float amount, List<InputEventAPI> events, boolean isSimulation) {
        if (isSimulation) return;
        if (allTechTypes.isEmpty()) return;
        CombatEngineAPI combatEngine = Global.getCombatEngine();

        Set<String> techTypesDeployed = new HashSet<>();

        for (DeployedFleetMemberAPI ship : combatEngine.getFleetManager(FleetSide.PLAYER).getAllEverDeployedCopy()) {
            // Skip allied ships.
            if (ship.getMember().isAlly())
                continue;

            techTypesDeployed.add(ship.getMember().getHullSpec().getManufacturer());
        }

        for (String reqType : allTechTypes) {
            if (!techTypesDeployed.contains(reqType)) {
                return;
            }
        }

        completeAchievement();
    }

    @Override
    public String getTooltip() {
        return Misc.getJoined("and", new ArrayList<>(allTechTypes)) + ".";
    }
}
