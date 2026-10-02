package com.sxilverr.spawnercurio.core;

import com.sxilverr.spawnercurio.config.SpawnerCurioConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
//? if >=1.21.6 {
/*import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.TagValueInput;
*///?}
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public final class SpawnerBoost {

    private static final String DELAY = "Delay";
    private static final String SPAWN_COUNT = "SpawnCount";
    private static final String MAX_NEARBY = "MaxNearbyEntities";
    private static final String PLAYER_RANGE = "RequiredPlayerRange";

    private static final Map<ResourceKey<Level>, Map<BlockPos, int[]>> ORIGINALS = new HashMap<>();

    private SpawnerBoost() {
    }

    public static void tick(ServerLevel level) {
        if (level.getGameTime() % SpawnerCurioConfig.checkInterval != 0) {
            return;
        }
        Set<BlockPos> boosted = new HashSet<>();
        //? if >=1.21.11 {
        /*String dimension = level.dimension().identifier().toString();
        *///?} else {
        String dimension = level.dimension().location().toString();
        //?}
        if (SpawnerCurioConfig.dimensionAllowed(dimension)) {
            for (ServerPlayer player : level.players()) {
                boostFor(level, player, boosted);
            }
        }
        restoreWhere(level, pos -> !boosted.contains(pos));
    }

    public static void onChunkUnload(ServerLevel level, ChunkPos chunk) {
        restoreWhere(level, pos -> new ChunkPos(pos).equals(chunk));
    }

    public static void restoreAll(MinecraftServer server) {
        for (ServerLevel level : server.getAllLevels()) {
            restoreWhere(level, pos -> true);
        }
        ORIGINALS.clear();
    }

    private static void restoreWhere(ServerLevel level, Predicate<BlockPos> which) {
        Map<BlockPos, int[]> originals = ORIGINALS.get(level.dimension());
        if (originals == null) {
            return;
        }
        originals.entrySet().removeIf(entry -> {
            if (!which.test(entry.getKey())) {
                return false;
            }
            restore(level, entry.getKey(), entry.getValue());
            return true;
        });
    }

    private static void boostFor(ServerLevel level, ServerPlayer player, Set<BlockPos> boosted) {
        ItemStack best = NecklaceData.active(player).stream()
                .max(Comparator.comparingDouble(Progression::speed)).orElse(null);
        if (best == null) {
            return;
        }
        int extra = (int) Math.round(SpawnerCurioConfig.checkInterval * (Progression.speed(best) - 1.0));
        boolean overrides = SpawnerCurioConfig.hasSpawnerOverrides();
        if (extra <= 0 && !overrides) {
            return;
        }
        for (SpawnerBlockEntity spawner : nearbySpawners(level, player, Progression.radius(best))) {
            apply(level, spawner, extra, overrides, boosted);
        }
    }

    private static List<SpawnerBlockEntity> nearbySpawners(ServerLevel level, ServerPlayer player, double radius) {
        List<SpawnerBlockEntity> found = new ArrayList<>();
        double limit = radius * radius;
        int chunkRadius = Mth.ceil(radius / 16.0);
        ChunkPos center = player.chunkPosition();
        for (int x = center.x - chunkRadius; x <= center.x + chunkRadius; x++) {
            for (int z = center.z - chunkRadius; z <= center.z + chunkRadius; z++) {
                if (!level.hasChunk(x, z)) {
                    continue;
                }
                for (BlockEntity blockEntity : level.getChunk(x, z).getBlockEntities().values()) {
                    if (blockEntity instanceof SpawnerBlockEntity spawner && distanceSqr(player, spawner) <= limit) {
                        found.add(spawner);
                    }
                }
            }
        }
        if (found.size() > SpawnerCurioConfig.maxSpawners) {
            found.sort(Comparator.comparingDouble(spawner -> distanceSqr(player, spawner)));
            return found.subList(0, SpawnerCurioConfig.maxSpawners);
        }
        return found;
    }

    private static double distanceSqr(ServerPlayer player, SpawnerBlockEntity spawner) {
        return player.distanceToSqr(Vec3.atCenterOf(spawner.getBlockPos()));
    }

    private static void apply(ServerLevel level, SpawnerBlockEntity spawner, int extra, boolean overrides,
                              Set<BlockPos> boosted) {
        CompoundTag tag = saveSpawner(level, spawner);
        String mob = Nbt.getString(Nbt.getCompound(Nbt.getCompound(tag, "SpawnData"), "entity"), "id");
        if (SpawnerCurioConfig.blockedMobs.contains(mob)) {
            return;
        }
        boolean changed = false;
        short delay = Nbt.getShort(tag, DELAY);
        if (extra > 0 && delay > 0) {
            tag.putShort(DELAY, (short) Math.max(0, delay - extra));
            changed = true;
        }
        if (overrides) {
            BlockPos pos = spawner.getBlockPos().immutable();
            ORIGINALS.computeIfAbsent(level.dimension(), key -> new HashMap<>()).putIfAbsent(pos, new int[] {
                    Nbt.getShort(tag, SPAWN_COUNT), Nbt.getShort(tag, MAX_NEARBY), Nbt.getShort(tag, PLAYER_RANGE) });
            changed |= override(tag, SPAWN_COUNT, SpawnerCurioConfig.spawnCount);
            changed |= override(tag, MAX_NEARBY, SpawnerCurioConfig.maxNearbyEntities);
            changed |= override(tag, PLAYER_RANGE, SpawnerCurioConfig.requiredPlayerRange);
            boosted.add(pos);
        }
        if (changed) {
            loadSpawner(level, spawner, tag);
            spawner.setChanged();
        }
    }

    private static boolean override(CompoundTag tag, String key, int value) {
        if (value < 0 || Nbt.getShort(tag, key) == (short) value) {
            return false;
        }
        tag.putShort(key, (short) value);
        return true;
    }

    private static void restore(ServerLevel level, BlockPos pos, int[] original) {
        if (!level.isLoaded(pos) || !(level.getBlockEntity(pos) instanceof SpawnerBlockEntity spawner)) {
            return;
        }
        CompoundTag tag = saveSpawner(level, spawner);
        tag.putShort(SPAWN_COUNT, (short) original[0]);
        tag.putShort(MAX_NEARBY, (short) original[1]);
        tag.putShort(PLAYER_RANGE, (short) original[2]);
        loadSpawner(level, spawner, tag);
        spawner.setChanged();
    }

    private static CompoundTag saveSpawner(ServerLevel level, SpawnerBlockEntity spawner) {
        //? if <1.20.5 {
        return spawner.saveWithoutMetadata();
        //?} else {
        /*return spawner.saveWithoutMetadata(level.registryAccess());
        *///?}
    }

    private static void loadSpawner(ServerLevel level, SpawnerBlockEntity spawner, CompoundTag tag) {
        //? if <1.20.5 {
        spawner.load(tag);
        //?} else if <1.21.6 {
        /*spawner.loadWithComponents(tag, level.registryAccess());
        *///?} else {
        /*spawner.loadWithComponents(TagValueInput.create(ProblemReporter.DISCARDING, level.registryAccess(), tag));
        *///?}
    }
}
