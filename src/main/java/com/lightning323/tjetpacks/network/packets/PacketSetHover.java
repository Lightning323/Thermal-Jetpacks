package com.lightning323.tjetpacks.network.packets;

import com.lightning323.tjetpacks.item.JetpackItem;
import com.lightning323.tjetpacks.network.NetworkHandler;
import com.lightning323.tjetpacks.util.JetpackUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSetHover {

    final ToggleStatus status;

    public PacketSetHover(FriendlyByteBuf buf) {
        status = buf.readEnum(ToggleStatus.class);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeEnum(status);
    }

    public PacketSetHover(ToggleStatus status) {
        this.status = status;
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                ItemStack stack = JetpackUtil.getItemFromChest(player);
                Item item = stack.getItem();
                if (item instanceof JetpackItem jetpack) {
                    if(status == ToggleStatus.TOGGLE) jetpack.setHoverOn(stack, player, !jetpack.isHoverOn(stack));
                    else jetpack.setHoverOn(stack, player, status == ToggleStatus.ON);
                    //Send the status back to the client
                    NetworkHandler.sendToClient(new PacketUpdateClientJetpackUI(jetpack, stack), player);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}