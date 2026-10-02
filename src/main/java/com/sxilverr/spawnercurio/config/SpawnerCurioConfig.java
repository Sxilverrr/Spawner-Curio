package com.sxilverr.spawnercurio.config;

import java.util.List;
import java.util.Locale;

public final class SpawnerCurioConfig {

    public enum ListMode {
        BLACKLIST,
        WHITELIST
    }

    public static int checkInterval = 20;
    public static int maxSpawners = 8;

    public static boolean worksInCurioSlot = true;
    public static boolean worksInHand = false;
    public static boolean worksInInventory = false;
    public static List<String> curioSlots = List.of("necklace", "charm");

    public static boolean allowToggle = true;
    public static boolean defaultOn = true;

    public static boolean lavaResistant = true;
    public static boolean glowWhenOn = false;
    public static boolean showTooltip = true;

    public static boolean progressionEnabled = false;
    public static boolean progressionFromKills = true;
    public static boolean progressionFromSpawners = true;
    public static boolean progressionFromXp = false;
    public static int killsPerLevel = 50;
    public static int spawnersPerLevel = 5;
    public static int xpPerLevel = 100;
    public static double xpShare = 0.5;
    public static int maxLevel = 10;
    public static double startSpeed = 1.25;
    public static double startRadius = 4.0;

    public static int spawnCount = -1;
    public static int maxNearbyEntities = -1;
    public static int requiredPlayerRange = -1;

    public static ListMode dimensionMode = ListMode.BLACKLIST;
    public static List<String> dimensions = List.of();
    public static List<String> blockedMobs = List.of();

    public static double lootChance = 0.05;
    public static List<String> lootTables = List.of(
            "minecraft:chests/simple_dungeon",
            "minecraft:chests/abandoned_mineshaft",
            "minecraft:chests/stronghold_corridor",
            "minecraft:chests/nether_bridge",
            "minecraft:chests/bastion_treasure",
            "minecraft:chests/woodland_mansion",
            "minecraft:chests/ancient_city");

    private SpawnerCurioConfig() {
    }

    public static double speedFor(int level, double max) {
        double start = Math.min(startSpeed, max);
        return start + (max - start) * level / maxLevel;
    }

    public static double radiusFor(int level, double max) {
        double start = Math.min(startRadius, max);
        return start + (max - start) * level / maxLevel;
    }

    public static boolean slotAllowed(String slot) {
        return curioSlots.isEmpty() || curioSlots.contains(slot.toLowerCase(Locale.ROOT));
    }

    public static boolean dimensionAllowed(String dimension) {
        return dimensions.contains(dimension) == (dimensionMode == ListMode.WHITELIST);
    }

    public static boolean lootTableAllowed(String table) {
        return lootChance > 0.0 && lootTables.stream().anyMatch(entry -> tableMatches(entry, table));
    }

    private static boolean tableMatches(String entry, String table) {
        if (entry.startsWith("@")) {
            return table.startsWith(entry.substring(1) + ":");
        }
        if (entry.endsWith("*")) {
            return table.startsWith(entry.substring(0, entry.length() - 1));
        }
        return entry.equals(table);
    }

    public static boolean hasSpawnerOverrides() {
        return spawnCount >= 0 || maxNearbyEntities >= 0 || requiredPlayerRange >= 0;
    }
}
