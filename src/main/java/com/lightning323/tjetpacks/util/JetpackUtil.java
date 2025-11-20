package com.lightning323.tjetpacks.util;

import com.lightning323.tjetpacks.item.JetpackItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import top.theillusivec4.curios.api.CuriosApi;

public class JetpackUtil {

    private final static boolean CURIOS_LOADED = ModList.get().isLoaded("curios");

    public static ItemStack getItemFromChest(Player player) {
//        ItemStack jetpackItem = getFromChest(player);
//
//        if (jetpackItem == ItemStack.EMPTY && curiosLoaded) {
//            //https://docs.illusivesoulworks.com/1.20.x/curios/inventory/basic-inventory
//            ICuriosItemHandler curiosInventory = CuriosApi.getCuriosInventory(player).resolve().get();
//            SlotResult slotResult = curiosInventory
//                    .findFirstCurio(stack -> stack.getItem() instanceof JetpackItem)
//                    .orElse(null);
//            if (slotResult != null) return slotResult.stack();
//
//
////            return CuriosApi.getCuriosHelper()
////                    .findEquippedCurio(stack -> stack.getItem() instanceof JetpackItem, player)
////                    .map(ImmutableTriple::getRight).orElse(ItemStack.EMPTY);
//        }
//        return jetpackItem;
        return player.getItemBySlot(EquipmentSlot.CHEST);
    }


    /*
     * Train of thought:
     * Curios slot has priority
     * - If a jetpack is in the curios slot, only use this jetpack
     * - Jetpack must be identified by item stack match
     *
     * */
    public static boolean checkTickForEquippedSlot(int index, ItemStack which, Player player) {
        boolean isNormalChestSlotCorrect = index == EquipmentSlot.CHEST.getIndex()
                && player.getItemBySlot(EquipmentSlot.CHEST) == which;
        int checkCuriosFlag = checkCuriosSlot(which, player);

        if (checkCuriosFlag >= 0) {
            if (checkCuriosFlag > 0) {
                return checkCuriosFlag > 1;
            } else {
                return isNormalChestSlotCorrect;
            }
        } else {
            return isNormalChestSlotCorrect;
        }
    }

    // -1 if no curios
    // 0 if curios but not correct slot
    // 1 if curios and any jetpack item in slot
    // 2 if curios and correct slot
    private static int checkCuriosSlot(ItemStack which, Player player) {
        if (CURIOS_LOADED) {
            ItemStack curioStack = CuriosApi.getCuriosHelper().findEquippedCurio(stack -> stack.getItem() instanceof JetpackItem, player)
                    .map(ImmutableTriple::getRight).orElse(ItemStack.EMPTY);
            if (curioStack.isEmpty()) {
                return 0;
            } else {
                if (curioStack == which) {
                    return 2;
                } else {
                    return 1;
                }
            }
        } else {
            return -1;
        }
    }
}
