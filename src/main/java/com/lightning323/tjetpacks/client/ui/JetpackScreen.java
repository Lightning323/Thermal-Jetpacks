package com.lightning323.tjetpacks.client.ui;

import com.lightning323.tjetpacks.handlers.KeybindHandler;
import com.lightning323.tjetpacks.item.JetpackItem;
import com.lightning323.tjetpacks.network.NetworkHandler;
import com.lightning323.tjetpacks.network.packets.*;
import com.lightning323.tjetpacks.util.JetpackUtil;
import com.lightning323.tjetpacks.util.TextUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.widget.ForgeSlider;

import static com.lightning323.tjetpacks.ThermalJetpacks.MOD_ID;

@OnlyIn(Dist.CLIENT)
public class JetpackScreen extends Screen {

    private static final Minecraft minecraft = Minecraft.getInstance();
    private static final float LEAN_FACTOR = 2.0f;

    private final ResourceLocation JETPACK_TEXTURE = new ResourceLocation(MOD_ID, "textures/gui/jetpack_screen.png");
    private static final int WIDTH = 150;
    private static final int HEIGHT = 144;
    private static final int Y_UI_BELOW_TEXT = 20;

    private final JetpackItem jetpackItem;
    private final ItemStack jetpackStack;

    private static ToggleImageButton engineButton, hoverButton, ehoverButton, chargerButton;
    private ForgeSlider slider;

    public JetpackScreen() {
        super(Component.translatable("screen." + MOD_ID + ".jetpack_screen.title"));
        this.width = WIDTH;
        this.height = HEIGHT;
        this.jetpackItem = (JetpackItem) JetpackUtil.getItemFromChest(minecraft.player).getItem();
        this.jetpackStack = JetpackUtil.getItemFromChest(minecraft.player);
    }

    public static void update(boolean engineOn, boolean hoverOn, boolean eHoverOn, boolean chargerOn) {
        if (engineButton != null) {
            engineButton.setToggled(engineOn);
        }
        if (hoverButton != null) {
            hoverButton.setToggled(hoverOn);
        }
        if (ehoverButton != null) {
            ehoverButton.setToggled(eHoverOn);
        }
        if (chargerButton != null) {
            chargerButton.setToggled(chargerOn);
        }
    }


    @Override
    protected void init() {
        int relX = (this.width - WIDTH) / 2;
        int relY = (this.height - HEIGHT) / 2;

        Item item = jetpackStack.getItem();
        Player player = minecraft.player;
        assert player != null;

        NetworkHandler.sendToServer(new PacketUpdateClientJetpackUI());


        if (item instanceof JetpackItem jetpack) {
            int y = Y_UI_BELOW_TEXT;
            int x = WIDTH - 20 - 10;
            engineButton = new ToggleImageButton(relX + x, relY + y, 20, 20, 176,
                    40, 20, 0, JETPACK_TEXTURE,
                    button -> {
//                        boolean on = !jetpack.isEngineOn(jetpackStack);
                        NetworkHandler.sendToServer(new PacketSetEngine(ToggleStatus.TOGGLE));
                    });
            engineButton.setToggled(false);
            engineButton.setTooltip(Tooltip.create(Component.translatable("tooltip." + MOD_ID + ".engine_on")));
            if (engineButton.active) {
                y += 22;
                addRenderableWidget(engineButton);
            }

            hoverButton = new ToggleImageButton(relX + x, relY + y, 20, 20, 216,
                    40, 20, 0, JETPACK_TEXTURE,
                    button -> {
//                        boolean on = !jetpack.isEngineOn(jetpackStack);
                        NetworkHandler.sendToServer(new PacketSetHover(ToggleStatus.TOGGLE));
                    });
            hoverButton.setToggled(false);
            hoverButton.setTooltip(Tooltip.create(Component.translatable("tooltip." + MOD_ID + ".hover_on")));
            hoverButton.active = jetpack.getJetpackType().getHoverMode();
            if (hoverButton.active) {
                y += 22;
                addRenderableWidget(hoverButton);
            }

            chargerButton = new ToggleImageButton(relX + x, relY + y, 20, 20, 196,
                    40, 20, 0, JETPACK_TEXTURE,
                    button -> {
//                        boolean on = !jetpack.isChargerOn(jetpackStack);
                        NetworkHandler.sendToServer(new PacketSetCharger(ToggleStatus.TOGGLE));
                    });
            chargerButton.setToggled(false);
            chargerButton.setTooltip(Tooltip.create(Component.translatable("tooltip." + MOD_ID + ".charger_on")));
            chargerButton.active = jetpack.getJetpackType().getChargerMode();
            if (chargerButton.active) {
                y += 22;
                addRenderableWidget(chargerButton);
            }

            ehoverButton = new ToggleImageButton(relX + x, relY + y, 20, 20, 236,
                    40, 20, 0, JETPACK_TEXTURE,
                    button -> {
//                        boolean on = !jetpack.isEHoverOn(jetpackStack);
                        NetworkHandler.sendToServer(new PacketSetEHover(ToggleStatus.TOGGLE));
                    });
            ehoverButton.setToggled(false);
            ehoverButton.setTooltip(Tooltip.create(Component.translatable("tooltip." + MOD_ID + ".ehover_on")));
            ehoverButton.active = jetpack.getJetpackType().getEmergencyHoverMode();
            if (ehoverButton.active) {
                y += 22;
                addRenderableWidget(ehoverButton);
            }
        }

        addRenderableWidget(slider = new ForgeSlider(
                relX + 10, relY + 118,
                WIDTH - 20, 16,
                Component.translatable("screen." + MOD_ID + ".throttle"),
                Component.literal("%"), 0, 100,
                jetpackItem.getThrottle(jetpackStack), true));
    }

    final int barOffTextureStartX = 0;
    final int barOnTextureStartX = 14;
    final int barCreativeTextureStartX = 56;
    final int barTexStartY = 170;
    final int barXStart = 10;
    final int barYStart = Y_UI_BELOW_TEXT;
    final int barHeight = 86;
    final int barWidth = 14;

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        int relX = (this.width - WIDTH) / 2;
        int relY = (this.height - HEIGHT) / 2;

        //Background
        RenderSystem.setShaderTexture(0, JETPACK_TEXTURE);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        graphics.blit(JETPACK_TEXTURE, relX, relY, 0, 0, WIDTH, HEIGHT);

        assert minecraft.screen != null;
        float halfScreenHeight = minecraft.screen.height / 2f;
        float halfScreenWidth = minecraft.screen.width / 2f;
        float angleX = -((mouseX - halfScreenWidth) / halfScreenWidth * LEAN_FACTOR);
        float angleY = -(((mouseY + 25) - halfScreenHeight) / halfScreenHeight * LEAN_FACTOR);

        //Player
        InventoryScreen.renderEntityInInventoryFollowsAngle(graphics, (relX + WIDTH / 2) - 4, relY + 100, 38, angleX, angleY, minecraft.player);

        //Title
//        graphics.drawCenteredString(minecraft.font, Component.translatable(jetpackStack.getDescriptionId()), relX + WIDTH / 2, relY + 5, 0xFFFFFF);

        int textWidth = minecraft.font.width(Component.translatable(jetpackStack.getDescriptionId()));
        graphics.drawString(minecraft.font, Component.translatable(jetpackStack.getDescriptionId()),
                relX + (WIDTH - textWidth) / 2,
                relY + 5, 0x303030, false);

        RenderSystem.setShaderTexture(0, JETPACK_TEXTURE);

        //Slider
        NetworkHandler.sendToServer(new PacketUpdateThrottle(slider.getValueInt()));

        int amount = getEnergyBarAmount(barHeight); // Texture height
        int barOffset = barHeight - amount;

        graphics.blit(JETPACK_TEXTURE, relX + barXStart, relY + barYStart,
                jetpackItem.isCreative ? barCreativeTextureStartX : barOffTextureStartX
                , barTexStartY, barWidth, barHeight);

        if (!jetpackItem.isCreative) {
//                graphics.fillGradient(relX + 12, relY + barYStart + 2 + barOffset,
//                        relX + 22, relY + 14 + barHeight, 0xffb51500, 0xff600b00);

            graphics.blit(JETPACK_TEXTURE, relX + barXStart, relY + barYStart + 1 + barOffset,
                    barOnTextureStartX, barTexStartY + 1, barWidth, amount - 2);
        }
        // This does not update like a screen container :(
        if (mouseX >= relX + barXStart && mouseY >= relY + barYStart && mouseX < relX + barXStart + barWidth && mouseY < relY + barYStart + barHeight) {
            Component text;
            if (jetpackItem.isCreative) {
                text = TextUtils.translate("tooltip", "infiniteEnergy", ChatFormatting.LIGHT_PURPLE);
            } else if (jetpackItem.getEnergy(jetpackStack) == 0) {
                text = TextUtils.translate("hud", "energyDepleted", ChatFormatting.RED);
            } else {
                text = TextUtils.energyWithMax(jetpackItem.getEnergy(jetpackStack), jetpackItem.getCapacity(jetpackStack));
            }

            if (text != null) graphics.renderTooltip(font, text, mouseX, mouseY);
        }
        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    private int getEnergyBarAmount(int barHeight) {
        Item item = jetpackStack.getItem();
        if (item instanceof JetpackItem) {
            JetpackItem jetpack = (JetpackItem) item;
            if (jetpack.isCreative) {
                return barHeight;
            }
            int i = jetpack.getEnergy(jetpackStack);
            int j = jetpack.getCapacity(jetpackStack);
            return (int) (j != 0 && i != 0 ? (long) i * barHeight / j : 0);
        }
        return 0;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (KeybindHandler.JETPACK_GUI_KEY.matches(keyCode, scanCode) || minecraft.options.keyInventory.matches(keyCode, scanCode)) {
            minecraft.setScreen(null);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}