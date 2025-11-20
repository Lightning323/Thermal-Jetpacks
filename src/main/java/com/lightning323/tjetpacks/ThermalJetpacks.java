package com.lightning323.tjetpacks;

import com.lightning323.tjetpacks.client.ui.HUDHandler;
import com.lightning323.tjetpacks.config.ModConfig;
import com.lightning323.tjetpacks.handlers.ClientJetpackHandler;
import com.lightning323.tjetpacks.handlers.CommonJetpackHandler;
import com.lightning323.tjetpacks.handlers.KeybindHandler;
import com.lightning323.tjetpacks.integration.CuriosIntegration;
import com.lightning323.tjetpacks.item.JetpackItem;
import com.lightning323.tjetpacks.item.ModItemGroup;
import com.lightning323.tjetpacks.item.PilotGogglesItem;
import com.lightning323.tjetpacks.network.NetworkHandler;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.curios.api.CuriosCapability;

@Mod(ThermalJetpacks.MOD_ID)
public class ThermalJetpacks {
    public static final String MOD_ID = "tjetpacks";
    /*
    Creative Tab: minecraft:building_blocks
Creative Tab: minecraft:natural_blocks
Creative Tab: minecraft:hotbar
Creative Tab: thermal:thermal.items
Creative Tab: minecraft:search
Creative Tab: thermal:thermal.foods
Creative Tab: minecraft:combat
Creative Tab: minecraft:colored_blocks
Creative Tab: thermal:thermal.blocks
Creative Tab: thermal:thermal.tools
Creative Tab: minecraft:inventory
Creative Tab: minecraft:spawn_eggs
Creative Tab: minecraft:op_blocks
Creative Tab: minecraft:food_and_drinks
Creative Tab: minecraft:ingredients
Creative Tab: minecraft:functional_blocks
Creative Tab: thermal:thermal.devices
Creative Tab: minecraft:tools_and_utilities
Creative Tab: tjetpacks:tjetpacks.main
Creative Tab: minecraft:redstone_blocks
Tab ID: minecraft:building_blocks
Tab Title: Building Blocks
Tab ID: minecraft:colored_blocks
Tab Title: Colored Blocks
Tab ID: minecraft:natural_blocks
Tab Title: Natural Blocks
Tab ID: minecraft:functional_blocks
Tab Title: Functional Blocks
Tab ID: minecraft:redstone_blocks
Tab Title: Redstone Blocks
Tab ID: minecraft:hotbar
Tab Title: Saved Hotbars
Tab ID: minecraft:search
Tab Title: Search Items
Tab ID: minecraft:tools_and_utilities
Tab Title: Tools & Utilities
Tab ID: minecraft:combat
Tab Title: Combat
Tab ID: minecraft:food_and_drinks
Tab Title: Food & Drinks
Tab ID: minecraft:ingredients
Tab Title: Ingredients
Tab ID: minecraft:spawn_eggs
Tab Title: Spawn Eggs
Tab ID: minecraft:op_blocks
Tab Title: Operator Utilities
Tab ID: minecraft:inventory
Tab Title: Survival Inventory
Tab ID: thermal:thermal.blocks
Tab Title: itemGroup.thermal.blocks
Tab ID: thermal:thermal.devices
Tab Title: itemGroup.thermal.devices
Tab ID: thermal:thermal.foods
Tab Title: itemGroup.thermal.foods
Tab ID: thermal:thermal.items
Tab Title: itemGroup.thermal.items
Tab ID: thermal:thermal.tools
Tab Title: itemGroup.thermal.tools
Tab ID: tjetpacks:tjetpacks.main
Tab Title: itemGroup.tjetpacks.main
     */

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);


    public static final Logger LOGGER = LogManager.getLogger();

    public ThermalJetpacks() {

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);


        var bus = FMLJavaModLoadingContext.get().getModEventBus();

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            bus.register(new HUDHandler());
        });

        // TODO: fix this.
        if (ModList.get().isLoaded("curios")) {
            MinecraftForge.EVENT_BUS.addGenericListener(ItemStack.class, this::attachCapabilities);
        }

        MinecraftForge.EVENT_BUS.register(this);
//        MinecraftForge.EVENT_BUS.register(new JetpackCraftingEvents());
        MinecraftForge.EVENT_BUS.register(new CommonJetpackHandler());
//        MinecraftForge.EVENT_BUS.register(new ModSounds());

        // Register the item to a creative tab
        bus.addListener(ThermalJetpacks::addCreative);

        ModConfig.register();
        RegistryHandler.init();
    }

    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == BuiltInRegistries.CREATIVE_MODE_TAB.get(new ResourceLocation("thermal", "thermal.tools"))) {
            System.out.println("Adding jetpacks to creative tab");
            ModItemGroup.registerItems(event);
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Common Setup Method registered.");
        NetworkHandler.registerMessages();
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        //Get all creative tabs
//        Registry<CreativeModeTab> tabRegistry = BuiltInRegistries.CREATIVE_MODE_TAB;
//        for (ResourceKey<CreativeModeTab> key : tabRegistry.registryKeySet()) {
//            System.out.println("Creative Tab: " + key.location());
//        }
//        for (CreativeModeTab tab : BuiltInRegistries.CREATIVE_MODE_TAB) {
//            System.out.println("Tab ID: " + BuiltInRegistries.CREATIVE_MODE_TAB.getKey(tab));
//            System.out.println("Tab Title: " + tab.getDisplayName().getString());
//        }

        //Check if the target tab exists, if not, register a custom tab
        ResourceLocation targetTabId = new ResourceLocation("thermal", "thermal.tools");
        boolean tabExists = BuiltInRegistries.CREATIVE_MODE_TAB.containsKey(targetTabId);
        if (!tabExists) {
            CREATIVE_TAB.register(MOD_ID + ".main", ModItemGroup::new);
            CREATIVE_TAB.register(FMLJavaModLoadingContext.get().getModEventBus());
        }

        LOGGER.info("Client Setup Method registered.");

        MinecraftForge.EVENT_BUS.register(new KeybindHandler());
        MinecraftForge.EVENT_BUS.register(new ClientJetpackHandler());
        MinecraftForge.EVENT_BUS.register(new HUDHandler());

        if (ModList.get().isLoaded("curios")) {
            CuriosIntegration.initRenderers();
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Server starting...");
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        LOGGER.info("Server stopping...");
        CommonJetpackHandler.clear();
    }

    @SubscribeEvent
    public void onPlayerLogin(final PlayerEvent.PlayerLoggedInEvent loggedInEvent) {
        ThermalJetpacks.LOGGER.info("{} logging in. Syncing server jetpack configs with client.", loggedInEvent.getEntity().getName().getString());
        ModConfig.sendServerConfigFiles(loggedInEvent.getEntity());
        ThermalJetpacks.LOGGER.info("Finished syncing server jetpack configs.");
    }

    private void attachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        if (!ModList.get().isLoaded("curios")) {
            return;
        }
        ItemStack stack = event.getObject();
        if (stack.getItem() instanceof JetpackItem) {
            event.addCapability(CuriosCapability.ID_ITEM, CuriosIntegration.initJetpackCapabilities(stack));
        }
        if (stack.getItem() instanceof PilotGogglesItem) {
            event.addCapability(CuriosCapability.ID_ITEM, CuriosIntegration.initGogglesCapabilities(stack));
        }
    }
}
