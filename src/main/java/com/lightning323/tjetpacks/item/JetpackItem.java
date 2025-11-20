package com.lightning323.tjetpacks.item;

import com.lightning323.tjetpacks.I_ServerGamePacketListenerImpl;
import com.lightning323.tjetpacks.RegistryHandler;
import com.lightning323.tjetpacks.client.JetpackModelLayers;
import com.lightning323.tjetpacks.client.ui.IHUDInfoProvider;
import com.lightning323.tjetpacks.energy.EnergyStorageImpl;
import com.lightning323.tjetpacks.energy.IEnergyContainer;
import com.lightning323.tjetpacks.handlers.CommonJetpackHandler;
import com.lightning323.tjetpacks.util.*;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class JetpackItem extends ArmorItem implements IHUDInfoProvider, IEnergyContainer {

    private final JetpackType jetpackType;
    public final int tier;
    public final boolean isCreative;

    public JetpackItem(JetpackType jetpackType, boolean isCreative) {
        super(jetpackType.isArmored() ? JetpackArmorMaterial.JETPACK_ARMORED : JetpackArmorMaterial.JETPACK, Type.CHESTPLATE,
                new Properties().setNoRepair());

        this.isCreative = isCreative;
        this.jetpackType = jetpackType;
        this.tier = jetpackType.getTier();
    }

    public JetpackItem(JetpackType jetpackType, JetpackArmorMaterial material, boolean isCreative) {
        super(material, Type.CHESTPLATE, new Properties());
        this.isCreative = isCreative;
        this.jetpackType = jetpackType;
        this.tier = jetpackType.getTier();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(Rendering.INSTANCE);
    }

    @OnlyIn(Dist.CLIENT)
    private static class Rendering implements IClientItemExtensions {
        private static final Rendering INSTANCE = new Rendering();

        private Rendering() {
        }

        @Override
        public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
            return JetpackModelLayers.JETPACK_MODEL;
        }
    }

    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return this.jetpackType.getArmorTexture();
    }

    boolean inSlot = false;


    //TODO: Could this be optimized?
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity holder, int vanillaIndex, boolean selected) {
        if (holder instanceof Player player
                && !player.isSpectator()
                && !player.getAbilities().flying) {

            //Check every 20 ticks to make sure the player is still wearing the jetpack
            if (level.getGameTime() % 20 != 0)
                inSlot = JetpackUtil.checkTickForEquippedSlot(vanillaIndex, stack, player);
            if (!inSlot) return;


            flyUser(player, stack, this, false);
            if (this.jetpackType.getChargerMode() && this.isChargerOn(stack)) {
                chargeInventory(player, stack);
            }
        }

        super.inventoryTick(stack, level, holder, vanillaIndex, selected);
    }

    public JetpackType getJetpackType() {
        return jetpackType;
    }


    @Override
    public int getEnchantmentValue() {
        return super.getEnchantmentValue() + jetpackType.getEnchantability();
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return super.isFoil(stack) || isCreative;
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return jetpackType.getRarity();
    }


    public boolean isEngineOn(ItemStack stack) {
        return NBTUtil.getBoolean(stack, Constants.TAG_ENGINE);
    }

    public void setEngineOn(ItemStack stack, Player player, boolean engineOn) {
        NBTUtil.flipBoolean(stack, Constants.TAG_ENGINE);
        Component msg = TextUtils.getStateToggle("engineMode", engineOn);
        player.displayClientMessage(msg, true);
    }

    public boolean isHoverOn(ItemStack stack) {
        return NBTUtil.getBoolean(stack, Constants.TAG_HOVER);
    }

    public void setHoverOn(ItemStack stack, Player player, boolean hoverOn) {
        if (jetpackType.getHoverMode()) {
            NBTUtil.flipBoolean(stack, Constants.TAG_HOVER);
            Component msg = TextUtils.getStateToggle("hoverMode", hoverOn);
            player.displayClientMessage(msg, true);
        }
    }

    public boolean isEHoverOn(ItemStack stack) {
        return NBTUtil.getBoolean(stack, Constants.TAG_E_HOVER);
    }

    public void setEHoverOn(ItemStack stack, Player player, boolean eHoverOn) {
        if (jetpackType.getEmergencyHoverMode()) {
            NBTUtil.flipBoolean(stack, Constants.TAG_E_HOVER);
            Component msg = TextUtils.getStateToggle("emergencyHoverMode", eHoverOn);
            player.displayClientMessage(msg, true);
        }
    }

    private void doEHover(ItemStack stack, Player player) {
        if (jetpackType.getHoverMode()) {
            NBTUtil.setBoolean(stack, Constants.TAG_ENGINE, true);
            NBTUtil.setBoolean(stack, Constants.TAG_HOVER, true);
            Component msg = TextUtils.getEmergencyText();
            player.displayClientMessage(msg, true);
        }
    }

    public boolean isChargerOn(ItemStack stack) {
        return NBTUtil.getBoolean(stack, Constants.TAG_CHARGER);
    }

    public void setChargerOn(ItemStack stack, Player player, boolean chargerOn) {
        if (jetpackType.getChargerMode()) {
            NBTUtil.flipBoolean(stack, Constants.TAG_CHARGER);
            Component msg = TextUtils.getStateToggle("chargerMode", chargerOn);
            player.displayClientMessage(msg, true);
        }
    }

    public static float getChargeRatio(ItemStack stack) {
        LazyOptional<IEnergyStorage> optional = stack.getCapability(ForgeCapabilities.ENERGY);
        if (optional.isPresent()) {
            IEnergyStorage energyStorage = optional.orElseThrow(IllegalStateException::new);
            return (float) energyStorage.getEnergyStored() / energyStorage.getMaxEnergyStored();
        }
        return 0;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
        IEnergyContainer container = this;
        return new ICapabilityProvider() {
            @Nonnull
            @Override
            public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
                if (cap == ForgeCapabilities.ENERGY)
                    return LazyOptional.of(() -> new EnergyStorageImpl(stack, container)).cast();
                return LazyOptional.empty();
            }
        };
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level levelIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (ForgeCapabilities.ENERGY == null) return;
        TextUtils.addBaseInfo(stack, tooltip);
        if (KeyboardUtil.isHoldingShift()) {
            TextUtils.addShiftInfo(stack, tooltip);
        } else {
            tooltip.add(TextUtils.getShiftText());
        }
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return !isCreative && getEnergy(stack) > 0;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round(13.0F * getChargeRatio(stack));
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0x03fc49;
    }

    public ItemStack asChargedCopy() {
        ItemStack full = new ItemStack(this);
        NBTUtil.setInt(full, Constants.TAG_ENERGY, jetpackType.getEnergyCapacity());
        return full;
    }

    public static int clamp(int min, int value, int max) {
        if (value < min) return min;
        return Math.min(value, max);
    }

    private void setEnergyStored(ItemStack container, int value) {
        NBTUtil.setInt(container, Constants.TAG_ENERGY, clamp(value, 0, getCapacity(container)));
    }

    public int getEnergyReceive() {
        return jetpackType.getEnergyPerTickIn();
    }

    public int getEnergyExtract() {
        return jetpackType.getEnergyUsage();
    }


    public static int getParticleId(ItemStack stack) {
        return stack.getOrCreateTag().contains(Constants.TAG_PARTICLE) ? stack.getOrCreateTag().getInt(Constants.TAG_PARTICLE) : JetpackType.getDefaultParticles(stack);
    }

    public void setThrottle(ItemStack stack, int value) {
        NBTUtil.setInt(stack, Constants.TAG_THROTTLE, value);
    }

    public int getThrottle(ItemStack stack) {
        return stack.getOrCreateTag().contains(Constants.TAG_THROTTLE) ? stack.getOrCreateTag().getInt(Constants.TAG_THROTTLE) : 100;
    }

    public void useEnergy(ItemStack container, int amount) {
        if (container.getTag() != null || container.getTag().contains(Constants.TAG_ENERGY)) {
            int stored = Math.min(container.getTag().getInt(Constants.TAG_ENERGY), getCapacity(container));
            stored -= amount;
            if (stored < 0) stored = 0;
            container.getTag().putInt(Constants.TAG_ENERGY, stored);
        }
    }

    public int getEnergyUsage(ItemStack stack) {
        int baseUsage = jetpackType.getEnergyUsage();
        int level = EnchantmentHelper.getItemEnchantmentLevel(RegistryHandler.FUEL_EFFICIENCY.get(), stack);
        return level != 0 ? (int) Math.round(baseUsage * (5 - level) / 5.0D) : baseUsage;
    }

    public void chargeInventory(Player player, ItemStack stack) {
        if (!player.getCommandSenderWorld().isClientSide) {
            if (getEnergy(stack) > 0 || isCreative) {
                // Charge hands
                for (ItemStack itemStack : player.getHandSlots()) {
                    charge(stack, itemStack);
                }
                // Charge equipment
                for (ItemStack itemStack : player.getArmorSlots()) {
                    charge(stack, itemStack);
                }
            }
        }
    }

    private void charge(ItemStack jetpack, ItemStack item) {
        if (!item.equals(jetpack) && item.getCapability(ForgeCapabilities.ENERGY).isPresent()) {
            LazyOptional<IEnergyStorage> optional = item.getCapability(ForgeCapabilities.ENERGY);
            if (optional.isPresent()) {
                IEnergyStorage energyStorage = optional.orElseThrow(IllegalStateException::new);
                if (isCreative) {
                    energyStorage.receiveEnergy(1000, false);
                } else {
                    useEnergy(jetpack, energyStorage.receiveEnergy(getEnergyUsage(jetpack), false));
                }
            }
        }
    }

    private void fly(Player player, double y) {
        Vec3 motion = player.getDeltaMovement();
        player.setDeltaMovement(motion.get(Direction.Axis.X), y, motion.get(Direction.Axis.Z));
    }

    final float HOVER_FALL = 0.15f;

    @Override
    public boolean isEnchantable(ItemStack itemStack) {
        return itemStack.getItem() instanceof JetpackItem;
    }

    boolean hoverMode;
    double hoverSpeed;
    boolean flyKeyDown;
    boolean descendKeyDown;
    double currentAccel;
    double currentSpeedVertical;
    double speedVerticalHover;
    double speedVerticalHoverSlow;

    public void flyUser(Player player, ItemStack stack, JetpackItem item, Boolean force) {
        if (isEngineOn(stack)) {
            hoverMode = isHoverOn(stack);
            hoverSpeed = CommonJetpackHandler.isInverted(player) == CommonJetpackHandler.isHoldingDown(player) ? jetpackType.getSpeedVerticalHoverSlow() : jetpackType.getSpeedVerticalHover();
            flyKeyDown = force || CommonJetpackHandler.isHoldingUp(player);
            descendKeyDown = CommonJetpackHandler.isHoldingDown(player);
            currentAccel = jetpackType.getAccelVertical() * (player.getDeltaMovement().get(Direction.Axis.Y) < 0.3D ? 2.5D : 1.0D);
            currentSpeedVertical = jetpackType.getSpeedVertical() * (player.isInWater() ? 0.4D : 1.0D);
            speedVerticalHover = jetpackType.getSpeedVerticalHover();
            speedVerticalHoverSlow = jetpackType.getSpeedVerticalHoverSlow();

            if ((flyKeyDown || hoverMode && !player.onGround())) {
                if (!isCreative) {
                    int amount = (int) (player.isSprinting() ? Math.round(getEnergyUsage(stack) * jetpackType.getSprintEnergyModifier()) : getEnergyUsage(stack));
                    useEnergy(stack, amount);
                }
                if (getEnergy(stack) > 0 || isCreative) {
                    if (flyKeyDown) {
                        if (!hoverMode) {
                            fly(player, Math.min(player.getDeltaMovement().get(Direction.Axis.Y) + currentAccel, currentSpeedVertical));
                        } else {
                            if (descendKeyDown) {
                                fly(player, Math.min(player.getDeltaMovement().get(Direction.Axis.Y) + currentAccel, -speedVerticalHoverSlow));
                            } else {
                                fly(player, Math.min(player.getDeltaMovement().get(Direction.Axis.Y) + currentAccel, speedVerticalHover - HOVER_FALL));
                            }
                        }
                    } else {
                        fly(player, Math.min(player.getDeltaMovement().get(Direction.Axis.Y) + currentAccel, -hoverSpeed));
                    }

                    double baseSpeedSideways = jetpackType.getSpeedSideways();
                    double sprintSpeedModifier = jetpackType.getSprintSpeedModifier();
                    float speedSideways = (float) (player.isCrouching() ? baseSpeedSideways * 0.5F : baseSpeedSideways) * (getThrottle(stack) / 100.0F);
                    float speedForward = (float) (player.isSprinting() ? speedSideways * sprintSpeedModifier : speedSideways) * (getThrottle(stack) / 100.0F);

                    if (CommonJetpackHandler.isHoldingForwards(player)) {
                        player.moveRelative(1, new Vec3(0, 0, speedForward));
                    }
                    if (CommonJetpackHandler.isHoldingBackwards(player)) {
                        player.moveRelative(1, new Vec3(0, 0, -speedSideways * 0.8F));
                    }
                    if (CommonJetpackHandler.isHoldingLeft(player)) {
                        player.moveRelative(1, new Vec3(speedSideways, 0, 0));
                    }
                    if (CommonJetpackHandler.isHoldingRight(player)) {
                        player.moveRelative(1, new Vec3(-speedSideways, 0, 0));
                    }
                    if (!player.getCommandSenderWorld().isClientSide()) {
                        player.fallDistance = 0.0F;
                        if (player instanceof ServerPlayer) {
                            I_ServerGamePacketListenerImpl mixinObj = (I_ServerGamePacketListenerImpl) ((ServerPlayer) player).connection;
                            mixinObj.setAboveGroundTickCount(0);
                        }
                    }
                }
            }
        }
        if (!player.getCommandSenderWorld().isClientSide && this.isEHoverOn(stack)) {
            if ((item.getEnergy(stack) > 0 || this.isCreative) && (!this.isHoverOn(stack) || !this.isEngineOn(stack))) {
                if (player.position().get(Direction.Axis.Y) < -69) {
                    this.doEHover(stack, player);
                } else {
                    if (!player.isCreative() && player.fallDistance - 1.2F >= player.getHealth()) {
                        for (int j = 0; j <= 16; j++) {
                            if (!player.onGround() && !player.isSwimming()) {
                                this.doEHover(stack, player);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addHUDInfo(ItemStack stack, List<Component> list) {
        TextUtils.addHUDInfoText(stack, list);
    }

    @Override
    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
        if (getEnergyReceive() == 0) return 0;
        int energyStored = getEnergy(container);
        int energyReceived = Math.min(getCapacity(container) - energyStored, Math.min(getEnergyReceive(), maxReceive));
        if (!simulate) setEnergyStored(container, energyStored + energyReceived);
        return energyReceived;
    }

    @Override
    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
        if (getEnergyExtract() == 0) return 0;
        int energyStored = getEnergy(container);
        int energyExtracted = Math.min(energyStored, Math.min(getEnergyExtract(), maxExtract));
        if (!simulate) setEnergyStored(container, energyStored - energyExtracted);
        return energyExtracted;
    }

    @Override
    public int getEnergy(ItemStack container) {
        return container.getOrCreateTag().getInt(Constants.TAG_ENERGY);
    }

    @Override
    public int getCapacity(ItemStack container) {
        return jetpackType.getEnergyCapacity();
    }

}
