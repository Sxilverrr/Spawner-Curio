package com.sxilverr.spawnercurio.core;

import net.minecraft.nbt.CompoundTag;

public final class Nbt {

    private Nbt() {
    }

    public static int getInt(CompoundTag tag, String key) {
        //? if >=1.21.5 {
        /*return tag.getIntOr(key, 0);
        *///?} else {
        return tag.getInt(key);
        //?}
    }

    public static short getShort(CompoundTag tag, String key) {
        //? if >=1.21.5 {
        /*return tag.getShortOr(key, (short) 0);
        *///?} else {
        return tag.getShort(key);
        //?}
    }

    public static boolean getBoolean(CompoundTag tag, String key, boolean fallback) {
        //? if >=1.21.5 {
        /*return tag.getBooleanOr(key, fallback);
        *///?} else {
        return tag.contains(key) ? tag.getBoolean(key) : fallback;
        //?}
    }

    public static String getString(CompoundTag tag, String key) {
        //? if >=1.21.5 {
        /*return tag.getStringOr(key, "");
        *///?} else {
        return tag.getString(key);
        //?}
    }

    public static CompoundTag getCompound(CompoundTag tag, String key) {
        //? if >=1.21.5 {
        /*return tag.getCompoundOrEmpty(key);
        *///?} else {
        return tag.getCompound(key);
        //?}
    }
}
