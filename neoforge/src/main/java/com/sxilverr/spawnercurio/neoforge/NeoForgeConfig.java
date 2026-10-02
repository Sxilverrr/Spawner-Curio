package com.sxilverr.spawnercurio.neoforge;

import com.sxilverr.spawnercurio.SpawnerCurio;
import com.sxilverr.spawnercurio.item.NecklaceTier;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import static com.sxilverr.spawnercurio.config.SpawnerCurioConfig.*;

@EventBusSubscriber(modid = SpawnerCurio.MOD_ID)
public final class NeoForgeConfig {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    private static final List<Runnable> APPLY = new ArrayList<>();
    private static final ModConfigSpec SPEC;

    static {
        BUILDER.comment("Spawner boost.").push("general");
        bind(BUILDER.comment("Ticks between spawner checks. 20 is one second.")
                .defineInRange("check_interval", checkInterval, 1, 200), v -> checkInterval = v);
        bind(BUILDER.comment("Max spawners boosted per player. Closest first.")
                .defineInRange("max_spawners", maxSpawners, 1, 256), v -> maxSpawners = v);
        BUILDER.pop();

        BUILDER.comment("Where the necklace works.").push("slots");
        bind(BUILDER.comment("Works in a curio slot.")
                .define("works_in_curio_slot", worksInCurioSlot), v -> worksInCurioSlot = v);
        bind(BUILDER.comment("Works when held in either hand.")
                .define("works_in_hand", worksInHand), v -> worksInHand = v);
        bind(BUILDER.comment("Works anywhere in the inventory, hands included.")
                .define("works_in_inventory", worksInInventory), v -> worksInInventory = v);
        list("curio_slots", curioSlots, v -> curioSlots = v,
                "Curio slots it works in. Empty allows all.");
        BUILDER.pop();

        BUILDER.comment("On and off toggle.").push("toggle");
        bind(BUILDER.comment("Right-click to turn it on or off.")
                .define("allow_toggle", allowToggle), v -> allowToggle = v);
        bind(BUILDER.comment("New necklaces start on.")
                .define("default_on", defaultOn), v -> defaultOn = v);
        BUILDER.pop();

        BUILDER.comment("Item settings.").push("item");
        bind(BUILDER.comment("Survives fire and lava.")
                .define("lava_resistant", lavaResistant), v -> lavaResistant = v);
        bind(BUILDER.comment("Enchantment glint while on.")
                .define("glow_when_on", glowWhenOn), v -> glowWhenOn = v);
        bind(BUILDER.comment("Show stats in the tooltip.")
                .define("show_tooltip", showTooltip), v -> showTooltip = v);
        BUILDER.pop();

        BUILDER.comment("The Spawner Necklace starts weak and levels up. Tiered necklaces are always at full power.").push("progression");
        bind(BUILDER.comment("Turn progression on. When off, it is always at full power.")
                .define("enabled", progressionEnabled), v -> progressionEnabled = v);
        bind(BUILDER.comment("Mob kills add progress.")
                .define("from_kills", progressionFromKills), v -> progressionFromKills = v);
        bind(BUILDER.comment("Breaking spawners adds progress.")
                .define("from_spawners", progressionFromSpawners), v -> progressionFromSpawners = v);
        bind(BUILDER.comment("Absorbs part of the XP you pick up.")
                .define("from_xp", progressionFromXp), v -> progressionFromXp = v);
        bind(BUILDER.comment("Kills per level.")
                .defineInRange("kills_per_level", killsPerLevel, 1, 100000), v -> killsPerLevel = v);
        bind(BUILDER.comment("Spawners broken per level.")
                .defineInRange("spawners_per_level", spawnersPerLevel, 1, 100000), v -> spawnersPerLevel = v);
        bind(BUILDER.comment("XP per level.")
                .defineInRange("xp_per_level", xpPerLevel, 1, 1000000), v -> xpPerLevel = v);
        bind(BUILDER.comment("Share of each XP pickup it takes. 0.5 is half. Stops at max level.")
                .defineInRange("xp_share", xpShare, 0.0, 1.0), v -> xpShare = v);
        bind(BUILDER.comment("Max level.")
                .defineInRange("max_level", maxLevel, 1, 1000), v -> maxLevel = v);
        bind(BUILDER.comment("Speed at level 0. Grows to the Spawner Necklace's speed_multiplier.")
                .defineInRange("start_speed", startSpeed, 1.0, 100.0), v -> startSpeed = v);
        bind(BUILDER.comment("Range at level 0. Grows to the Spawner Necklace's range.")
                .defineInRange("start_radius", startRadius, 1.0, 128.0), v -> startRadius = v);
        BUILDER.pop();

        BUILDER.comment("Overrides for boosted spawners. Reset when the necklace leaves.").push("spawner");
        bind(BUILDER.comment("Mobs per spawn. -1 for no change.")
                .defineInRange("spawn_count", spawnCount, -1, 128), v -> spawnCount = v);
        bind(BUILDER.comment("Mob cap around the spawner. -1 for no change.")
                .defineInRange("max_nearby_entities", maxNearbyEntities, -1, 256), v -> maxNearbyEntities = v);
        bind(BUILDER.comment("Player range a spawner needs to run. -1 for no change.")
                .defineInRange("required_player_range", requiredPlayerRange, -1, 128), v -> requiredPlayerRange = v);
        BUILDER.pop();

        BUILDER.comment("Dimension and mob filters.").push("filters");
        bind(BUILDER.comment("BLACKLIST blocks the listed dimensions. WHITELIST allows only them.")
                .defineEnum("dimension_mode", dimensionMode), v -> dimensionMode = v);
        list("dimensions", dimensions, v -> dimensions = v,
                "Dimension ids, like minecraft:the_nether.");
        list("blocked_mobs", blockedMobs, v -> blockedMobs = v,
                "Spawners of these mobs are never boosted, like minecraft:blaze.");
        BUILDER.pop();

        BUILDER.comment("Chest loot.").push("loot");
        bind(BUILDER.comment("Chance per chest. 0.05 is 5%.")
                .defineInRange("chance", lootChance, 0.0, 1.0), v -> lootChance = v);
        list("tables", lootTables, v -> lootTables = v,
                "Loot tables it can appear in. Use a full id, a prefix with * like minecraft:chests/*, or @modid.");
        BUILDER.pop();

        for (NecklaceTier tier : NecklaceTier.values()) {
            BUILDER.push(tier.id);
            bind(BUILDER.comment("Turn the item on. When off, it does nothing and is hidden from the creative tab.")
                    .define("enabled", tier.enabled), v -> tier.enabled = v);
            bind(BUILDER.comment(tier.progresses()
                            ? "Spawner speed. Max-level speed when progression is on."
                            : "Spawner speed.")
                    .defineInRange("speed_multiplier", tier.speed, 1.0, 100.0), v -> tier.speed = v);
            bind(BUILDER.comment(tier.progresses()
                            ? "Range in blocks. Max-level range when progression is on."
                            : "Range in blocks.")
                    .defineInRange("range", tier.range, 1.0, 128.0), v -> tier.range = v);
            bind(BUILDER.comment("Allow crafting it. /reload after changes.")
                    .define("craftable", tier.craftable), v -> tier.craftable = v);
            bind(BUILDER.comment("Can show up in the loot tables listed under loot.")
                    .define("in_loot", tier.inLoot), v -> tier.inLoot = v);
            BUILDER.pop();
        }

        SPEC = BUILDER.build();
    }

    private NeoForgeConfig() {
    }

    private static <T> void bind(ModConfigSpec.ConfigValue<T> value, Consumer<T> set) {
        APPLY.add(() -> set.accept(value.get()));
    }

    private static void list(String key, List<String> defaults, Consumer<List<String>> set, String... comment) {
        bind(BUILDER.comment(comment).defineListAllowEmpty(key, () -> defaults, () -> "", o -> o instanceof String),
                v -> set.accept(v.stream().map(s -> s.toLowerCase(Locale.ROOT)).toList()));
    }

    public static void register(ModContainer container) {
        container.registerConfig(ModConfig.Type.COMMON, SPEC);
    }

    @SubscribeEvent
    public static void onLoad(ModConfigEvent.Loading event) {
        APPLY.forEach(Runnable::run);
    }

    @SubscribeEvent
    public static void onReload(ModConfigEvent.Reloading event) {
        APPLY.forEach(Runnable::run);
    }
}
