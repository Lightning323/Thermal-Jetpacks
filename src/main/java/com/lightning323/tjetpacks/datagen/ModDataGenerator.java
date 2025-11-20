package com.lightning323.tjetpacks.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.lightning323.tjetpacks.ThermalJetpacks.MOD_ID;


@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModDataGenerator {

    private ModDataGenerator() {
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        // Required for 1.20 data-gen
        BlockTagsProvider dummyProvider = new BlockTagsProvider(gen.getPackOutput(), event.getLookupProvider(), MOD_ID, existingFileHelper) {
            @Override
            protected void addTags(HolderLookup.Provider provider) {}
        };

        // TODO: test the boolean
        gen.addProvider(true, new ModItemModelProvider(gen, existingFileHelper));
        gen.addProvider(true, dummyProvider);
//        gen.addProvider(true, new ModItemTagsProvider(gen.getPackOutput(), event.getLookupProvider(), dummyProvider.contentsGetter(), event.getExistingFileHelper()));
//        gen.addProvider(true, new ModRecipeProvider(gen));
        gen.addProvider(true, new ModAdvancementProviderAdapter(gen.getPackOutput(), event.getLookupProvider()));

        DatapackBuiltinEntriesProvider modRegistryProvider = new ModRegistryProvider(gen.getPackOutput(), event.getLookupProvider());
        gen.addProvider(true, modRegistryProvider);
    }
}