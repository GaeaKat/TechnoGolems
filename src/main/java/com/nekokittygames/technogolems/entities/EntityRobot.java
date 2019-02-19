package com.nekokittygames.technogolems.entities;

import com.nekokittygames.technogolems.inventory.UpgradableInventoryHandler;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

public class EntityRobot extends EntityLiving  {
    private net.minecraftforge.common.util.LazyOptional<UpgradableInventoryHandler> inventory=LazyOptional.of(() -> new UpgradableInventoryHandler(2,28));

    // todo: remove temporary hack until forge PR goes through
    public static final EntityType<?> ENTITY_ROBOT= EntityType.Builder.create(EntityRobot.class,EntityRobot::new).tracker(10,10,true).build("").setRegistryName("technogolems","robot");
    public EntityRobot(World worldIn) {
        super(ENTITY_ROBOT, worldIn);
        this.setSize(0.75F, 0.75F);
        initInventory();
    }

    @Override
    public void remove() {
        super.remove();
        inventory.invalidate();
    }

    @Override
    public void writeAdditional(NBTTagCompound compound) {
        super.writeAdditional(compound);
        compound.setTag("inventory", inventory.map(UpgradableInventoryHandler::serializeNBT).orElseGet(NBTTagCompound::new));

    }

    @Override
    public void readAdditional(NBTTagCompound compound) {
        super.readAdditional(compound);
        if(compound.hasKey("inventory"))
            this.inventory.ifPresent(handler->handler.deserializeNBT(compound.getCompound("inventory")));
    }

    public void initInventory()
    {

    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(8, new EntityAILookIdle(this));
    }

    public void throwExcess() {
            inventory.map(UpgradableInventoryHandler::getSpareItems).ifPresent(list -> {
                for (ItemStack excess1 : (NonNullList<ItemStack>)list) {
                    this.entityDropItem(excess1);
                }
            });
    }

    @Override
    public void livingTick() {
        super.livingTick();
        throwExcess();

    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if(!removed && capability==net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && inventory!=null)
            return inventory.cast();
        return super.getCapability(capability, facing);
    }
}
