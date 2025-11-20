package com.lightning323.tjetpacks.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModAdvancementProviderAdapter extends AdvancementProvider {

    public ModAdvancementProviderAdapter(PackOutput out, CompletableFuture<HolderLookup.Provider> tagLookup) {
        super(out, tagLookup, List.of(new ModAdvancementProvider()));
    }
}