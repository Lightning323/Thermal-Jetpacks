package com.lightning323.tjetpacks.client.ui;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface IHUDInfoProvider {
    void addHUDInfo(ItemStack stack, List<Component> list);
}