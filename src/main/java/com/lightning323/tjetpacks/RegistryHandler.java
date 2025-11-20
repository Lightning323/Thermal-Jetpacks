package com.lightning323.tjetpacks;

import com.lightning323.tjetpacks.enchantment.EnchantmentFuelEfficiency;
import com.lightning323.tjetpacks.item.JetpackArmorMaterial;
import com.lightning323.tjetpacks.item.JetpackItem;
import com.lightning323.tjetpacks.item.JetpackType;
import com.lightning323.tjetpacks.item.PilotGogglesItem;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.lightning323.tjetpacks.ThermalJetpacks.MOD_ID;

public class RegistryHandler {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MOD_ID);
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, MOD_ID);
    public static final DeferredRegister<MenuType<?>> MENU = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MOD_ID);

    //Sounds
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MOD_ID);
//    public static final SoundEvent JETPACK = SoundEvent.createVariableRangeEvent(new ResourceLocation(MOD_ID, "jetpack"));
//    public static final SoundEvent JETPACK_OTHER = SoundEvent.createVariableRangeEvent(new ResourceLocation(MOD_ID, "jetpack_other"));
//    public static final SoundEvent ROCKET = SoundEvent.createVariableRangeEvent(new ResourceLocation(MOD_ID, "rocket"));
    public static final RegistryObject<SoundEvent> SOUND_JETPACK = SOUNDS.register("jetpack", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MOD_ID, "jetpack")));
    public static final RegistryObject<SoundEvent> SOUND_JETPACK_OTHER = SOUNDS.register("jetpack_other", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MOD_ID, "jetpack_other")));
    public static final RegistryObject<SoundEvent> SOUND_ROCKET = SOUNDS.register("rocket", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MOD_ID, "rocket")));


    public static void init() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ENCHANTMENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
        RECIPE_SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        PARTICLE_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        MENU.register(FMLJavaModLoadingContext.get().getModEventBus());
        SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    // Particles:
//    public static final RegistryObject<SimpleParticleType> RAINBOW_PARTICLE = PARTICLE_TYPES.register("rainbow_particle", () -> new SimpleParticleType(false));
    // Recipes:
//    public static final RegistryObject<RecipeSerializer<JetpackCustomRecipe>> JETPACK_CUSTOM_RECIPE = RECIPE_SERIALIZERS.register("jetpack_custom_recipe",() -> new SimpleCraftingRecipeSerializer<>((location, category) -> new JetpackCustomRecipe(location)));

    // Enchantments:
    public static final EnchantmentCategory JETPACK_ENCHANTMENT_TYPE = EnchantmentCategory.create("JETPACK", (item -> item instanceof JetpackItem));
    public static final RegistryObject<EnchantmentFuelEfficiency> FUEL_EFFICIENCY = ENCHANTMENTS.register("fuel_efficiency", EnchantmentFuelEfficiency::new);

    public static final RegistryObject<PilotGogglesItem> PILOT_GOGGLES_GOLD = ITEMS.register("pilot_goggles_gold", () -> new PilotGogglesItem("gold"));
    public static final RegistryObject<PilotGogglesItem> PILOT_GOGGLES_IRON = ITEMS.register("pilot_goggles_iron", () -> new PilotGogglesItem("iron"));
    public static final RegistryObject<Item> LEATHER_STRAP = ITEMS.register("leather_strap", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CRYOGENIC_CRYSTAL = ITEMS.register("cryogenic_crystal", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COMBUSTION_CHAMBER = ITEMS.register("combustion_chamber", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COMBUSTION_CHAMBER_HARD = ITEMS.register("combustion_chamber_hard", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> THRUSTER_TE1 = ITEMS.register("thruster_te1", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> THRUSTER_TE2 = ITEMS.register("thruster_te2", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> THRUSTER_TE3 = ITEMS.register("thruster_te3", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> THRUSTER_TE4 = ITEMS.register("thruster_te4", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FLUX_CHESTPLATE = ITEMS.register("flux_chestplate", () -> new Item(new Item.Properties()));

    public static final RegistryObject<JetpackItem> JETPACK_TE1 = ITEMS.register("jetpack_te1", () -> new JetpackItem(JetpackType.TE1, false));
    public static final RegistryObject<JetpackItem> JETPACK_TE2 = ITEMS.register("jetpack_te2", () -> new JetpackItem(JetpackType.TE2, false));
    public static final RegistryObject<JetpackItem> JETPACK_TE3 = ITEMS.register("jetpack_te3", () -> new JetpackItem(JetpackType.TE3, false));
    public static final RegistryObject<JetpackItem> JETPACK_TE4 = ITEMS.register("jetpack_te4", () -> new JetpackItem(JetpackType.TE4, false));
    public static final RegistryObject<JetpackItem> JETPACK_TE5_ARMORED = ITEMS.register("jetpack_te5_enderium", () -> new JetpackItem(JetpackType.TE5_ARMORED, JetpackArmorMaterial.JETPLATE, false));
    public static final RegistryObject<JetpackItem> JETPACK_CREATIVE_ARMORED = ITEMS.register("jetpack_creative_armored", () -> new JetpackItem(JetpackType.CREATIVE_ARMORED, true));

}
