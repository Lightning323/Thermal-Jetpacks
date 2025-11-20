package com.lightning323.tjetpacks.client.ui;

import com.lightning323.tjetpacks.config.ModConfig;
import com.lightning323.tjetpacks.handlers.ClientJetpackHandler;
import com.lightning323.tjetpacks.item.JetpackItem;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public final class HUDHandler {

    public static boolean renderJetpackHUD = false;

    private static final IGuiOverlay HUD_OVERLAY = (gui, gfx, partialTick, width, height) -> {
        if (renderJetpackHUD) {
            var minecraft = Minecraft.getInstance();
            if (ModConfig.client_enableJetpackHud
                    && !minecraft.options.hideGui
                    && !minecraft.options.renderDebug
                    && minecraft.player != null
                    && minecraft.level != null) {

                if (minecraft.level.getGameTime() % 40 == 0) {//Check every 40 ticks
                    ClientJetpackHandler.global_checkForEquippedJetpack(minecraft.player);
                }

                ItemStack jetpackItem = ClientJetpackHandler.global_getEquippedJetpackStack();
                if (!jetpackItem.isEmpty() && jetpackItem.getItem() instanceof JetpackItem provider) {
                    List<Component> renderStrings = new ArrayList<>();
                    provider.addHUDInfo(jetpackItem, renderStrings);
                    if (renderStrings.isEmpty()) {
                        return;
                    }
                    int count = 0;
                    PoseStack matrix = gfx.pose();
                    matrix.pushPose();
                    matrix.scale(ModConfig.client_hudScale, ModConfig.client_hudScale, 1.0F);
                    Window window = minecraft.getWindow();
                    for (Component text : renderStrings) {
                        HUDRenderHelper.drawStringAtPosition(gfx, window, text, count);
                        count++;
                    }
                    matrix.popPose();
                }
            }
        }
    };

    @SubscribeEvent
    public void registerOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAbove(VanillaGuiOverlay.HOTBAR.id(), "hud", HUD_OVERLAY);
    }
}