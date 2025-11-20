package com.lightning323.tjetpacks.handlers;

import com.lightning323.tjetpacks.ThermalJetpacks;
import com.lightning323.tjetpacks.config.ModConfig;
import com.lightning323.tjetpacks.item.JetpackItem;
import com.lightning323.tjetpacks.item.JetpackType;
import com.lightning323.tjetpacks.network.NetworkHandler;
import com.lightning323.tjetpacks.network.packets.PacketUpdateInput;
import com.lightning323.tjetpacks.util.JetpackParticleType;
import com.lightning323.tjetpacks.util.JetpackUtil;
import com.lightning323.tjetpacks.util.Pos3D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

import static com.ibm.icu.text.RuleBasedNumberFormat.DURATION;

@OnlyIn(Dist.CLIENT)
public class ClientJetpackHandler {


    @SubscribeEvent
    public void onClientPlayerQuit(final ClientPlayerNetworkEvent.LoggingOut loggedOutEvent) {
        ThermalJetpacks.LOGGER.info("Reverting jetpack settings to client config.");
        JetpackType.loadAllConfigs();
        ThermalJetpacks.LOGGER.info("Client jetpack config successfully reverted.");
    }

    /**
     * These static fields are used to save us from having to constantly check if the player is wearing a jetpack
     */
    private static JetpackItem CLIENT_GLOBAL_JETPACK;//This value can be null
    private static ItemStack CLIENT_GLOBAL_JETPACK_STACK = ItemStack.EMPTY;//This value must not be null, it can only be empty

    public static ItemStack global_checkForEquippedJetpack(Player player) {
        CLIENT_GLOBAL_JETPACK_STACK = JetpackUtil.getItemFromChest(player);
        if (!CLIENT_GLOBAL_JETPACK_STACK.isEmpty() && CLIENT_GLOBAL_JETPACK_STACK.getItem() instanceof JetpackItem jetpack2) {
            CLIENT_GLOBAL_JETPACK = jetpack2;
        } else CLIENT_GLOBAL_JETPACK = null;
        return CLIENT_GLOBAL_JETPACK_STACK;
    }

    public static JetpackItem global_getEquippedJetpack() {
        return CLIENT_GLOBAL_JETPACK;
    }

    public static ItemStack global_getEquippedJetpackStack() {
        return CLIENT_GLOBAL_JETPACK_STACK;
    }

    //For client tick only:
    private static JetpackItem jetpack;
    private static ItemStack jetpackStack;
    private static boolean lastFlyState = false;
    private static boolean lastInvertHover = false;
    private static boolean lastDescendState = false;
    private static boolean lastForwardState = false;
    private static boolean lastBackwardState = false;
    private static boolean lastLeftState = false;
    private static boolean lastRightState = false;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {

            //Check if we are wearing a jetpack at the very beginning
            jetpack = global_getEquippedJetpack();
            jetpackStack = global_getEquippedJetpackStack();
            if (jetpack == null) return;

            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.player != null
                    && minecraft.level != null
                    && !minecraft.player.isSpectator()
                    && !minecraft.player.getAbilities().flying) {

                if (minecraft.level.getGameTime() % 40 == 0) { //Make sure the jetpack is still here! Check every 40 ticks
                    global_checkForEquippedJetpack(minecraft.player);
                    if (global_getEquippedJetpack() == null) return;
                }

                common_updateFlightState(minecraft);

                if (isJetpackFlying(minecraft.player, jetpackStack, jetpack)) {
                    //Make particles
                    if (ModConfig.client_enableJetpackParticles
                            && (minecraft.options.particles().get() != ParticleStatus.MINIMAL)) {
                        makeParticles(minecraft, JetpackParticleType.CLOUD);
                    }
                    // Play sounds:
                    if (minecraft.level.getGameTime() % DURATION == 0 &&
                            ModConfig.client_enableJetpackSounds &&
                            !JetpackSoundEvent.playing(minecraft.player.getId())) {
                        minecraft.getSoundManager().play(new JetpackSoundEvent(minecraft.player));
                    }

                }
            }
        }
    }


    private static void common_updateFlightState(Minecraft mc) {
        if (mc.player != null) {
            //Update the state and send to server
            boolean flyState = mc.player.input.jumping;
            boolean invertHover = ModConfig.client_invertHoverSneakingBehavior;
            boolean descendState = mc.player.input.shiftKeyDown;
            boolean forwardState = mc.player.input.up;
            boolean backwardState = mc.player.input.down;
            boolean leftState = mc.player.input.left;
            boolean rightState = mc.player.input.right;
            if (flyState != lastFlyState
                    || invertHover != lastInvertHover
                    || descendState != lastDescendState
                    || forwardState != lastForwardState
                    || backwardState != lastBackwardState
                    || leftState != lastLeftState
                    || rightState != lastRightState) {
                lastFlyState = flyState;
                lastInvertHover = invertHover;
                lastDescendState = descendState;
                lastForwardState = forwardState;
                lastBackwardState = backwardState;
                lastLeftState = leftState;
                lastRightState = rightState;
                NetworkHandler.sendToServer(new PacketUpdateInput(invertHover, flyState, descendState, forwardState, backwardState, leftState, rightState));
                CommonJetpackHandler.update(mc.player, invertHover, flyState, descendState, forwardState, backwardState, leftState, rightState);
            }
        }
    }

    private boolean isJetpackFlying(LocalPlayer player, ItemStack stack, JetpackItem jetpack) {
        if (jetpack.isEngineOn(stack) && (jetpack.getEnergy(stack) > 0 || jetpack.isCreative)) {
            if (jetpack.isHoverOn(stack)) {
                return !player.onGround();
            } else {
                return CommonJetpackHandler.isHoldingUp(player);
            }
        }
        return false;
    }

    private final Random rand = new Random();

    private void makeParticles(Minecraft minecraft, JetpackParticleType particleType) {
        ParticleOptions particle = particleType.getParticleData();

        float random = (rand.nextFloat() - 0.5F) * 0.1F;
        double[] sneakBonus = minecraft.player.isCrouching() ? new double[]{-0.30, -0.10} : new double[]{0, 0};
        Pos3D playerPos = new Pos3D(minecraft.player).translate(0, 1.5, 0);
        Pos3D vLeft = new Pos3D(-0.18, -0.90 + sneakBonus[1], -0.30 + sneakBonus[0]).rotate(minecraft.player.yBodyRot, 0);
        Pos3D vRight = new Pos3D(0.18, -0.90 + sneakBonus[1], -0.30 + sneakBonus[0]).rotate(minecraft.player.yBodyRot, 0);

        Pos3D v = playerPos.translate(vLeft).translate(new Pos3D(minecraft.player.getDeltaMovement()));
        minecraft.particleEngine.createParticle(particle, v.x, v.y, v.z, random, -0.2D, random);
        v = playerPos.translate(vRight).translate(new Pos3D(minecraft.player.getDeltaMovement()));
        minecraft.particleEngine.createParticle(particle, v.x, v.y, v.z, random, -0.2D, random);
    }

}
