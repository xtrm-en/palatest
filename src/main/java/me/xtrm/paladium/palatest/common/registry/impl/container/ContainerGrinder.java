package me.xtrm.paladium.palatest.common.registry.impl.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.xtrm.paladium.palatest.common.recipe.type.GrinderRecipe;
import me.xtrm.paladium.palatest.common.registry.impl.RecipeRegistry;
import me.xtrm.paladium.palatest.common.registry.impl.tile.TileEntityGrinder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ContainerGrinder extends Container {
    private final TileEntityGrinder tileEntityGrinder;
    private int lastLevel;
    private int lastProgress;
    private boolean lastLocked;

    public ContainerGrinder(InventoryPlayer inventoryPlayer, TileEntityGrinder tileEntityGrinder) {
        this.tileEntityGrinder = tileEntityGrinder;

        this.addSlotToContainer(new Slot(tileEntityGrinder, 0, 8, 53));
        this.addSlotToContainer(new SlotOutput(inventoryPlayer.player, tileEntityGrinder, 1, 53, 26));
        this.addSlotToContainer(new Slot(tileEntityGrinder, 2, 116, 26));
        this.addSlotToContainer(new Slot(tileEntityGrinder, 3, 116, 53));

        // Player inventory
        int i;
        for (i = 0; i < 3; ++i)
            for (int j = 0; j < 9; ++j)
                this.addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
        // Hotbar
        for (i = 0; i < 9; ++i)
            this.addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
    }

    @Override
    public void onCraftGuiOpened(ICrafting crafting) {
        super.onCraftGuiOpened(crafting);
        crafting.sendProgressBarUpdate(this, 0, this.tileEntityGrinder.level);
        crafting.sendProgressBarUpdate(this, 1, this.tileEntityGrinder.progress);
        crafting.sendProgressBarUpdate(this, 2, this.tileEntityGrinder.locked ? 1 : 0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (ICrafting crafter : (List<ICrafting>) this.crafters) {
            if (this.lastLevel != this.tileEntityGrinder.level)
                crafter.sendProgressBarUpdate(this, 0, this.tileEntityGrinder.level);
            if (this.lastProgress != this.tileEntityGrinder.progress)
                crafter.sendProgressBarUpdate(this, 1, this.tileEntityGrinder.progress);
            if (this.lastLocked != this.tileEntityGrinder.locked)
                crafter.sendProgressBarUpdate(this, 2, this.tileEntityGrinder.locked ? 1 : 0);
        }

        this.lastLevel = this.tileEntityGrinder.level;
        this.lastProgress = this.tileEntityGrinder.progress;
        this.lastLocked = this.tileEntityGrinder.locked;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(int index, int value) {
        if (index == 0)
            this.tileEntityGrinder.level = value;
        if (index == 1)
            this.tileEntityGrinder.progress = value;
        if (index == 2)
            this.tileEntityGrinder.locked = value == 1;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.tileEntityGrinder.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack stackCopy = null;
        Slot slot = (Slot) this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            stackCopy = slotStack.copy();

            if (index == 1) {
                if (!this.mergeItemStack(slotStack, 4, 40, true))
                    return null;
                slot.onSlotChange(slotStack, stackCopy);
            } else if (index != 0 && index != 2 && index != 3) {
                if (
                    RecipeRegistry.INSTANCE.ofType(GrinderRecipe.class)
                        .stream()
                        .anyMatch(it ->
                            it.inputs[0].isItemEqual(slotStack)
                        )
                ) { // Top recipe slot
                    if (!this.mergeItemStack(slotStack, 2, 3, false))
                        return null;
                } else if (
                    RecipeRegistry.INSTANCE.ofType(GrinderRecipe.class)
                        .stream()
                        .anyMatch(it ->
                            it.inputs[1].isItemEqual(slotStack)
                        )
                ) { // Bottom recipe slot
                    if (!this.mergeItemStack(slotStack, 3, 4, false))
                        return null;
                } else if (TileEntityGrinder.isFuel(slotStack)) {
                    if (!this.mergeItemStack(slotStack, 0, 1, false))
                        return null;
                } else if (index < 30) {
                    if (!this.mergeItemStack(slotStack, 31, 40, false))
                        return null;
                } else if (index < 39 && !this.mergeItemStack(slotStack, 4, 31, false)) {
                    return null;
                }
            } else if (!this.mergeItemStack(slotStack, 4, 40, false))
                return null;

            if (slotStack.stackSize == 0)
                slot.putStack(null);
            else
                slot.onSlotChanged();

            if (slotStack.stackSize == stackCopy.stackSize)
                return null;
            slot.onPickupFromSlot(player, slotStack);
        }
        return stackCopy;
    }

    private static class SlotOutput extends Slot {
        /**
         * The player that is using the GUI where this slot resides.
         */
        private final EntityPlayer thePlayer;
        private int amount;

        public SlotOutput(EntityPlayer entityPlayer, IInventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
            this.thePlayer = entityPlayer;
        }

        /**
         * Check if the stack is a valid item for this slot.
         */
        public boolean isItemValid(ItemStack stack) {
            return false;
        }

        /**
         * Decrease the size of the stack in slot (first int arg) by the amount
         * of the second int arg. Returns the new stack.
         */
        public ItemStack decrStackSize(int amount) {
            if (this.getHasStack())
                this.amount += Math.min(amount, this.getStack().stackSize);
            return super.decrStackSize(amount);
        }

        public void onPickupFromSlot(EntityPlayer entityPlayer, ItemStack itemStack) {
            this.onCrafting(itemStack);
            super.onPickupFromSlot(entityPlayer, itemStack);
        }

        /**
         * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood,
         * typically increases an internal count then calls onCrafting(item).
         */
        protected void onCrafting(ItemStack itemStack, int count) {
            this.amount += count;
            this.onCrafting(itemStack);
        }

        protected void onCrafting(ItemStack itemStack) {
            itemStack.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.amount);
            this.amount = 0;
        }
    }
}
