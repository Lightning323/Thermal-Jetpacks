package com.lightning323.tjetpacks.datagen;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import static com.lightning323.tjetpacks.ThermalJetpacks.MOD_ID;

public class ModTags {

    public static final TagKey<Item> PARTICLES = mod("particles");
    public static final TagKey<Item> JETPACK = mod("jetpack");

    public static final TagKey<Item> CURIOS_HEAD = curios("head");
    public static final TagKey<Item> CURIOS_JETPACK = curios("jetpack");

    private static TagKey<Item> forge(String path) {
        return ItemTags.create(new ResourceLocation("forge", path));
    }
    private static TagKey<Item> mod(String path) {
        return TagKey.create(Registries.ITEM, new ResourceLocation(MOD_ID, path));
    }
    private static TagKey<Item> curios(String path) {
        return TagKey.create(Registries.ITEM, new ResourceLocation("curios", path));
    }
}
