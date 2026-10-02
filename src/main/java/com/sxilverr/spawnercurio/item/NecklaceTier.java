package com.sxilverr.spawnercurio.item;

import net.minecraft.world.item.Item;

import java.util.Arrays;
import java.util.function.Supplier;

public enum NecklaceTier {
    SPAWNER("spawner_necklace", 2.0, 16.0, true, false, true),
    IRON("iron_spawner_necklace", 1.5, 12.0, false, true, false),
    GOLD("gold_spawner_necklace", 2.0, 16.0, false, true, false),
    DIAMOND("diamond_spawner_necklace", 5.0, 24.0, false, true, false),
    NETHERITE("netherite_spawner_necklace", 10.0, 32.0, false, true, false);

    public final String id;
    public boolean enabled;
    public double speed;
    public double range;
    public boolean craftable;
    public boolean inLoot;
    public Supplier<? extends Item> item;

    NecklaceTier(String id, double speed, double range, boolean enabled, boolean craftable, boolean inLoot) {
        this.id = id;
        this.speed = speed;
        this.range = range;
        this.enabled = enabled;
        this.craftable = craftable;
        this.inLoot = inLoot;
    }

    public boolean progresses() {
        return this == SPAWNER;
    }

    public static NecklaceTier byId(String id) {
        return Arrays.stream(values()).filter(tier -> tier.id.equals(id)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown necklace: " + id));
    }
}
