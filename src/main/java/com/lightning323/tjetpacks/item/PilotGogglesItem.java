package com.lightning323.tjetpacks.item;

import com.lightning323.tjetpacks.util.TextUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

import static com.lightning323.tjetpacks.ThermalJetpacks.MOD_ID;

public class PilotGogglesItem extends ArmorItem {

    private final String materialType;

    public PilotGogglesItem(String materialType) {
        super(JetpackArmorMaterial.PILOT_GOGGLES, Type.HELMET, new Properties());
        this.materialType = materialType;
    }

    public String getMaterialType() {
        return materialType;
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return new ResourceLocation(MOD_ID, "textures/models/armor/pilot_goggles_" + this.materialType + ".png").toString();
    }


    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level levelIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(TextUtils.translate("tooltip", "pilot_goggles"));
    }
}
