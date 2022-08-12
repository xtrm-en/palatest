package me.xtrm.paladium.palatest.common.registry.impl.tile;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import me.xtrm.paladium.palatest.PalaTest;
import me.xtrm.paladium.palatest.common.extended.PalaPlayer;
import me.xtrm.paladium.palatest.common.network.clientbound.PacketNotify;
import me.xtrm.paladium.palatest.common.recipe.type.GrinderRecipe;
import me.xtrm.paladium.palatest.common.registry.impl.RecipeRegistry;
import me.xtrm.paladium.palatest.common.registry.impl.entity.EntityGolem;
import me.xtrm.paladium.palatest.common.ui.notification.NotificationType;
import me.xtrm.paladium.palatest.server.ServerProxy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.EnumDifficulty;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class TileEntityGrinder extends TileEntity implements IInventory {
    private static final Random RANDOM = new Random();
    /**
     * The maximum level of diamond the container can store.
     */
    private static final int MAX_LEVEL = 100;

    /**
     * The value added to the level per diamond.
     */
    private static final int FUEL_INCREMENT = 10;

    /**
     * The maximum spawning range of the golems.
     */
    private static final int MAX_SPAWN_DIST = 10;

    /**
     * The inventory stacks of this container.
     * <ul>
     *  <li>0 -> Diamond input</li>
     *  <li>1 -> Output</li>
     *  <li>2 -> Top input slot</li>
     *  <li>3 -> Bottom input slot</li>
     * </ul>
     */
    private final ItemStack[] stacks = new ItemStack[4];

    /**
     * The current diamond level
     * (between 0 and {@link TileEntityGrinder#MAX_LEVEL},
     * increments by {@link TileEntityGrinder#FUEL_INCREMENT})
     */
    public int level = 0;

    /**
     * The processing level (between 0.0F and 1.0F).
     */
    public int progress = 0;

    /**
     * Holder for entities spawned during locked / battle mode.
     */
    private final Set<Entity> battleTargets = new HashSet<>();

    /**
     * Whether this grinder is locked (i.e. in battle mode)
     */
    public boolean locked = false;

    /**
     * Last player to have interacted with the tile.
     */
    public PalaPlayer lastInteract;

    @Override
    public void updateEntity() {
        if (stacks[0] != null && isItemValidForSlot(0, stacks[0])) {
            if (level + FUEL_INCREMENT <= MAX_LEVEL) {
                decrStackSize(0, 1);
                level += FUEL_INCREMENT;
            }
        }

        GrinderRecipe currentRecipe = findCurrentRecipe();
        if (
            currentRecipe == null ||
                currentRecipe.levelCost > this.level ||
                !canOutput(currentRecipe.output)
        ) {
            this.progress = 0;
            return;
        }

        if (!locked) {
            progress++;
        }
        if (progress >= currentRecipe.ticks) {
            if (!locked) {
                locked = true;

                if (!worldObj.isRemote) {
                    if (!spawnGolems()) {
                        locked = false;
                        progress++;
                        return;
                    }

                    if (lastInteract == null)
                        return;

                    PalaTest.INSTANCE.getSidedProxy()
                        .getNetworkManager()
                        .sendTo(new PacketNotify(
                                NotificationType.WARNING,
                                "title.grinder.locked",
                                "message.grinder.locked",
                                30
                            ),
                            (EntityPlayerMP) lastInteract.getPlayer()
                        );
                    lastInteract.golemKills += 5;
                }
            } else {
                if (!worldObj.isRemote) {
                    if (battleTargets.stream().noneMatch(Entity::isEntityAlive)) {
                        battleTargets.clear();
                        locked = false;
                        progress++;

                        if (lastInteract == null)
                            return;

                        if (FMLCommonHandler.instance().getSide() == Side.SERVER) {
                            ((ServerProxy) PalaTest.INSTANCE.getSidedProxy())
                                .logCraft(lastInteract.getPlayer(), worldObj, currentRecipe);
                        }

                        PalaTest.INSTANCE.getSidedProxy()
                            .getNetworkManager()
                            .sendTo(new PacketNotify(
                                    NotificationType.SUCCESS,
                                    "title.grinder.unlocked",
                                    "message.grinder.unlocked",
                                    20
                                ),
                                (EntityPlayerMP) lastInteract.getPlayer()
                            );
                    }
                }
                if (!locked) {
                    if (this.stacks[1] == null) this.stacks[1] = currentRecipe.output.copy();
                    else if (this.stacks[2].isItemEqual(currentRecipe.output))
                        this.stacks[2].stackSize += currentRecipe.output.stackSize;
                    decrStackSize(2, 1);
                    decrStackSize(3, 1);
                    this.level -= currentRecipe.levelCost;
                    progress = 0;
                }
            }
        }
    }

    private boolean canOutput(ItemStack output) {
        if (this.stacks[1] == null) return true;
        if (!this.stacks[1].isItemEqual(output)) return false;
        int result = stacks[1].stackSize + output.stackSize;
        return (result <= getInventoryStackLimit()
            && result <= output.getMaxStackSize());
    }

    private boolean spawnGolems() {
        if (worldObj.difficultySetting == EnumDifficulty.PEACEFUL)
            return false;

        boolean succeeded = false;

        initial:
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 50; j++) {
                int x = (int) ((lastInteract.getPlayer().posX - MAX_SPAWN_DIST / 2) + RANDOM.nextInt(MAX_SPAWN_DIST));
                int y = (int) (lastInteract.getPlayer().posY + RANDOM.nextInt((int) ((float) MAX_SPAWN_DIST * .25)));
                int z = (int) ((lastInteract.getPlayer().posZ - MAX_SPAWN_DIST / 2) + RANDOM.nextInt(MAX_SPAWN_DIST));

                if (worldObj.getBlock(x, y, z).getMaterial().blocksMovement())
                    continue;

                double difficultyFactor = lastInteract.golemKills / 5.;
                EntityGolem golem = new EntityGolem(worldObj, difficultyFactor);
                golem.setPosition(x + .5, y + .5, z + .5);

                worldObj.spawnEntityInWorld(golem);
                worldObj.spawnParticle("cloud", x, y, z, .5, .5, .5);
                battleTargets.add(golem);
                succeeded = true;
                continue initial;
            }
        }
        return succeeded;
    }

    public GrinderRecipe findCurrentRecipe() {
        if (stacks[2] == null || stacks[3] == null) return null;
        return RecipeRegistry.INSTANCE.ofType(GrinderRecipe.class)
            .stream()
            .filter(it -> it.inputs[0].isItemEqual(stacks[2]))
            .filter(it -> it.inputs[1].isItemEqual(stacks[3]))
            .findFirst()
            .orElse(null);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.level = compound.getInteger("Level");
        this.progress = compound.getInteger("Process");
        this.locked = compound.getBoolean("Locked");

        NBTTagList tagList = compound.getTagList("Stacks", 10);
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound tag = tagList.getCompoundTagAt(i);
            byte b0 = tag.getByte("Slot");

            if (b0 >= 0 && b0 < this.stacks.length) this.stacks[b0] = ItemStack.loadItemStackFromNBT(tag);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("Level", level);
        compound.setInteger("Process", progress);
        compound.setBoolean("Locked", locked);

        NBTTagList tagList = new NBTTagList();
        for (int i = 0; i < this.stacks.length; i++) {
            if (this.stacks[i] != null) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setByte("Slot", (byte) i);
                this.stacks[i].writeToNBT(tag);
                tagList.appendTag(tag);
            }
        }
        compound.setTag("Stacks", tagList);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound compound = new NBTTagCompound();
        this.writeToNBT(compound);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, compound);
    }

    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity p) {
        this.readFromNBT(p.getNbtCompound());
    }

    @Override
    public int getSizeInventory() {
        return stacks.length;
    }

    @Override
    public ItemStack getStackInSlot(int slotIn) {
        return (slotIn < 0 || slotIn > stacks.length) ? null : stacks[slotIn];
    }

    public ItemStack decrStackSize(int index, int count) {
        if (this.stacks[index] == null) return null;
        if (this.stacks[index].stackSize <= count) {
            ItemStack itemstack = this.stacks[index];
            this.stacks[index] = null;
            return itemstack;
        }
        ItemStack itemstack = this.stacks[index].splitStack(count);
        if (this.stacks[index].stackSize == 0) this.stacks[index] = null;
        return itemstack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int index) {
        if (this.stacks[index] == null) return null;
        ItemStack itemstack = this.stacks[index];
        this.stacks[index] = null;
        return itemstack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.stacks[index] = stack;
        if (stack != null && stack.stackSize > this.getInventoryStackLimit())
            stack.stackSize = this.getInventoryStackLimit();
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return index != 1 && (index != 0 || stack.getItem() == Items.diamond);
    }

    @Override
    public String getInventoryName() {
        return "container.grinder";
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this;
    }

    @Override
    public boolean isCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void openChest() {
    }

    @Override
    public void closeChest() {
    }

    public static boolean isFuel(ItemStack itemStack) {
        return itemStack != null && itemStack.getItem() == Items.diamond;
    }
}
