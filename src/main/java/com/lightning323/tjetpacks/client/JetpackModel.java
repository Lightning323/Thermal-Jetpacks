package com.lightning323.tjetpacks.client;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.entity.LivingEntity;

import java.io.IOException;
import static com.lightning323.tjetpacks.ThermalJetpacks.MOD_ID;

/**
 * Minecraft only supports resource-based JSON models for blocks/items/entities using specific formats (like block, item, entity),
 * not for custom HumanoidModel-based renderers directly — unless you implement a custom loader (like GeckoLib or your own BakedModel logic).
 *
 * @param <T>
 */
public class JetpackModel<T extends LivingEntity> extends HumanoidModel<T> {

    private final ModelPart middle;
    private final ModelPart leftCanister;
    private final ModelPart rightCanister;
    private final ModelPart leftTip1;
    //    private final ModelPart leftTip2;
    private final ModelPart rightTip1;
    //    private final ModelPart rightTip2;
    private final ModelPart leftExhaust1;
    private final ModelPart leftExhaust2;
    private final ModelPart rightExhaust1;
    private final ModelPart rightExhaust2;

    public JetpackModel(ModelPart model) {
        super(model);
        this.middle = model.getChild("middle");
        this.leftCanister = model.getChild("leftCanister");
        this.rightCanister = model.getChild("rightCanister");
        this.leftTip1 = model.getChild("leftTip1");
//        this.leftTip2 = model.getChild("leftTip2");
        this.rightTip1 = model.getChild("rightTip1");
//        this.rightTip2 = model.getChild("rightTip2");
        this.leftExhaust1 = model.getChild("leftExhaust1");
        this.leftExhaust2 = model.getChild("leftExhaust2");
        this.rightExhaust1 = model.getChild("rightExhaust1");
        this.rightExhaust2 = model.getChild("rightExhaust2");
    }

    final static float yOffset = -1;
    final static float xSeparation = 0.6f;

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = HumanoidModel.createMesh(
                new CubeDeformation(0.8F), //Armor scale
                0F);//Vertical offset

        PartDefinition root = mesh.getRoot();


        //The strap (belt)
        root.addOrReplaceChild("middle", CubeListBuilder.create().mirror()
                .texOffs(0, 54)
                .addBox(-2F, 3F, 3.6F, 4, 5, 2), PartPose.ZERO);


        /**
         * Tip
         */
        root.addOrReplaceChild("leftTip2", CubeListBuilder.create().mirror()
                .texOffs(0, 49)
                .addBox(1.5F + xSeparation, -1F + yOffset, 3.6F, 2, 2, 2), PartPose.ZERO);

        root.addOrReplaceChild("rightTip2", CubeListBuilder.create().mirror()
                .texOffs(17, 49)
                .addBox(-3.5F - xSeparation, -1F + yOffset, 3.6F, 2, 2, 2), PartPose.ZERO);


        /**
         * Tip
         */
        root.addOrReplaceChild("leftTip1", CubeListBuilder.create().mirror()
                .texOffs(0, 45)
                .addBox(1F + xSeparation, 1.5F + yOffset, 3.1F, 3, 1, 3), PartPose.ZERO);

        root.addOrReplaceChild("rightTip1", CubeListBuilder.create().mirror()
                .texOffs(17, 45)
                .addBox(-4F - xSeparation, 1.5F + yOffset, 3.1F, 3, 1, 3), PartPose.ZERO);


        /**
         * Canister
         */
        root.addOrReplaceChild("leftCanister", CubeListBuilder.create().mirror()
                .texOffs(0, 32)
                .addBox(0.5F + xSeparation, 2F + yOffset, 2.6F, 4, 7, 4), PartPose.ZERO);

        root.addOrReplaceChild("rightCanister", CubeListBuilder.create().mirror()
                .texOffs(17, 32)
                .addBox(-4.5F - xSeparation, 2F + yOffset, 2.6F, 4, 7, 4), PartPose.ZERO);

        /**
         * Exaust
         */
        root.addOrReplaceChild("leftExhaust1", CubeListBuilder.create().mirror()
                .texOffs(35, 32)
                .addBox(1F + xSeparation, 9F + yOffset, 3.1F, 3, 1, 3), PartPose.ZERO);


        root.addOrReplaceChild("rightExhaust1", CubeListBuilder.create().mirror()
                .texOffs(48, 32)
                .addBox(-4F - xSeparation, 9F + yOffset, 3.1F, 3, 1, 3), PartPose.ZERO);

        /**
         * Exaust
         */
        root.addOrReplaceChild("leftExhaust2", CubeListBuilder.create().mirror()
                .texOffs(35, 37)
                .addBox(0.5F + xSeparation, 10F + yOffset, 2.6F, 4, 5, 4), PartPose.ZERO);

        root.addOrReplaceChild("rightExhaust2", CubeListBuilder.create().mirror()
                .texOffs(35, 46)
                .addBox(-4.5F - xSeparation, 10F + yOffset, 2.6F, 4, 5, 4), PartPose.ZERO);


        return LayerDefinition.create(mesh, 64, 64);
    }


    @Override
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of();
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        this.middle.copyFrom(this.body);
        this.leftCanister.copyFrom(this.middle);
        this.rightCanister.copyFrom(this.middle);
        this.leftTip1.copyFrom(this.middle);
//        this.leftTip2.copyFrom(this.middle);
        this.rightTip1.copyFrom(this.middle);
//        this.rightTip2.copyFrom(this.middle);
        this.leftExhaust1.copyFrom(this.middle);
        this.leftExhaust2.copyFrom(this.middle);
        this.rightExhaust1.copyFrom(this.middle);
        this.rightExhaust2.copyFrom(this.middle);

        return ImmutableList.of(
                this.body,
                this.middle,
                this.leftCanister,
                this.rightCanister,
                this.leftTip1,
//                this.leftTip2,
                this.rightTip1,
//                this.rightTip2,
                this.leftExhaust1,
                this.leftExhaust2,
                this.rightExhaust1,
                this.rightExhaust2,
                this.leftArm,
                this.rightArm
        );
    }

    @Override
    public void renderToBuffer(PoseStack poseStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        // Could be used to alter texture of some of the elements?
        //ResourceLocation TEXTURE = new ResourceLocation(SimplyJetpacks.MODID, "texture_path");
        //VertexConsumer vertexConsumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.entityTranslucent(TEXTURE));
        super.renderToBuffer(poseStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static void getJetpackModelFromJson(PartDefinition root) {
        Gson gson = new Gson();
        JsonObject model = null;
        try {
            ResourceLocation location = new ResourceLocation(MOD_ID, "jetpack_model.json"); // no "assets/" prefix!
            Resource resource = Minecraft.getInstance().getResourceManager().getResource(location).orElseThrow();
            model = gson.fromJson(resource.openAsReader(), JsonObject.class);
        } catch (IOException e) {
            throw new RuntimeException("Cannot load jetpack model!", e);
        }

        JsonArray elements = model.getAsJsonArray("elements");
        for (JsonElement element : elements) {
            JsonObject box = element.getAsJsonObject();

            String name = box.has("name") ? box.get("name").getAsString() : "part";
            JsonArray from = box.getAsJsonArray("from");
            JsonArray to = box.getAsJsonArray("to");

            float x1 = from.get(0).getAsFloat();
            float y1 = from.get(1).getAsFloat();
            float z1 = from.get(2).getAsFloat();

            float x2 = to.get(0).getAsFloat();
            float y2 = to.get(1).getAsFloat();
            float z2 = to.get(2).getAsFloat();

            //Get texture offsets
            JsonObject faces = box.getAsJsonObject("faces");
            JsonObject anyFace = faces.entrySet().iterator().next().getValue().getAsJsonObject();
            JsonArray uv = anyFace.getAsJsonArray("uv");

            int texU = Math.round(uv.get(0).getAsFloat());
            int texV = Math.round(uv.get(1).getAsFloat());

            // Minecraft model space is offset by -8 in X and Z
            float originX = x1 - 8;
            float originY = y1;
            float originZ = z1;
            int sizeX = Math.round(x2 - x1);
            int sizeY = Math.round(y2 - y1);
            int sizeZ = Math.round(z2 - z1);

            switch (name) {
                case "middle" -> root.addOrReplaceChild("middle", CubeListBuilder.create().mirror()
                        .texOffs(texU, texV).addBox(originX, originY, originZ, sizeX, sizeY, sizeZ), PartPose.ZERO);
                case "leftCanister" -> root.addOrReplaceChild("leftCanister", CubeListBuilder.create().mirror()
                        .texOffs(texU, texV).addBox(originX, originY, originZ, sizeX, sizeY, sizeZ), PartPose.ZERO);
                case "rightCanister" -> root.addOrReplaceChild("rightCanister", CubeListBuilder.create().mirror()
                        .texOffs(texU, texV).addBox(originX, originY, originZ, sizeX, sizeY, sizeZ), PartPose.ZERO);
                case "leftTip1" -> root.addOrReplaceChild("leftTip1", CubeListBuilder.create().mirror()
                        .texOffs(texU, texV).addBox(originX, originY, originZ, sizeX, sizeY, sizeZ), PartPose.ZERO);
                case "rightTip1" -> root.addOrReplaceChild("rightTip1", CubeListBuilder.create().mirror()
                        .texOffs(texU, texV).addBox(originX, originY, originZ, sizeX, sizeY, sizeZ), PartPose.ZERO);
                case "leftExhaust1" -> root.addOrReplaceChild("leftExhaust1", CubeListBuilder.create().mirror()
                        .texOffs(texU, texV).addBox(originX, originY, originZ, sizeX, sizeY, sizeZ), PartPose.ZERO);
                case "rightExhaust1" -> root.addOrReplaceChild("rightExhaust1", CubeListBuilder.create().mirror()
                        .texOffs(texU, texV).addBox(originX, originY, originZ, sizeX, sizeY, sizeZ), PartPose.ZERO);
                case "leftTip2" -> root.addOrReplaceChild("leftTip2", CubeListBuilder.create().mirror()
                        .texOffs(texU, texV).addBox(originX, originY, originZ, sizeX, sizeY, sizeZ), PartPose.ZERO);
                case "rightTip2" -> root.addOrReplaceChild("rightTip2", CubeListBuilder.create().mirror()
                        .texOffs(texU, texV).addBox(originX, originY, originZ, sizeX, sizeY, sizeZ), PartPose.ZERO);
                case "leftExhaust2" -> root.addOrReplaceChild("leftExhaust2", CubeListBuilder.create().mirror()
                        .texOffs(texU, texV).addBox(originX, originY, originZ, sizeX, sizeY, sizeZ), PartPose.ZERO);
                case "rightExhaust2" -> root.addOrReplaceChild("rightExhaust2", CubeListBuilder.create().mirror()
                        .texOffs(texU, texV).addBox(originX, originY, originZ, sizeX, sizeY, sizeZ), PartPose.ZERO);
            }
        }
    }
}