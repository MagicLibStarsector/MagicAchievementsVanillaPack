package org.wisp.magicachievements;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.InteractionDialogPlugin;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.rules.MemKeys;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.impl.campaign.econ.RogueAICore;
import com.fs.starfarer.api.util.Misc;
import org.magiclib.achievements.MagicAchievement;

/**
 * Had an AI core go rogue.
 * <p>
 * Mmm, delicious spaghetti code.
 */
public class AICoreAchievement extends MagicAchievement {
    @Override
    public void advanceAfterInterval(float amount) {
        super.advanceAfterInterval(amount);

        if (Global.getSector() == null) {
            return;
        }

        InteractionDialogAPI dialog = Global.getSector().getCampaignUI().getCurrentInteractionDialog();
        if (dialog == null) {
            return;
        }

        InteractionDialogPlugin plugin = dialog.getPlugin();
        if (plugin == null) {
            return;
        }

        MemoryAPI localMem = dialog.getPlugin().getMemoryMap().get(MemKeys.LOCAL);
        if (localMem == null) {
            return;
        }

        Object optionSelected = localMem.get("$option");
        if (optionSelected == null) {
            return;
        }

        if (optionSelected.toString().startsWith("RACA_")) {
            completeAchievement();
        }

        for (MarketAPI playerMarket : Misc.getPlayerMarkets(false)) {

            if (RogueAICore.get(playerMarket) != null) {
                completeAchievement();
            }
        }
    }
}
