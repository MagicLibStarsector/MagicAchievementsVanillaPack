package org.wisp.magicachievements

import org.magiclib.achievements.ShipKillsAchievement

class ConquestKillsAchievement : ShipKillsAchievement(
    playerShipHullIds = listOf("conquest"),
    killCount = 25f,
    rewardedPaintjobIds = listOf("conquest_tigerStripes")
)

class ChampionKillsAchievement : ShipKillsAchievement(
    playerShipHullIds = listOf("champion"),
    killCount = 25f,
    rewardedPaintjobIds = listOf("champion_tigerStripes")
)

class EagleKillsAchievement : ShipKillsAchievement(
    playerShipHullIds = listOf("eagle"),
    killCount = 25f,
    rewardedPaintjobIds = listOf("eagle_tigerStripes")
)

class FalconKillsAchievement : ShipKillsAchievement(
    playerShipHullIds = listOf("falcon"),
    killCount = 25f,
    rewardedPaintjobIds = listOf("falcon_tigerStripes")
)

class HammerheadKillsAchievement : ShipKillsAchievement(
    playerShipHullIds = listOf("hammerhead"),
    killCount = 25f,
    rewardedPaintjobIds = listOf("hammerhead_tigerStripes")
)

class SunderKillsAchievement : ShipKillsAchievement(
    playerShipHullIds = listOf("sunder"),
    killCount = 25f,
    rewardedPaintjobIds = listOf("sunder_tigerStripes")
)