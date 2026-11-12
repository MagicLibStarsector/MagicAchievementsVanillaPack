package org.wisp.magicachievements

import com.fs.starfarer.api.Global
import com.fs.starfarer.api.campaign.CampaignEventListener
import com.fs.starfarer.api.characters.PersonAPI
import org.magiclib.achievements.MagicAchievement
import org.magiclib.paintjobs.MagicPaintjobManager

/**
 * Base for the pack's achievements.
 *
 * MagicLib only calls `advanceAfterInterval` in the campaign and `advanceInCombat` in battle, and neither once the
 * achievement is complete, so subclasses don't need to check for that.
 *
 * @param paintjobRewards Unlocked when the achievement completes, and again on every load once it's complete.
 */
abstract class VanillaPackAchievement(
    private val paintjobRewards: List<String> = emptyList(),
) : MagicAchievement() {
    override fun onSaveGameLoaded(isComplete: Boolean) {
        super.onSaveGameLoaded(isComplete)
        if (isComplete) unlockPaintjobs()
    }

    override fun onCompleted(completedByPlayer: PersonAPI?) {
        super.onCompleted(completedByPlayer)
        unlockPaintjobs()
    }

    private fun unlockPaintjobs() = paintjobRewards.forEach { MagicPaintjobManager.unlockPaintjob(it) }
}

/**
 * An achievement that is itself a listener for the sector's listener manager, for example a [com.fs.starfarer.api.campaign.listeners.SurveyPlanetListener].
 * It's registered as a transient listener while incomplete, and removed when it completes or MagicLib unloads it.
 */
abstract class ListenerAchievement(
    paintjobRewards: List<String> = emptyList(),
) : VanillaPackAchievement(paintjobRewards) {
    /** Whether to listen at all after a load. Only called while incomplete. */
    protected open fun shouldListen(): Boolean = true

    override fun onSaveGameLoaded(isComplete: Boolean) {
        super.onSaveGameLoaded(isComplete)
        if (!isComplete && shouldListen()) Global.getSector().listenerManager.addListener(this, true)
    }

    override fun onCompleted(completedByPlayer: PersonAPI?) {
        super.onCompleted(completedByPlayer)
        stopListening()
    }

    override fun onDestroyed() {
        super.onDestroyed()
        stopListening()
    }

    private fun stopListening() {
        Global.getSector()?.listenerManager?.removeListener(this)
    }
}

/**
 * An achievement that reacts to [CampaignEventListener] events, such as battles and engagements.
 * The listener from [createListener] is added as a transient listener while incomplete, and removed when the
 * achievement completes or MagicLib unloads it.
 */
abstract class CampaignEventAchievement(
    paintjobRewards: List<String> = emptyList(),
) : VanillaPackAchievement(paintjobRewards) {
    private var listener: CampaignEventListener? = null

    protected abstract fun createListener(): CampaignEventListener

    override fun onSaveGameLoaded(isComplete: Boolean) {
        super.onSaveGameLoaded(isComplete)
        stopListening()
        if (!isComplete) listener = createListener().also { Global.getSector().addTransientListener(it) }
    }

    override fun onCompleted(completedByPlayer: PersonAPI?) {
        super.onCompleted(completedByPlayer)
        stopListening()
    }

    override fun onDestroyed() {
        super.onDestroyed()
        stopListening()
    }

    private fun stopListening() {
        listener?.let { Global.getSector()?.removeListener(it) }
        listener = null
    }
}
