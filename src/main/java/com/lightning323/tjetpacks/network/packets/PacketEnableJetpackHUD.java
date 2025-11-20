package com.lightning323.tjetpacks.network.packets;

import com.lightning323.tjetpacks.client.ui.HUDHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketEnableJetpackHUD {
    final boolean enabled;

    public PacketEnableJetpackHUD(FriendlyByteBuf buf) {
        this.enabled = buf.readBoolean();
    }

    public PacketEnableJetpackHUD(boolean enabled) {
        this.enabled = enabled;
    }


    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(enabled);
    }

    public static PacketEnableJetpackHUD fromBytes(FriendlyByteBuf buffer) {
        return new PacketEnableJetpackHUD(buffer);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            System.out.println("Toggling HUD: " + enabled);
            HUDHandler.renderJetpackHUD = enabled;
        });
        ctx.get().setPacketHandled(true);
    }
}
