package com.nekokittygames.technogolems.client.renderer.entities;

import com.nekokittygames.technogolems.client.renderer.entities.models.ModelRobot;
import com.nekokittygames.technogolems.entities.EntityRobot;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.model.ModelBase;
import net.minecraft.client.renderer.entity.model.ModelIronGolem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class RenderRobot extends RenderLiving<EntityRobot> {
    private static final ResourceLocation ROBOT_TEXTURES = new ResourceLocation("technogolems","textures/entities/robot.png");
    public RenderRobot(RenderManager rendermanagerIn) {
        super(rendermanagerIn, new ModelRobot(), 0.5F);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityRobot entity) {
        return ROBOT_TEXTURES;
    }

    @Override
    public void doRender(EntityRobot entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
