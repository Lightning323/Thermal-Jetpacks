package com.lightning323.tjetpacks.datagen;//package jetpacks.datagen;
//
//import jetpacks.handlers.RegistryHandler;
//import net.minecraft.advancements.Advancement;
//import net.minecraft.advancements.CriterionTriggerInstance;
//import net.minecraft.advancements.FrameType;
//import net.minecraft.advancements.critereon.InventoryChangeTrigger;
//import net.minecraft.advancements.critereon.LocationPredicate;
//import net.minecraft.advancements.critereon.PlayerTrigger;
//import net.minecraft.core.registries.BuiltInRegistries;
//import net.minecraft.network.chat.Component;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.level.ItemLike;
//import net.minecraft.world.level.block.Blocks;
//
//import javax.annotation.Nullable;
//import java.util.function.Consumer;
//
//public class ModAdvancements implements Consumer<Consumer<Advancement>> {
//
//    // showToast, announceChat, hidden
//
//    public ModAdvancements() {
//    }
//
//    @Override
//    public void accept(Consumer<Advancement> consumer) {
//        Advancement root = rootAdvancement(consumer, RegistryHandler.JETPACK_CREATIVE.get(), "crafting_table", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.CRAFTING_TABLE));

//        Advancement te1 = jetpackAdvancement(consumer, root, RegistryHandler.JETPACK_TE1.get());
//        Advancement te2 = jetpackAdvancement(consumer, te1, RegistryHandler.JETPACK_TE2.get());
//        Advancement te3 = jetpackAdvancement(consumer, te2, RegistryHandler.JETPACK_TE3.get());
//        Advancement te4 = jetpackAdvancement(consumer, te3, RegistryHandler.JETPACK_TE4.get());
//        Advancement te5 = jetpackAdvancement(consumer, te4, RegistryHandler.JETPACK_TE5.get());
//        Advancement te5_armored = jetpackAdvancement(consumer, te5, RegistryHandler.JETPACK_TE5_ARMORED.get());
//    }
//
//    private Advancement jetpackAdvancement(Consumer<Advancement> consumer, Advancement parent, ItemLike item) {
//        // TODO: test this
////        String name = item.asItem().getRegistryName().getPath();
//        String name = BuiltInRegistries.ITEM.getKey(item.asItem()).getPath();
//        return Advancement.Builder.advancement().parent(parent)
//                .display(item,
//                        Component.translatable("advancement."+MOD_ID+"." + name + ".title"),
//                        Component.translatable("advancement."+MOD_ID+"." + name + ".description"),
//                        null, FrameType.TASK, true, true, false
//                )
//                .addCriterion("has_jetpack", InventoryChangeTrigger.TriggerInstance.hasItems(item))
//                .save(consumer, new ResourceLocation(MOD_ID, (""+MOD_ID+"" == null ? "" : ""+MOD_ID+"" + "/") + name).toString());
//    }
//
//    private Advancement advancement(Consumer<Advancement> consumer, Advancement parent, ItemLike item, String path, FrameType frame, String criterionId, @Nullable CriterionTriggerInstance criterion) {
////        String name = item.asItem().getRegistryName().getPath();
//        String name = BuiltInRegistries.ITEM.getKey(item.asItem()).getPath();
//        return Advancement.Builder.advancement().parent(parent)
//                .display(item,
//                        Component.translatable("advancement."+MOD_ID+"." + name + ".title"),
//                        Component.translatable("advancement."+MOD_ID+"." + name + ".description"),
//                        null, frame, true, true, false
//                )
//                .addCriterion(criterionId, criterion == null ? InventoryChangeTrigger.TriggerInstance.hasItems(item) : criterion)
//                .save(consumer, new ResourceLocation(MOD_ID, (path == null ? "" : path + "/") + name).toString());
//    }
//
//    private Advancement rootAdvancement(Consumer<Advancement> consumer, ItemLike item, String criterionId, CriterionTriggerInstance criterion) {
//        return Advancement.Builder.advancement()
//                .display(item,
//                        Component.translatable("advancement."+MOD_ID+"." + "root" + ".title"),
//                        Component.translatable("advancement."+MOD_ID+"." + "root" + ".description"),
//                        new ResourceLocation("textures/gui/advancements/backgrounds/" + "stone" + ".png"),
//                        FrameType.TASK, true, false, false)
//                .addCriterion("any", PlayerTrigger.TriggerInstance.located(LocationPredicate.ANY))
//                //.addCriterion(criterionId, criterion)
//                .save(consumer, new ResourceLocation(MOD_ID, "root").toString());
//    }
//}
