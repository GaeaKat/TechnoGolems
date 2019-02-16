package com.nekokittygames.technogolems.entities;

import com.nekokittygames.technogolems.TechnoGolems;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.world.World;

public class EntityRobot extends EntityLiving {
    // todo: remove temporary hack until forge PR goes through
    public static final EntityType<?> ENTITY_ROBOT= EntityType.register("technogolems:robot",EntityType.Builder.create(EntityRobot.class,EntityRobot::new).tracker(10,10,true)).setRegistryName("technogolems","robot");
    public EntityRobot(World worldIn) {
        super(ENTITY_ROBOT, worldIn);
        this.setSize(0.75F, 0.75F);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(8, new EntityAILookIdle(this));
    }

    @Override
    public void livingTick() {
        super.livingTick();
    }
}
