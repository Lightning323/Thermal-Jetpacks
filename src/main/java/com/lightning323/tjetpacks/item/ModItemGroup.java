package com.lightning323.tjetpacks.item;

import com.lightning323.tjetpacks.RegistryHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.registries.RegistryObject;

import static com.lightning323.tjetpacks.ThermalJetpacks.MOD_ID;

public class ModItemGroup extends CreativeModeTab {

    public ModItemGroup() {
        super(CreativeModeTab.builder()
                .title(Component.translatable("itemGroup." + MOD_ID + ".main"))
                .icon(() -> new ItemStack(RegistryHandler.JETPACK_CREATIVE_ARMORED.get()))
                // I have *zero* clue why this is required for this mod _exclusively_ but I don't care enough to figure out, why.
                .withBackgroundLocation(new ResourceLocation("minecraft", "textures/gui/container/creative_inventory/tab_items.png"))
                .displayItems((params, output) -> {
                    for (RegistryObject<Item> i : RegistryHandler.ITEMS.getEntries()) {
                        Item item = i.get();
                        output.accept(new ItemStack(item));
                        if (item instanceof JetpackItem jetpackItem) {
                            output.accept(jetpackItem.asChargedCopy());
                        }
                    }
                }));
    }

    public static void registerItems(BuildCreativeModeTabContentsEvent event) {
        for (RegistryObject<Item> i : RegistryHandler.ITEMS.getEntries()) {
            Item item = i.get();
            event.accept(new ItemStack(item));
            if (item instanceof JetpackItem jetpackItem) {
                event.accept(jetpackItem.asChargedCopy());
            }
        }
    }
}
