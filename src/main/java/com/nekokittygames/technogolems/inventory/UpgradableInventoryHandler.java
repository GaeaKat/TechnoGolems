package com.nekokittygames.technogolems.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

public class UpgradableInventoryHandler implements IItemHandler,IItemHandlerModifiable, INBTSerializable<NBTTagCompound> {
    private int allowedSlots;
    private final int maxSlots;
    private NonNullList<ItemStack> inventoryContents;

    public UpgradableInventoryHandler(int allowedSlots,int maxSlots)
    {
        this.allowedSlots=allowedSlots;
        this.maxSlots=maxSlots;
        this.inventoryContents=NonNullList.withSize(maxSlots,ItemStack.EMPTY);
    }


    @Override
    public int getSlots() {
        return allowedSlots;
    }

    public void setAllowedSlots(int allowedSlots)
    {
        if(allowedSlots>maxSlots)
            allowedSlots=maxSlots;
        this.allowedSlots=allowedSlots;
        this.updateSlotSize();
    }

    private void updateSlotSize() {

    }


    public NonNullList<ItemStack> getSpareItems()
    {
        if(allowedSlots==maxSlots)
            return NonNullList.create();
        NonNullList<ItemStack> spareItems=NonNullList.withSize(maxSlots-allowedSlots,ItemStack.EMPTY);
        for(int i=allowedSlots;i<maxSlots;i++)
        {
            spareItems.add(i-allowedSlots,inventoryContents.get(i));
            inventoryContents.set(i,ItemStack.EMPTY);
        }
        return spareItems;
    }
    public int getAllowedSlots()
    {
        return allowedSlots;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        validateSlotIndex(slot);
        if(slot>=allowedSlots)
            return ItemStack.EMPTY;
        return inventoryContents.get(slot);
    }

    protected void validateSlotIndex(int slot)
    {
        if (slot < 0 || slot >= inventoryContents.size())
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + inventoryContents.size() + ")");

    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (stack.isEmpty())
            return ItemStack.EMPTY;

        validateSlotIndex(slot);
        if(slot>=allowedSlots)
            return stack;

        ItemStack existing = this.inventoryContents.get(slot);

        int limit = getStackLimit(slot, stack);

        if (!existing.isEmpty())
        {
            if (!ItemHandlerHelper.canItemStacksStack(stack, existing))
                return stack;

            limit -= existing.getCount();
        }

        if (limit <= 0)
            return stack;

        boolean reachedLimit = stack.getCount() > limit;

        if (!simulate)
        {
            if (existing.isEmpty())
            {
                this.inventoryContents.set(slot, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
            }
            else
            {
                existing.grow(reachedLimit ? limit : stack.getCount());
            }
            onContentsChanged(slot);
        }

        return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount()- limit) : ItemStack.EMPTY;
    }

    private int getStackLimit(int slot, ItemStack stack) {
        return Math.min(getSlotLimit(slot), stack.getMaxStackSize());
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount == 0)
            return ItemStack.EMPTY;

        validateSlotIndex(slot);

        ItemStack existing = this.inventoryContents.get(slot);

        if (existing.isEmpty() || slot>=allowedSlots)
            return ItemStack.EMPTY;

        int toExtract = Math.min(amount, existing.getMaxStackSize());

        if (existing.getCount() <= toExtract)
        {
            if (!simulate)
            {
                this.inventoryContents.set(slot, ItemStack.EMPTY);
                onContentsChanged(slot);
            }
            return existing;
        }
        else
        {
            if (!simulate)
            {
                this.inventoryContents.set(slot, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
                onContentsChanged(slot);
            }

            return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
        }
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return true;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagList nbtTagList = new NBTTagList();
        for (int i = 0; i < inventoryContents.size(); i++)
        {
            if (!inventoryContents.get(i).isEmpty())
            {
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setInt("Slot", i);
                inventoryContents.get(i).write(itemTag);
                nbtTagList.add(itemTag);
            }
        }
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("Items", nbtTagList);
        nbt.setInt("Size", inventoryContents.size());
        nbt.setInt("AllowedSize",allowedSlots);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        setSize(nbt.contains("Size", Constants.NBT.TAG_INT) ? nbt.getInt("Size") : inventoryContents.size());
        NBTTagList tagList = nbt.getList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagList.size(); i++)
        {
            NBTTagCompound itemTags = tagList.getCompound(i);
            int slot = itemTags.getInt("Slot");

            if (slot >= 0 && slot < inventoryContents.size())
            {
                inventoryContents.set(slot, ItemStack.read(itemTags));
            }
        }
        onLoad();
    }
    public void setSize(int size)
    {
        inventoryContents = NonNullList.withSize(size, ItemStack.EMPTY);
    }
    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        validateSlotIndex(slot);
        inventoryContents.set(slot,stack);
        onContentsChanged(slot);
    }

    protected void onContentsChanged(int slot)
    {

    }
    protected void onLoad()
    {

    }
}
