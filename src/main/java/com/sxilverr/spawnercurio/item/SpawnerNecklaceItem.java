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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
//? if >=1.21.2 {
/*import net.minecraft.world.InteractionResult;
*///?} else {
import net.minecraft.world.InteractionResultHolder;
//?}
//? if >=1.21.5 {
/*import net.minecraft.world.item.component.TooltipDisplay;
*///?}

import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class SpawnerNecklaceItem extends Item {

    public final NecklaceTier tier;

    public SpawnerNecklaceItem(NecklaceTier tier, Properties properties) {
        super(properties);
        this.tier = tier;
    }

    //? if >=1.21.2 {
    /*@Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!SpawnerCurioConfig.allowToggle) {
            return InteractionResult.PASS;
        }
        toggle(level, player, player.getItemInHand(hand));
        return InteractionResult.SUCCESS;
    }
    *///?} else {
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!SpawnerCurioConfig.allowToggle) {
            return InteractionResultHolder.pass(stack);
        }
        toggle(level, player, stack);
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
    //?}

    private static void toggle(Level level, Player player, ItemStack stack) {
        if (level.isClientSide()) {
            return;
        }
        boolean on = !NecklaceData.isOn(stack);
        NecklaceData.setOn(stack, on);
        player.displayClientMessage(
                Component.translatable(on ? "message.spawnercurio.on" : "message.spawnercurio.off", stack.getHoverName()), true);
        SoundEvent sound = on ? SoundEvents.BEACON_ACTIVATE : SoundEvents.BEACON_DEACTIVATE;
        level.playSound(null, player.blockPosition(), sound, SoundSource.PLAYERS, 0.5F, 1.0F);
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
        addTooltip(stack, tooltip::add);
    }
    //?} else if <1.21.5 {
    /*@Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        addTooltip(stack, tooltip::add);
    }
    *///?} else {
    /*@Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display,
                                Consumer<Component> tooltip, TooltipFlag flag) {
        addTooltip(stack, tooltip);
    }
    *///?}

    private void addTooltip(ItemStack stack, Consumer<Component> tooltip) {
        if (!SpawnerCurioConfig.showTooltip) {
            return;
        }
        boolean on = NecklaceData.isOn(stack);
        int level = Progression.level(stack);
        tooltip.accept(Component.translatable(on ? "tooltip.spawnercurio.on" : "tooltip.spawnercurio.off")
                .withStyle(on ? ChatFormatting.GREEN : ChatFormatting.RED));
        tooltip.accept(Component.translatable("tooltip.spawnercurio.speed",
                round(SpawnerCurioConfig.speedFor(level, tier.speed))).withStyle(ChatFormatting.GRAY));
        tooltip.accept(Component.translatable("tooltip.spawnercurio.range",
                round(SpawnerCurioConfig.radiusFor(level, tier.range))).withStyle(ChatFormatting.GRAY));
        if (SpawnerCurioConfig.progressionEnabled && tier.progresses()) {
            tooltip.accept(Component.translatable("tooltip.spawnercurio.level",
                    level, SpawnerCurioConfig.maxLevel).withStyle(ChatFormatting.GRAY));
            if (SpawnerCurioConfig.progressionFromKills) {
                tooltip.accept(Component.translatable("tooltip.spawnercurio.kills",
                        NecklaceData.kills(stack)).withStyle(ChatFormatting.DARK_GRAY));
            }
            if (SpawnerCurioConfig.progressionFromXp) {
                tooltip.accept(Component.translatable("tooltip.spawnercurio.xp",
                        NecklaceData.xp(stack)).withStyle(ChatFormatting.DARK_GRAY));
            }
            if (SpawnerCurioConfig.progressionFromSpawners) {
                tooltip.accept(Component.translatable("tooltip.spawnercurio.spawners",
                        NecklaceData.spawners(stack)).withStyle(ChatFormatting.DARK_GRAY));
            }
        }
        if (SpawnerCurioConfig.allowToggle) {
            tooltip.accept(Component.translatable("tooltip.spawnercurio.toggle").withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    private static String round(double value) {
        return String.format(Locale.ROOT, "%.2f", value).replaceAll("0+$", "").replaceAll("\\.$", "");
    }
}
