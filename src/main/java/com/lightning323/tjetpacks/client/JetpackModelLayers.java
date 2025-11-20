package com.lightning323.tjetpacks.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.lightning323.tjetpacks.ThermalJetpacks.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class JetpackModelLayers {

    public static final ModelLayerLocation JETPACK_LAYER = new ModelLayerLocation(new ResourceLocation(MOD_ID, "jetpack_layer"), "main");
    public static JetpackModel<LivingEntity> JETPACK_MODEL = null;

    @SubscribeEvent
    public static void initLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(JETPACK_LAYER, JetpackModel::createLayer);
    }

    @SubscribeEvent
    public static void initModels(EntityRenderersEvent.AddLayers event) {
        JETPACK_MODEL = new JetpackModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(JetpackModelLayers.JETPACK_LAYER));
    }
}