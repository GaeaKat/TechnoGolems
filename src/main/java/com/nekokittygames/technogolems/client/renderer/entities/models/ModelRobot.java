package com.nekokittygames.technogolems.client.renderer.entities.models;

import net.minecraft.client.renderer.entity.model.ModelBase;
import net.minecraft.client.renderer.entity.model.ModelIronGolem;
import net.minecraft.client.renderer.entity.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelRobot extends ModelBase {
    private final ModelRenderer robotbody;
    private final ModelRenderer leftArm;
    private final ModelRenderer rightArm;


    public ModelRobot() {
        this(0.0f);
    }
    public ModelRobot(float scaleFactor)
    {
        this(scaleFactor,-7.0f);
    }

    public ModelRobot(float scaleFactor,float rotation)
    {
        this.robotbody=(new ModelRenderer(this)).setTextureSize(128,128);
        this.robotbody.setTextureOffset(0,0).addBox(0f,0f,0f,5,5,5,scaleFactor);
        this.robotbody.setRotationPoint(0,0,0);
        this.robotbody.rotateAngleX=0.45f;
        this.robotbody.rotateAngleY=0.45f;
        this.robotbody.rotateAngleZ=0.45f;

        this.leftArm=(new ModelRenderer(this)).setTextureSize(128,128);



        this.rightArm =(new ModelRenderer(this)).setTextureSize(128,128);
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        this.robotbody.render(scale);
    }
}
