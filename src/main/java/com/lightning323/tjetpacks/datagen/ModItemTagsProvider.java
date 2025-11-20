package com.lightning323.tjetpacks.datagen;


import com.lightning323.tjetpacks.RegistryHandler;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

import static com.lightning323.tjetpacks.ThermalJetpacks.MOD_ID;


public class ModItemTagsProvider extends ItemTagsProvider {

    public ModItemTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> blockTagProvider,
                               CompletableFuture<TagLookup<Block>> tagLookup,
                               @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, blockTagProvider, tagLookup, MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModTags.JETPACK)
                .add(RegistryHandler.JETPACK_CREATIVE_ARMORED.get())
                .add(RegistryHandler.JETPACK_TE1.get())
                .add(RegistryHandler.JETPACK_TE2.get())
                .add(RegistryHandler.JETPACK_TE3.get())
                .add(RegistryHandler.JETPACK_TE4.get())
                .add(RegistryHandler.JETPACK_TE5_ARMORED.get())
        ;

        // Curios:
        tag(ModTags.CURIOS_HEAD)
                .add(RegistryHandler.PILOT_GOGGLES_GOLD.get())
                .add(RegistryHandler.PILOT_GOGGLES_IRON.get())
        ;
        tag(ModTags.CURIOS_JETPACK)
                .add(RegistryHandler.JETPACK_CREATIVE_ARMORED.get())
                .add(RegistryHandler.JETPACK_TE1.get())
                .add(RegistryHandler.JETPACK_TE2.get())
                .add(RegistryHandler.JETPACK_TE3.get())
                .add(RegistryHandler.JETPACK_TE4.get())
                .add(RegistryHandler.JETPACK_TE5_ARMORED.get())
        ;
    }
}
