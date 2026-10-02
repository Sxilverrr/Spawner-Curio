package com.sxilverr.spawnercurio.forge;

import com.sxilverr.spawnercurio.core.LootInjection;
import com.sxilverr.spawnercurio.core.Progression;
import com.sxilverr.spawnercurio.core.SpawnerBoost;
import com.sxilverr.spawnercurio.item.NecklaceTier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class ForgeEvents {

    @SubscribeEvent
    public void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.level instanceof ServerLevel) {
            SpawnerBoost.tick((ServerLevel) event.level);
        }
    }

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof Player) {
            Progression.onKill((Player) event.getSource().getEntity());
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
        if (event.getLevel() instanceof ServerLevel && event.getChunk() instanceof LevelChunk) {
            SpawnerBoost.onChunkUnload((ServerLevel) event.getLevel(), event.getChunk().getPos());
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
