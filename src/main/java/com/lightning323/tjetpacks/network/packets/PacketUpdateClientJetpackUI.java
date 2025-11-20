package com.lightning323.tjetpacks.network.packets;

import com.lightning323.tjetpacks.client.ui.JetpackScreen;
import com.lightning323.tjetpacks.item.JetpackItem;
import com.lightning323.tjetpacks.network.NetworkHandler;
import com.lightning323.tjetpacks.util.JetpackUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketUpdateClientJetpackUI {

    final boolean engineOn, hoverOn, eHoverOn, chargerOn;

    public PacketUpdateClientJetpackUI(FriendlyByteBuf buf) {
        engineOn = buf.readBoolean();
        hoverOn = buf.readBoolean();
        eHoverOn = buf.readBoolean();
        chargerOn = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(engineOn);
        buf.writeBoolean(hoverOn);
        buf.writeBoolean(eHoverOn);
        buf.writeBoolean(chargerOn);
    }

    /**
     * Used for sending status (server)
     *
     * @param jetpack
     * @param stack
     */
    public PacketUpdateClientJetpackUI(JetpackItem jetpack, ItemStack stack) {
        this.engineOn = jetpack.isEngineOn(stack);
        this.hoverOn = jetpack.isHoverOn(stack);
        this.eHoverOn = jetpack.isEHoverOn(stack);
        this.chargerOn = jetpack.isChargerOn(stack);
    }

    public static PacketUpdateClientJetpackUI fromBytes(FriendlyByteBuf buffer) {
        return new PacketUpdateClientJetpackUI(buffer);
    }


    /**
     * Used for sending a REQUEST for status (client)
     */
    public PacketUpdateClientJetpackUI() {
        this.engineOn = false;
        this.hoverOn = false;
        this.eHoverOn = false;
        this.chargerOn = false;
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
//                System.out.println("PacketJetpackStatus: receiving status");
//                Player player = Minecraft.getInstance().player;
//                if (player != null) {
//                    ItemStack stack = JetpackUtil.getFromChestAndCurios(player);
//                    Item item = stack.getItem();
//                    if (item instanceof JetpackItem jetpack) {
                System.out.println("PacketJetpackStatus: " + engineOn + ", " + hoverOn + ", " + eHoverOn + ", " + chargerOn);
                JetpackScreen.update(engineOn, hoverOn, eHoverOn, chargerOn);
//                    }
//            }
            } else if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER) {
                System.out.println("PacketJetpackStatus: Requesting status");
                ServerPlayer player = ctx.get().getSender();
                if (player != null) { //If the client sent this packet, its requesting us to send status to them
                    ItemStack stack = JetpackUtil.getItemFromChest(player);
                    Item item = stack.getItem();
                    if (item instanceof JetpackItem jetpack) {
                        NetworkHandler.sendToClient(new PacketUpdateClientJetpackUI(jetpack, stack), player);
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}