package com.lightning323.tjetpacks.enchantment;

import com.lightning323.tjetpacks.RegistryHandler;
import com.lightning323.tjetpacks.item.JetpackItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class EnchantmentFuelEfficiency extends Enchantment {

    public EnchantmentFuelEfficiency() {
        super(Rarity.RARE, RegistryHandler.JETPACK_ENCHANTMENT_TYPE, new EquipmentSlot[]{ EquipmentSlot.CHEST });
    }

    @Override
    public int getMinCost(int level) {
        return 8 + (level - 1) * 8;
    }

    @Override
    public int getMaxCost(int level) {
        return super.getMinCost(level) + 50;
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return stack.getItem() instanceof JetpackItem && super.canEnchant(stack);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return stack.getItem() instanceof JetpackItem && super.canApplyAtEnchantingTable(stack);
    }

    @Override
    public boolean isAllowedOnBooks() {
        return true;
    }
}
