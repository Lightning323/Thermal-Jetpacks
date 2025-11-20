package com.lightning323.tjetpacks.integration;

import com.lightning323.tjetpacks.RegistryHandler;
import com.lightning323.tjetpacks.client.PilotGogglesRenderer;
import com.lightning323.tjetpacks.network.NetworkHandler;
import com.lightning323.tjetpacks.network.packets.PacketEnableJetpackHUD;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.type.capability.ICurio;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.lightning323.tjetpacks.ThermalJetpacks.MOD_ID;

public class CuriosIntegration {

    private static ResourceLocation getJetpackTexture(String name) {
        return new ResourceLocation(MOD_ID, "textures/models/armor/jetpack_" + name + ".png");
    }

    public static void initRenderers() {
        CuriosRendererRegistry.register(RegistryHandler.PILOT_GOGGLES_IRON.get(), () -> new PilotGogglesRenderer(new ResourceLocation(MOD_ID, "textures/models/armor/pilot_goggles_iron.png")));
        CuriosRendererRegistry.register(RegistryHandler.PILOT_GOGGLES_GOLD.get(), () -> new PilotGogglesRenderer(new ResourceLocation(MOD_ID, "textures/models/armor/pilot_goggles_gold.png")));
//        CuriosRendererRegistry.register(RegistryHandler.JETPACK_CREATIVE.get(), () -> new JetpackRenderer(getJetpackTexture("creative")));
        CuriosRendererRegistry.register(RegistryHandler.JETPACK_CREATIVE_ARMORED.get(), () -> new JetpackRenderer(getJetpackTexture("creative_armored")));
        CuriosRendererRegistry.register(RegistryHandler.JETPACK_TE1.get(), () -> new JetpackRenderer(getJetpackTexture("te1")));
//        CuriosRendererRegistry.register(RegistryHandler.JETPACK_TE1_ARMORED.get(), () -> new JetpackRenderer(getJetpackTexture("te1_armored")));
        CuriosRendererRegistry.register(RegistryHandler.JETPACK_TE2.get(), () -> new JetpackRenderer(getJetpackTexture("te2")));
//        CuriosRendererRegistry.register(RegistryHandler.JETPACK_TE2_ARMORED.get(), () -> new JetpackRenderer(getJetpackTexture("te2_armored")));
        CuriosRendererRegistry.register(RegistryHandler.JETPACK_TE3.get(), () -> new JetpackRenderer(getJetpackTexture("te3")));
//        CuriosRendererRegistry.register(RegistryHandler.JETPACK_TE3_ARMORED.get(), () -> new JetpackRenderer(getJetpackTexture("te3_armored")));
        CuriosRendererRegistry.register(RegistryHandler.JETPACK_TE4.get(), () -> new JetpackRenderer(getJetpackTexture("te4")));
//        CuriosRendererRegistry.register(RegistryHandler.JETPACK_TE4_ARMORED.get(), () -> new JetpackRenderer(getJetpackTexture("te4_armored")));
//        CuriosRendererRegistry.register(RegistryHandler.JETPACK_TE5.get(), () -> new JetpackRenderer(getJetpackTexture("te5")));
        CuriosRendererRegistry.register(RegistryHandler.JETPACK_TE5_ARMORED.get(), () -> new JetpackRenderer(getJetpackTexture("te5_enderium")));
    }

    public static ICapabilityProvider initGogglesCapabilities(ItemStack itemStack) {
        return getProvider(new ICurio() {

            @Override
            public void playRightClickEquipSound(LivingEntity livingEntity) {
                livingEntity.getCommandSenderWorld().playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
                        ((ArmorItem) itemStack.getItem()).getMaterial().getEquipSound(), SoundSource.PLAYERS, 1.0F, 1.0F
                );
            }

            @Override
            public ItemStack getStack() {
                return itemStack;

            }

            @Override
            public void onEquip(SlotContext slotContext, ItemStack prevStack) {
                if (slotContext.entity() instanceof ServerPlayer player)
                    NetworkHandler.sendToClient(new PacketEnableJetpackHUD(true), player);
                ICurio.super.onEquip(slotContext, prevStack);
            }

            @Override
            public void onUnequip(SlotContext slotContext, ItemStack newStack) {
                if (slotContext.entity() instanceof ServerPlayer player)
                    NetworkHandler.sendToClient(new PacketEnableJetpackHUD(false), player);
                ICurio.super.onUnequip(slotContext, newStack);
            }
        });
    }

    public static ICapabilityProvider initJetpackCapabilities(ItemStack itemStack) {
        return getProvider(new ICurio() {

            @Override
            public void playRightClickEquipSound(LivingEntity livingEntity) {
                livingEntity.getCommandSenderWorld().playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
                        ((ArmorItem) itemStack.getItem()).getMaterial().getEquipSound(), SoundSource.PLAYERS, 1.0F, 1.0F
                );
            }

            @Override
            public ItemStack getStack() {
                return itemStack;
            }

            @Override
            public boolean canRightClickEquip() {
                return true;
            }

//            @Override
//            public void curioTick(String identifier, int index, LivingEntity livingEntity) {
//                if (livingEntity instanceof Player) {
//                    itemStack.onArmorTick(livingEntity.getCommandSenderWorld(), (Player) livingEntity);
//                }
//            }

            @Override
            public boolean canSync(String identifier, int index, LivingEntity livingEntity) {
                return true;
            }
        });
    }

    private static ICapabilityProvider getProvider(ICurio curio) {
        return new ICapabilityProvider() {
            private final LazyOptional<ICurio> curioOptional = LazyOptional.of(() -> curio);

            @Nonnull
            @Override
            public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
                return CuriosCapability.ITEM.orEmpty(cap, curioOptional);
            }
        };
    }

}
