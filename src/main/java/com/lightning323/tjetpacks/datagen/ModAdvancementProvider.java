package com.lightning323.tjetpacks.datagen;

import com.lightning323.tjetpacks.RegistryHandler;
import com.lightning323.tjetpacks.item.JetpackItem;
import com.lightning323.tjetpacks.item.JetpackType;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;

import static com.lightning323.tjetpacks.ThermalJetpacks.MOD_ID;


public class ModAdvancementProvider implements AdvancementSubProvider {

    @Override
    public void generate(HolderLookup.Provider provider, Consumer<Advancement> consumer) {
        Advancement root = Advancement.Builder.advancement().display(
                new DisplayInfo(
                        new ItemStack(RegistryHandler.JETPACK_CREATIVE_ARMORED.get()),
                        Component.translatable("advancement.tjetpacks.root.title"),
                        Component.translatable("advancement.tjetpacks.root.description"),
                        new ResourceLocation("minecraft", "textures/gui/advancements/backgrounds/stone.png"),
                        FrameType.TASK,
                        true,
                        false,
                        false
                ))
                .requirements(RequirementsStrategy.OR)
                .addCriterion("any",
                        PlayerTrigger.TriggerInstance.located(
                                EntityPredicate.Builder.entity().build()))
                .save(consumer, MOD_ID + ":root");

        Advancement lastRoot = null;
        JetpackType lastJetpackType = null;
        for (JetpackType type : JetpackType.values()) {
            if (!type.isNoAdvancements() && !type.isArmored()) {
                RegistryObject<Item> currentJetpack = null;
                for (RegistryObject<Item> item : RegistryHandler.ITEMS.getEntries()) {
                    if (item.get() instanceof JetpackItem jetpackItem && jetpackItem.getJetpackType() == type) {
                        currentJetpack = item;
                        break;
                    }
                }
                if (currentJetpack != null) {
                    if (lastJetpackType == null || lastJetpackType.getTier() > type.getTier()) {
                        lastRoot = generateJetpack(root, (JetpackItem) currentJetpack.get(), currentJetpack.getId(), consumer);
                    } else {
                        lastRoot = generateJetpack(lastRoot, (JetpackItem) currentJetpack.get(), currentJetpack.getId(), consumer);
                    }
                    lastJetpackType = type;
                }
            }
        }
    }

    private Advancement generateJetpack(Advancement parent, JetpackItem displayItem, ResourceLocation namespace, Consumer<Advancement> consumer) {
        String advancementPath = "advancement." + namespace.getNamespace() + "." + namespace.getPath();
        return Advancement.Builder.advancement().parent(parent).display(new DisplayInfo(
                new ItemStack(displayItem),
                Component.translatable(advancementPath + ".title"),
                Component.translatable(advancementPath + ".description"),
                null,
                FrameType.TASK,
                true,
                true,
                false

        )).requirements(RequirementsStrategy.OR)
                .addCriterion("has_jetpack",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(displayItem).build()))
                .save(consumer, namespace.getNamespace() + ":" + namespace.getNamespace() + "/" + namespace.getPath());
    }
}
