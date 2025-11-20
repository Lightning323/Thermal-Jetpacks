package com.lightning323.tjetpacks.network;

import com.lightning323.tjetpacks.network.packets.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import static com.lightning323.tjetpacks.ThermalJetpacks.MOD_ID;

public class NetworkHandler {

    public static SimpleChannel CHANNEL_INSTANCE;
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void registerMessages() {
        CHANNEL_INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(MOD_ID, MOD_ID), () -> "1.0", s -> true, s -> true);

        CHANNEL_INSTANCE.messageBuilder(PacketSetEngine.class, nextID())
                .encoder(PacketSetEngine::toBytes)
                .decoder(PacketSetEngine::new)
                .consumerNetworkThread(PacketSetEngine::handle)
                .add();
        CHANNEL_INSTANCE.messageBuilder(PacketSetHover.class, nextID())
                .encoder(PacketSetHover::toBytes)
                .decoder(PacketSetHover::new)
                .consumerNetworkThread(PacketSetHover::handle)
                .add();

        CHANNEL_INSTANCE.messageBuilder(PacketSetEHover.class, nextID())
                .encoder(PacketSetEHover::toBytes)
                .decoder(PacketSetEHover::new)
                .consumerNetworkThread(PacketSetEHover::handle)
                .add();

        CHANNEL_INSTANCE.messageBuilder(PacketSetCharger.class, nextID())
                .encoder(PacketSetCharger::toBytes)
                .decoder(PacketSetCharger::new)
                .consumerNetworkThread(PacketSetCharger::handle)
                .add();

        CHANNEL_INSTANCE.messageBuilder(PacketUpdateInput.class, nextID())
                .encoder(PacketUpdateInput::toBytes)
                .decoder(PacketUpdateInput::fromBytes)
                .consumerNetworkThread(PacketUpdateInput::handle)
                .add();

        CHANNEL_INSTANCE.messageBuilder(PacketUpdateThrottle.class, nextID())
                .encoder(PacketUpdateThrottle::toBytes)
                .decoder(PacketUpdateThrottle::fromBytes)
                .consumerNetworkThread(PacketUpdateThrottle::handle)
                .add();

        CHANNEL_INSTANCE.messageBuilder(PacketJetpackConfigSync.class, nextID())
                .encoder(PacketJetpackConfigSync::toBytes)
                .decoder(PacketJetpackConfigSync::fromBytes)
                .consumerNetworkThread(PacketJetpackConfigSync::handle)
                .add();

        CHANNEL_INSTANCE.messageBuilder(PacketEnableJetpackHUD.class, nextID())
                .encoder(PacketEnableJetpackHUD::toBytes)
                .decoder(PacketEnableJetpackHUD::fromBytes)
                .consumerNetworkThread(PacketEnableJetpackHUD::handle)
                .add();

        CHANNEL_INSTANCE.messageBuilder(PacketUpdateClientJetpackUI.class, nextID())
                .encoder(PacketUpdateClientJetpackUI::toBytes)
                .decoder(PacketUpdateClientJetpackUI::fromBytes)
                .consumerNetworkThread(PacketUpdateClientJetpackUI::handle)
                .add();
    }

    public static void sendToClient(Object packet, ServerPlayer player) {
        CHANNEL_INSTANCE.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToServer(Object packet) {
        CHANNEL_INSTANCE.sendToServer(packet);
    }
}
