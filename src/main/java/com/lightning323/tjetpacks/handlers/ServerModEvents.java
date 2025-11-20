package com.lightning323.tjetpacks.handlers;

import com.lightning323.tjetpacks.RegistryHandler;
import com.lightning323.tjetpacks.network.NetworkHandler;
import com.lightning323.tjetpacks.network.packets.PacketEnableJetpackHUD;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.lightning323.tjetpacks.ThermalJetpacks.MOD_ID;

/**
 * Only works on the server
 */
@Mod.EventBusSubscriber(modid = MOD_ID)
public class ServerModEvents {

//    @SubscribeEvent
//    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
//        ServerPlayer player = (ServerPlayer) event.getEntity();
//        // Your logic here
//    }

    @SubscribeEvent
    public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        /**
         * Create an event to turn on the HUD when the player equips the goggles
         */
        if (event.getTo().getItem() == RegistryHandler.PILOT_GOGGLES_GOLD.get() ||
                event.getTo().getItem() == RegistryHandler.PILOT_GOGGLES_IRON.get()) {
            NetworkHandler.sendToClient(new PacketEnableJetpackHUD(true), (ServerPlayer) player);

        } else if (event.getFrom().getItem() == RegistryHandler.PILOT_GOGGLES_GOLD.get() ||
                event.getFrom().getItem() == RegistryHandler.PILOT_GOGGLES_IRON.get()) {
            NetworkHandler.sendToClient(new PacketEnableJetpackHUD(false), (ServerPlayer) player);
        }

//        if (event.getTo().getItem() instanceof JetpackItem jetpack
//                && event.getSlot() == EquipmentSlot.CHEST) {
//            ClientJetpackHandler.jetpackItemStack = event.getTo();
//            ClientJetpackHandler.jetpackItem = jetpack;
//        } else if (event.getFrom().getItem() instanceof JetpackItem jetpack
//                && event.getSlot() == EquipmentSlot.CHEST) {
//            ClientJetpackHandler.jetpackItem = null;
//            ClientJetpackHandler.jetpackItemStack = null;
//        }
    }
}