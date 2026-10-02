package com.sxilverr.spawnercurio.item;

import com.sxilverr.spawnercurio.config.SpawnerCurioConfig;
import com.sxilverr.spawnercurio.core.NecklaceData;
import com.sxilverr.spawnercurio.core.Progression;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Locale;

public class SpawnerNecklaceItem extends Item {

    public final NecklaceTier tier;

    public SpawnerNecklaceItem(NecklaceTier tier, Properties properties) {
        super(properties);
        this.tier = tier;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!SpawnerCurioConfig.allowToggle) {
            return InteractionResultHolder.pass(stack);
        }
        if (!level.isClientSide) {
            boolean on = !NecklaceData.isOn(stack);
            NecklaceData.setOn(stack, on);
            player.displayClientMessage(
                    Component.translatable(on ? "message.spawnercurio.on" : "message.spawnercurio.off", stack.getHoverName()), true);
            SoundEvent sound = on ? SoundEvents.BEACON_ACTIVATE : SoundEvents.BEACON_DEACTIVATE;
            level.playSound(null, player.blockPosition(), sound, SoundSource.PLAYERS, 0.5F, 1.0F);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return SpawnerCurioConfig.glowWhenOn && NecklaceData.isOn(stack);
    }

    //? if <1.20.5 {
    @Override
    public boolean isFireResistant() {
        return SpawnerCurioConfig.lavaResistant;
    }
    //?}

    //? if <1.20.5 {
    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
    //?} else {
    /*@Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
    *///?}
        if (!SpawnerCurioConfig.showTooltip) {
            return;
        }
        boolean on = NecklaceData.isOn(stack);
        int level1 = Progression.level(stack);
        tooltip.add(Component.translatable(on ? "tooltip.spawnercurio.on" : "tooltip.spawnercurio.off")
                .withStyle(on ? ChatFormatting.GREEN : ChatFormatting.RED));
        tooltip.add(Component.translatable("tooltip.spawnercurio.speed",
                round(SpawnerCurioConfig.speedFor(level1, tier.speed))).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltip.spawnercurio.range",
                round(SpawnerCurioConfig.radiusFor(level1, tier.range))).withStyle(ChatFormatting.GRAY));
        if (SpawnerCurioConfig.progressionEnabled && tier.progresses()) {
            tooltip.add(Component.translatable("tooltip.spawnercurio.level",
                    level1, SpawnerCurioConfig.maxLevel).withStyle(ChatFormatting.GRAY));
            if (SpawnerCurioConfig.progressionFromKills) {
                tooltip.add(Component.translatable("tooltip.spawnercurio.kills",
                        NecklaceData.kills(stack)).withStyle(ChatFormatting.DARK_GRAY));
            }
            if (SpawnerCurioConfig.progressionFromXp) {
                tooltip.add(Component.translatable("tooltip.spawnercurio.xp",
                        NecklaceData.xp(stack)).withStyle(ChatFormatting.DARK_GRAY));
            }
            if (SpawnerCurioConfig.progressionFromSpawners) {
                tooltip.add(Component.translatable("tooltip.spawnercurio.spawners",
                        NecklaceData.spawners(stack)).withStyle(ChatFormatting.DARK_GRAY));
            }
        }
        if (SpawnerCurioConfig.allowToggle) {
            tooltip.add(Component.translatable("tooltip.spawnercurio.toggle").withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    private static String round(double value) {
        return String.format(Locale.ROOT, "%.2f", value).replaceAll("0+$", "").replaceAll("\\.$", "");
    }
}
