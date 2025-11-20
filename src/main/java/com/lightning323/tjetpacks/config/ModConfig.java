package com.lightning323.tjetpacks.config;

import com.lightning323.tjetpacks.ThermalJetpacks;
import com.lightning323.tjetpacks.item.JetpackType;
import com.lightning323.tjetpacks.network.NetworkHandler;
import com.lightning323.tjetpacks.network.packets.PacketJetpackConfigSync;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import static com.lightning323.tjetpacks.ThermalJetpacks.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModConfig {

    public static final Builder CLIENT_BUILDER = new Builder();
    public static final Builder COMMON_BUILDER = new Builder();
    public static final Builder SERVER_BUILDER = new Builder();

    public static ForgeConfigSpec CLIENT_CONFIG;
    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec SERVER_CONFIG;

    public static void register() {
        setupClientConfig();
        setupCommonConfig();
        setupServerConfig();

        CLIENT_CONFIG = CLIENT_BUILDER.build();
        COMMON_CONFIG = COMMON_BUILDER.build();
        SERVER_CONFIG = SERVER_BUILDER.build();

        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.CLIENT, CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, COMMON_CONFIG);
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.SERVER, SERVER_CONFIG);
    }

    private static void setupClientConfig() {
        CLIENT_BUILDER.comment("Thermal Jetpacks - Client Configurations").push(MOD_ID + "-jetpack-client");

        CLIENT_BUILDER.comment("Controls Configurations").push("controls");

        invertHoverSneakingBehavior = CLIENT_BUILDER
                .comment("This sets whether you must hold sneak to hover.")
                .translation("config." + MOD_ID + ".invertHoverSneakingBehavior")
                .define("invertHoverSneakingBehavior", ConfigDefaults.invertHoverSneakingBehavior);

        CLIENT_BUILDER.pop();

        CLIENT_BUILDER.comment("Audio Configurations").push("audio");

        enableJetpackSounds = CLIENT_BUILDER
                .comment("This sets whether Jetpack sounds will play.")
                .translation("config." + MOD_ID + ".enableJetpackSounds")
                .define("enableJetpackSounds", ConfigDefaults.enableJetpackSounds);

        CLIENT_BUILDER.pop();

        CLIENT_BUILDER.comment("Visual Configurations").push("visual");

        enableJetpackParticles = CLIENT_BUILDER
                .comment("This sets whether Jetpack particles will be displayed.")
                .translation("config." + MOD_ID + ".enableJetpackParticles")
                .define("enableJetpackParticles", ConfigDefaults.enableJetpackParticles);

        CLIENT_BUILDER.pop();

        CLIENT_BUILDER.comment("GUI Configurations").push("gui");

        showThrottle = CLIENT_BUILDER
                .comment("Show the Throttle value in the Jetpack HUD.")
                .translation("config." + MOD_ID + ".showThrottle")
                .define("showThrottle", ConfigDefaults.showThrottle);

        showExactEnergy = CLIENT_BUILDER
                .comment("Show the exact energy of the Jetpack in the HUD.")
                .translation("config." + MOD_ID + ".showExactEnergy")
                .define("showExactEnergy", ConfigDefaults.showExactEnergy);

        enableStateMessages = CLIENT_BUILDER
                .comment("This sets whether or not Jetpack state messages will show.")
                .translation("config." + MOD_ID + ".enableStateMessages")
                .define("enableStateMessages", ConfigDefaults.enableStateMessages);

        enableJetpackHud = CLIENT_BUILDER
                .comment("This sets whether or not the Jetpack HUD will be visible.")
                .translation("config." + MOD_ID + ".enableJetpackHud")
                .define("enableJetpackHud", ConfigDefaults.enableJetpackHud);

        showHoverState = CLIENT_BUILDER
                .comment("Show the Hover State in the HUD.")
                .translation("config." + MOD_ID + ".showHoverState")
                .define("showHoverState", ConfigDefaults.showHoverState);

        showEHoverState = CLIENT_BUILDER
                .comment("Show the Emergency Hover State in the HUD.")
                .translation("config." + MOD_ID + ".showEHoverState")
                .define("showEHoverState", ConfigDefaults.showEHoverState);

        showChargerState = CLIENT_BUILDER
                .comment("Show the Charger State in the HUD.")
                .translation("config." + MOD_ID + ".showChargerState")
                .define("showChargerState", ConfigDefaults.showChargerState);

        hudTextColor = CLIENT_BUILDER
                .comment("This sets the color of the Jetpack HUD.")
                .translation("config." + MOD_ID + ".hudTextColor")
                .defineInRange("hudTextColor", ConfigDefaults.hudTextColor, Integer.MIN_VALUE, Integer.MAX_VALUE);

        hudTextPosition = CLIENT_BUILDER
                .comment("Set the position of the Jetpack HUD on the screen.")
                .translation("config." + MOD_ID + ".hudTextPosition")
                .defineEnum("hudTextPosition", ConfigDefaults.hudTextPosition);

        hudXOffset = CLIENT_BUILDER
                .comment("Set the X offset of the Jetpack HUD on the screen.")
                .translation("config." + MOD_ID + ".hudXOffset")
                .defineInRange("hudXOffset", ConfigDefaults.hudXOffset, Integer.MIN_VALUE, Integer.MAX_VALUE);

        hudYOffset = CLIENT_BUILDER
                .comment("Set the Y offset of the Jetpack HUD on the screen.")
                .translation("config." + MOD_ID + ".hudYOffset")
                .defineInRange("hudYOffset", ConfigDefaults.hudYOffset, Integer.MIN_VALUE, Integer.MAX_VALUE);

        hudScale = CLIENT_BUILDER
                .comment("Set the scale of the Jetpack HUD on the screen.")
                .translation("config." + MOD_ID + ".hudScale")
                .defineInRange("hudScale", ConfigDefaults.hudScale, 1, 100);

        hudTextShadow = CLIENT_BUILDER
                .comment("Set if the Jetpack HUD values have text shadows.")
                .translation("config." + MOD_ID + ".hudTextShadow")
                .define("hudTextShadow", ConfigDefaults.hudTextShadow);

        CLIENT_BUILDER.pop();
        CLIENT_BUILDER.pop();
    }

    private static void setupCommonConfig() {
        COMMON_BUILDER.comment("Thermal Jetpacks - Common Configurations").push(MOD_ID + "-jetpack-common");
        COMMON_BUILDER.comment("Jetpack Tuning Configurations").push("tuning");
        JetpackConfig.createJetpackConfig(COMMON_BUILDER);
    }

    private static void setupServerConfig() {
        SERVER_BUILDER.comment("Thermal Jetpacks - Server Configurations").push(MOD_ID + "-jetpack-server");
        //JetpackConfig.createJetpackConfig(SERVER_BUILDER);
        SERVER_BUILDER.pop();
    }

    /**
     * Send the config data from the server to the client
     *
     * @param player
     */
    public static void sendServerConfigFiles(Player player) {
        JetpackType.loadAllConfigs();
        for (JetpackType jetpack : JetpackType.JETPACK_ALL) {
            NetworkHandler.sendToClient(new PacketJetpackConfigSync(jetpack), (ServerPlayer) player);
        }
    }

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent.Loading configEvent) {
        ThermalJetpacks.LOGGER.info("Config Loaded: {}", configEvent.getConfig().getFileName());

        // Prevent loading of jetpack configs before common config has been loaded by system.
        if (configEvent.getConfig().getFileName().equals(MOD_ID + "-common.toml")) {
            JetpackType.loadAllConfigs();
        } else if (configEvent.getConfig().getFileName().equals(MOD_ID + "-client.toml")) {
            client_invertHoverSneakingBehavior = invertHoverSneakingBehavior.get();
            client_enableJetpackSounds = enableJetpackSounds.get();
            client_enableJetpackParticles = enableJetpackParticles.get();
            client_showThrottle = showThrottle.get();
            client_showExactEnergy = showExactEnergy.get();
            client_enableStateMessages = enableStateMessages.get();
            client_enableJetpackHud = enableJetpackHud.get();
            client_showHoverState = showHoverState.get();
            client_showEHoverState = showEHoverState.get();
            client_showChargerState = showChargerState.get();
            client_hudTextShadow = hudTextShadow.get();
            client_hudTextColor = hudTextColor.get();
            client_hudXOffset = hudXOffset.get();
            client_hudYOffset = hudYOffset.get();
            client_hudScale = hudScale.get();
            client_hudTextPosition = hudTextPosition.get();
        }
    }

    //Private constants
    private static ForgeConfigSpec.BooleanValue invertHoverSneakingBehavior;
    private static ForgeConfigSpec.BooleanValue enableJetpackSounds;
    private static ForgeConfigSpec.BooleanValue enableJetpackParticles;
    private static ForgeConfigSpec.BooleanValue showThrottle;
    private static ForgeConfigSpec.BooleanValue showExactEnergy;
    private static ForgeConfigSpec.BooleanValue enableStateMessages;
    private static ForgeConfigSpec.BooleanValue enableJetpackHud;
    private static ForgeConfigSpec.BooleanValue showHoverState;
    private static ForgeConfigSpec.BooleanValue showEHoverState;
    private static ForgeConfigSpec.BooleanValue showChargerState;
    private static ForgeConfigSpec.BooleanValue hudTextShadow;
    private static ForgeConfigSpec.IntValue hudTextColor;
    private static ForgeConfigSpec.IntValue hudXOffset;
    private static ForgeConfigSpec.IntValue hudYOffset;
    private static ForgeConfigSpec.LongValue hudScale;
    private static ForgeConfigSpec.EnumValue<ConfigDefaults.HUDPosition> hudTextPosition;

    //Public constants
    public static boolean client_invertHoverSneakingBehavior;
    public static boolean client_enableJetpackSounds;
    public static boolean client_enableJetpackParticles;
    public static boolean client_showThrottle;
    public static boolean client_showExactEnergy;
    public static boolean client_enableStateMessages;
    public static boolean client_enableJetpackHud;
    public static boolean client_showHoverState;
    public static boolean client_showEHoverState;
    public static boolean client_showChargerState;
    public static boolean client_hudTextShadow;
    public static int client_hudTextColor;
    public static int client_hudXOffset;
    public static int client_hudYOffset;
    public static long client_hudScale;
    public static ConfigDefaults.HUDPosition client_hudTextPosition;
}
