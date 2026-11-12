package org.wisp.magicachievements;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.listeners.DiscoverEntityListener;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.impl.campaign.DerelictShipEntityPlugin;
import org.magiclib.achievements.MagicAchievement;
import org.magiclib.util.MagicMisc;

/**
 * Found a capital derelict within the first month.
 * Derelict ships are wreck entities with a {@link DerelictShipEntityPlugin}, not fleets.
 */
public class LuckyStartAchievement extends MagicAchievement implements DiscoverEntityListener {
    int days = 30;

    @Override
    public void onSaveGameLoaded(boolean isComplete) {
        super.onSaveGameLoaded(isComplete);
        if (isComplete) return;
        if (MagicMisc.getElapsedDaysSinceGameStart() > days)
            return;

        Global.getSector().getListenerManager().addListener(this, true);
    }

    @Override
    public void onDestroyed() {
        super.onDestroyed();
        Global.getSector().getListenerManager().removeListener(this);
    }

    @Override
    public void reportEntityDiscovered(SectorEntityToken entity) {
        if (entity == null || MagicMisc.getElapsedDaysSinceGameStart() > days) return;
        if (!(entity.getCustomPlugin() instanceof DerelictShipEntityPlugin plugin)) return;

        DerelictShipEntityPlugin.DerelictShipData data = plugin.getData();
        if (data == null || data.ship == null) return;

        ShipVariantAPI variant = data.ship.getVariant();
        if (variant == null || variant.getHullSize() != ShipAPI.HullSize.CAPITAL_SHIP) return;

        completeAchievement();
        Global.getLogger(this.getClass()).info("Lucky start achievement: found derelict '" + variant.getHullSpec().getHullName() + "'.");
        onDestroyed();
    }
}
