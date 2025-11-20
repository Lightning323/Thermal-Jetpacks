package com.lightning323.tjetpacks.client.ui;

import com.lightning323.tjetpacks.ThermalJetpacks;
import com.lightning323.tjetpacks.config.ConfigDefaults;
import com.lightning323.tjetpacks.config.ModConfig;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class HUDRenderHelper {

    private static final Font font = Minecraft.getInstance().font;

    public static void drawStringAtPosition(GuiGraphics graphics, Window window, Component text, int lineOffset) {
        int windowScaleHeight = window.getGuiScaledHeight();
        int windowScaleWidth = window.getGuiScaledWidth();

        ConfigDefaults.HUDPosition position = ModConfig.client_hudTextPosition;
        int color = ModConfig.client_hudTextColor;
        int xOffset = ModConfig.client_hudXOffset;
        int yOffset = ModConfig.client_hudYOffset;
        long hudScale = ModConfig.client_hudScale;
        boolean hudTextShadow = ModConfig.client_hudTextShadow;

        int screenHeight = (int) (windowScaleHeight / hudScale);
        int screenWidth = (int) (windowScaleWidth / hudScale);

        switch (position) {
            case TOP_LEFT -> {
                yOffset += lineOffset * 9;
                drawStringLeft(graphics, text, 2 + xOffset, 2 + yOffset, color, hudTextShadow);
            }
            case TOP_CENTER -> {
                yOffset += lineOffset * 9;
                drawStringCenter(graphics, text, screenWidth / 2 + xOffset, 2 + yOffset, color, hudTextShadow);
            }
            case TOP_RIGHT -> {
                yOffset += lineOffset * 9;
                drawStringRight(graphics, text, screenWidth - 2 + xOffset, 2 + yOffset, color, hudTextShadow);
            }
            case LEFT -> {
                yOffset += lineOffset * 9;
                drawStringLeft(graphics, text, 2 + xOffset, screenHeight / 2 + yOffset, color, hudTextShadow);
            }
            case RIGHT -> {
                yOffset += lineOffset * 9;
                drawStringRight(graphics, text, screenWidth - 2 + xOffset, screenHeight / 2 + yOffset, color, hudTextShadow);
            }
            case BOTTOM_LEFT -> {
                yOffset -= lineOffset * 9;
                drawStringLeft(graphics, text, 2 + xOffset, screenHeight - 9 + yOffset, color, hudTextShadow);
            }
            case BOTTOM_RIGHT -> {
                yOffset -= lineOffset * 9;
                drawStringRight(graphics, text, screenWidth - 2 + xOffset, screenHeight - 9 + yOffset, color, hudTextShadow);
            }
            default -> ThermalJetpacks.LOGGER.info("Invalid HUD Position passed to renderer.");
        }
    }

    private static void drawStringLeft(GuiGraphics graphics, Component text, int x, int y, int color, boolean shadow) {
        graphics.drawString(font, text, x, y, color, shadow);
    }

    private static void drawStringCenter(GuiGraphics graphics, Component text, int x, int y, int color, boolean shadow) {
        float textWidth = font.width(text);
        graphics.drawString(font, text, (int) (x - (textWidth / 2)), y, color, shadow);
    }

    private static void drawStringRight(GuiGraphics graphics, Component text, int x, int y, int color, boolean shadow) {
        float textWidth = font.width(text);
        graphics.drawString(font, text, (int) (x - textWidth), y, color, shadow);
    }
}
