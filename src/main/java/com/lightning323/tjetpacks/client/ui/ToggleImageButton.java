package com.lightning323.tjetpacks.client.ui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.resources.ResourceLocation;

public class ToggleImageButton extends ImageButton {
    final int yTexStart_On;
    boolean isOn = true;

    public ToggleImageButton(int x, int y, int width, int height, int xTexStart,
                             int yTexStart, int yDiffTex, int yTexStart_On,
                             ResourceLocation texture,
                             OnPress onPress) {

        super(x, y, width, height, xTexStart, yTexStart, yDiffTex, texture, onPress);
        this.yTexStart_On = yTexStart_On;
    }

    public void toggle() {
        isOn = !isOn;
    }

    public void setToggled(boolean on) {
        isOn = on;
    }

    public boolean isOn() {
        return isOn;
    }

    public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderTexture(pGuiGraphics, this.resourceLocation, this.getX(), this.getY(),
                this.xTexStart,
                isOn ? this.yTexStart_On : this.yTexStart,
                this.yDiffTex,
                this.width, this.height, this.textureWidth, this.textureHeight);
    }


}
