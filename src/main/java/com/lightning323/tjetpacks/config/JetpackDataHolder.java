package com.lightning323.tjetpacks.config;

import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

import java.util.HashMap;
import java.util.Map;

import static com.lightning323.tjetpacks.ThermalJetpacks.MOD_ID;


public class JetpackDataHolder {

    public static Map<String, JetpackDataHolder> DEFAULTS = new HashMap<>();

    public String name;
    public String mod;

    // actual values
    public int energyCapacity;
    public int energyUsage;
    public int energyPerTickIn;
    public int energyPerTickOut;
    public int armorReduction;
    public int armorEnergyPerHit;
    public int enchantability;
    public double speedVertical;
    public double accelVertical;
    public double speedVerticalHover;
    public double speedVerticalHoverSlow;
    public double speedSideways;
    public double sprintSpeedModifier;
    public double sprintEnergyModifier;
    public boolean hoverMode;
    public boolean emergencyHoverMode;
    public boolean chargerMode;

    // config values
    public IntValue _energyCapacity;
    public IntValue _energyUsage;
    public IntValue _energyPerTickIn;
    public IntValue _energyPerTickOut;
    public IntValue _armorReduction;
    public IntValue _armorEnergyPerHit;
    public IntValue _enchantability;
    public DoubleValue _speedVertical;
    public DoubleValue _accelVertical;
    public DoubleValue _speedVerticalHover;
    public DoubleValue _speedVerticalHoverSlow;
    public DoubleValue _speedSideways;
    public DoubleValue _sprintSpeedModifier;
    public DoubleValue _sprintEnergyModifier;
    public BooleanValue _hoverMode;
    public BooleanValue _emergencyHoverMode;
    public BooleanValue _chargerMode;

    public JetpackDataHolder(String name, String mod) {
        this.name = name;
        this.mod = mod;
        DEFAULTS.put(name, this);
    }

    static {
//        JetpackDataHolder d = new JetpackDataHolder("potato", "" + MOD_ID + "");
//        d.energyCapacity = 1200;
//        d.energyUsage = 45;
//        d.energyPerTickOut = 45;
//        d.energyPerTickIn = 45;
//        d.armorReduction = 0;
//        d.armorEnergyPerHit = 0;
//        d.enchantability = 0;
//        d.speedVertical = 0.9D;
//        d.accelVertical = 0.5D;
//        d.speedVerticalHover = 0.18D;
//        d.speedVerticalHoverSlow = 0.14D;
//        d.speedSideways = 0.0D;
//        d.sprintSpeedModifier = 1.0D;
//        d.sprintEnergyModifier = 1.0D;
//        d.hoverMode = false;
//        d.emergencyHoverMode = false;
//        d.chargerMode = false;

        JetpackDataHolder d = new JetpackDataHolder("creative", "" + MOD_ID + "");
        d.energyCapacity = 0;
        d.energyUsage = 1000;
        d.energyPerTickOut = 1000;
        d.energyPerTickIn = 0;
        d.armorReduction = 12;
        d.enchantability = 20;
        d.speedVertical = 0.9D;
        d.accelVertical = 0.15D;
        d.speedVerticalHover = 0.45D;
        d.speedVerticalHoverSlow = 0.0D;
        d.speedSideways = 0.21D;
        d.sprintSpeedModifier = 2.5D;
        d.hoverMode = true;
        d.emergencyHoverMode = true;
        d.chargerMode = false;

        //--------------------------------------------------------------------

        d = new JetpackDataHolder("te1", "thermal");
        d.energyCapacity = 80000;
        d.energyUsage = 32;
        d.energyPerTickIn = 1500;
        d.armorReduction = 2;
        d.armorEnergyPerHit = 80;
        d.enchantability = 3;
        d.speedVertical = 0.09D;
        d.accelVertical = 0.1D;
        d.speedVerticalHover = 0.18D;
        d.speedVerticalHoverSlow = 0.14D;
        d.speedSideways = 0.0D;
        d.sprintSpeedModifier = 1.0D;
        d.sprintEnergyModifier = 1.0D;
        d.hoverMode = false;
        d.emergencyHoverMode = false;
        d.chargerMode = false;

        d = new JetpackDataHolder("te2", "thermal");
        d.energyCapacity = 400000;
        d.energyUsage = 50;
        d.energyPerTickIn = 8000;
        d.armorReduction = 4;
        d.armorEnergyPerHit = 80;
        d.enchantability = 8;
        d.speedVertical = 0.18D;
        d.accelVertical = 0.1D;
        d.speedVerticalHover = 0.18D;
        d.speedVerticalHoverSlow = 0.1D;
        d.speedSideways = 0.0D;
        d.sprintSpeedModifier = 1.0D;
        d.sprintEnergyModifier = 1.5D;
        d.hoverMode = true;
        d.emergencyHoverMode = false;
        d.chargerMode = false;

        d = new JetpackDataHolder("te3", "thermal");
        d.energyCapacity = 2400000;
        d.energyUsage = 200;
        d.energyPerTickIn = 15000;
        d.armorReduction = 6;
        d.armorEnergyPerHit = 120;
        d.enchantability = 13;
        d.speedVertical = 0.25D;
        d.accelVertical = 0.1D;
        d.speedVerticalHover = 0.34D;
        d.speedVerticalHoverSlow = 0.03D;
        d.speedSideways = 0.05D;
        d.sprintSpeedModifier = 1.1D;
        d.sprintEnergyModifier = 2.3D;
        d.hoverMode = true;
        d.emergencyHoverMode = true;
        d.chargerMode = false;

        d = new JetpackDataHolder("te4", "thermal");
        d.energyCapacity = 80000000;
        d.energyUsage = 850;
        d.energyPerTickIn = 20000;
        d.armorReduction = 8;
        d.armorEnergyPerHit = 160;
        d.enchantability = 16;
        d.speedVertical = 0.48D;
        d.accelVertical = 0.14D;
        d.speedVerticalHover = 0.4D;
        d.speedVerticalHoverSlow = 0.005D;
        d.speedSideways = 0.1D;
        d.sprintSpeedModifier = 1.3D;
        d.sprintEnergyModifier = 4.0D;
        d.hoverMode = true;
        d.emergencyHoverMode = true;
        d.chargerMode = false;

        d = new JetpackDataHolder("te5", "thermal");
        d.energyCapacity = 160000000;
        d.energyUsage = 1600;
        d.energyPerTickIn = 30000;
        d.energyPerTickOut = 30000;
        d.armorReduction = 12;
        d.armorEnergyPerHit = 240;
        d.enchantability = 18;
        d.speedVertical = 0.5D;
        d.accelVertical = 0.14D;
        d.speedVerticalHover = 0.4D;
        d.speedVerticalHoverSlow = 0.005D;
        d.speedSideways = 0.12D;
        d.sprintSpeedModifier = 1.3D;
        d.sprintEnergyModifier = 4.0D;
        d.hoverMode = true;
        d.emergencyHoverMode = true;
        d.chargerMode = false;

        d = new JetpackDataHolder("te6", "thermal");
        d.energyCapacity = 50000000;
        d.energyUsage = 850;
        d.energyPerTickIn = 30000;
        d.energyPerTickOut = 30000;
        d.armorReduction = 12;
        d.armorEnergyPerHit = 4500;
        d.enchantability = 20;
        d.speedVertical = 0.9D;
        d.accelVertical = 0.15D;
        d.speedVerticalHover = 0.45D;
        d.speedVerticalHoverSlow = 0.0D;
        d.speedSideways = 0.2D;
        d.sprintSpeedModifier = 2.4D;
        d.sprintEnergyModifier = 6.0D;
        d.hoverMode = true;
        d.emergencyHoverMode = true;
        d.chargerMode = false;

    }
}
