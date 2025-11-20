package com.lightning323.tjetpacks.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Lazy;

import java.util.function.Supplier;

import static com.lightning323.tjetpacks.ThermalJetpacks.MOD_ID;

public enum JetpackArmorMaterial implements ArmorMaterial {

	PILOT_GOGGLES("pilot_goggles", 0, new int[] {0, 0, 0, 0}, 0, () -> Ingredient.of(Items.LEATHER), "item.armor.equip_leather", 0.0f, 0.0f),
	JETPACK("jetpack", 0, new int[] {0, 2, 0, 0}, 10, () -> Ingredient.of(Items.IRON_INGOT), "item.armor.equip_iron", 0.0f, 0.0f),
	JETPACK_ARMORED("jetpack_armored", 0, new int[] {0, 4, 0, 0}, 10, () -> Ingredient.of(Items.IRON_INGOT), "item.armor.equip_iron", 0.0f, 0.0f),
	JETPLATE("jetplate", 0, new int[] {0, 12, 0, 0}, 10, () -> Ingredient.of(Items.IRON_INGOT), "item.armor.equip_iron", 3.0f, 3.0f);

	private final String name;
	private final int durability;
	private final int[] damageReductionAmounts;
	private int enchantability;
	private final Lazy<Ingredient> repairIngredient;
	private final String equipSound;
	private final float toughness;
	private final float knockbackResistance;
	private static final int[] max_damage_array = new int[] {13, 15, 16, 11};

	JetpackArmorMaterial(String name, int durability, int[] defense, int enchantability,
						 Supplier<Ingredient> repairIngredient, String equipSound, float toughness, float knockbackResistance) {
		this.name = name;
		this.durability = durability;
		this.damageReductionAmounts = defense;
		this.enchantability = enchantability;
		this.repairIngredient = Lazy.of(repairIngredient);
		this.equipSound = equipSound;
		this.toughness = toughness;
		this.knockbackResistance = knockbackResistance;
	}

	// Outdated: uses non-both slot logic
	public static void setStats(JetpackArmorMaterial armor, boolean isArmored, int enchant, int defense) {
		defense = isArmored ? defense : (defense - 1) / 2;
		armor.enchantability = enchant;
		armor.damageReductionAmounts[EquipmentSlot.CHEST.getIndex()] = defense;
	}

	@Override
	public int getDurabilityForType(ArmorItem.Type slotIn) {
		return max_damage_array[slotIn.ordinal()] * this.durability;
	}

	@Override
	public int getDefenseForType(ArmorItem.Type slotIn) {
		return this.damageReductionAmounts[slotIn.ordinal()];
	}

	@Override
	public int getEnchantmentValue() {
		return this.enchantability;
	}

	@Override
	public SoundEvent getEquipSound() {
		return SoundEvent.createVariableRangeEvent(new ResourceLocation(equipSound));
	}

	@Override
	public Ingredient getRepairIngredient() {
		return this.repairIngredient.get();
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public String getName() {
		return MOD_ID + ":" + this.name;
	}

	@Override
	public float getToughness() {
		return this.toughness;
	}

	@Override
	public float getKnockbackResistance() {
		return this.knockbackResistance;
	}
}