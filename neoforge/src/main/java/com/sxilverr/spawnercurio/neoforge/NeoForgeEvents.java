package com.sxilverr.spawnercurio.neoforge;

import com.sxilverr.spawnercurio.core.LootInjection;
import com.sxilverr.spawnercurio.core.Progression;
import com.sxilverr.spawnercurio.core.SpawnerBoost;
import com.sxilverr.spawnercurio.item.NecklaceTier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.storage.loot.LootPool;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

public final class NeoForgeEvents {

    @SubscribeEvent
    public void onLevelTick(LevelTickEvent.Post event) {
        if (event.getLevel() instanceof ServerLevel level) {
            SpawnerBoost.tick(level);
        }
    }

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            Progression.onKill(player);
        }
    }

    @SubscribeEvent
    public void onXpChange(PlayerXpEvent.XpChange event) {
        event.setAmount(Progression.absorbXp(event.getEntity(), event.getAmount()));
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getState().is(Blocks.SPAWNER)) {
            Progression.onSpawnerBroken(event.getPlayer());
        }
    }

    @SubscribeEvent
    public void onChunkUnload(ChunkEvent.Unload event) {
        if (event.getLevel() instanceof ServerLevel level && event.getChunk() instanceof LevelChunk) {
            SpawnerBoost.onChunkUnload(level, event.getChunk().getPos());
        }
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        SpawnerBoost.restoreAll(event.getServer());
    }

    @SubscribeEvent
    public void onLootTableLoad(LootTableLoadEvent event) {
        for (NecklaceTier tier : NecklaceTier.values()) {
            LootPool pool = LootInjection.poolFor(event.getName().toString(), tier);
            if (pool != null) {
                event.getTable().addPool(pool);
            }
        }
    }
}
