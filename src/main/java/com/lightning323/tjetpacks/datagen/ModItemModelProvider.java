package com.lightning323.tjetpacks.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import static com.lightning323.tjetpacks.ThermalJetpacks.MOD_ID;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn.getPackOutput(), MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ModelFile itemGenerated = getExistingFile(mcLoc("item/generated"));

        builder(itemGenerated, "leather_strap");
        builder(itemGenerated, "pilot_goggles_gold");
        builder(itemGenerated, "pilot_goggles_iron");

        builder(itemGenerated, "jetpack_potato");
        builder(itemGenerated, "jetpack_creative");
        builder(itemGenerated, "jetpack_creative_armored");

        builder(itemGenerated, "jetpack_te1");
        builder(itemGenerated, "jetpack_te1_armored");
        builder(itemGenerated, "jetpack_te2");
        builder(itemGenerated, "jetpack_te2_armored");
        builder(itemGenerated, "jetpack_te3");
        builder(itemGenerated, "jetpack_te3_armored");
        builder(itemGenerated, "jetpack_te4");
        builder(itemGenerated, "jetpack_te4_armored");
        builder(itemGenerated, "jetpack_te5");
        builder(itemGenerated, "jetpack_te5_enderium");

        builder(itemGenerated, "armorplating_te1");
        builder(itemGenerated, "armorplating_te2");
        builder(itemGenerated, "armorplating_te3");
        builder(itemGenerated, "armorplating_te4");
        builder(itemGenerated, "armorplating_te5");
        builder(itemGenerated, "armorplating_te5_enderium");

        builder(itemGenerated, "thruster_te1");
        builder(itemGenerated, "thruster_te2");
        builder(itemGenerated, "thruster_te3");
        builder(itemGenerated, "thruster_te4");
        builder(itemGenerated, "thruster_te5");

        builder(itemGenerated, "cryogenic_crystal");
        builder(itemGenerated, "flux_chestplate");
    }

    private ItemModelBuilder builder(ModelFile itemGenerated, String name) {
        return getBuilder(name).parent(itemGenerated).texture("layer0", "item/" + name);
    }
}
