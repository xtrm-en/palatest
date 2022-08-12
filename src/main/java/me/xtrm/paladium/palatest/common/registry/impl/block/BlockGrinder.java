package me.xtrm.paladium.palatest.common.registry.impl.block;

import me.xtrm.paladium.palatest.PalaTest;
import me.xtrm.paladium.palatest.common.extended.PalaPlayer;
import me.xtrm.paladium.palatest.common.registry.impl.BlockRegistry;
import me.xtrm.paladium.palatest.common.registry.impl.tile.TileEntityGrinder;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.Random;

import static me.xtrm.paladium.palatest.common.util.RegisterableWrapper.wrap;

public class BlockGrinder extends Block implements ITileEntityProvider {
    private static final Random RANDOM = new Random();

    public BlockGrinder() {
        super(Material.rock);
        wrap(this
            .setHardness(4.0F)
            .setResistance(5.0F))
            .named("grinder_panel")
            .defaultCreativeTab();
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileEntityGrinder();
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return createTileEntity(worldIn, meta);
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ) {
        if (!BlockGrinder.isValidStructure(worldIn, x, y, z))
            return false;
        if (worldIn.isRemote)
            return true;
        TileEntity tileGrinder = worldIn.getTileEntity(x, y, z);
        if (tileGrinder instanceof TileEntityGrinder) {
            ((TileEntityGrinder) tileGrinder).lastInteract = PalaPlayer.get(player);
            player.openGui(PalaTest.INSTANCE, 1, worldIn, x, y, z);
            return true;
        }
        return false;
    }

    @Override
    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int par6) {
        TileEntity tileGrinder = worldIn.getTileEntity(x, y, z);

        if (tileGrinder instanceof TileEntityGrinder) {
            TileEntityGrinder tileEntityGrinder = (TileEntityGrinder) tileGrinder;
            for (int j1 = 0; j1 < tileEntityGrinder.getSizeInventory(); ++j1) {
                ItemStack itemstack = tileEntityGrinder.getStackInSlot(j1);

                if (itemstack != null) {
                    float offsetX = RANDOM.nextFloat() * 0.8F + 0.1F;
                    float offsetY = RANDOM.nextFloat() * 0.8F + 0.1F;
                    float offsetZ = RANDOM.nextFloat() * 0.8F + 0.1F;

                    while (itemstack.stackSize > 0) {
                        int k1 = RANDOM.nextInt(21) + 10;

                        if (k1 > itemstack.stackSize)
                            k1 = itemstack.stackSize;
                        itemstack.stackSize -= k1;

                        EntityItem entityitem = new EntityItem(
                            worldIn,
                            (float) x + offsetX,
                            (float) y + offsetY,
                            (float) z + offsetZ,
                            new ItemStack(
                                itemstack.getItem(),
                                k1,
                                itemstack.getMetadata()
                            )
                        );
                        if (itemstack.hasTagCompound())
                            entityitem.getEntityItem().setTagCompound(
                                (NBTTagCompound) itemstack.getTagCompound().copy()
                            );

                        float jumpFactor = 0.05F;
                        entityitem.motionX = RANDOM.nextGaussian() * jumpFactor;
                        entityitem.motionY = RANDOM.nextGaussian() * jumpFactor + 0.2F;
                        entityitem.motionZ = RANDOM.nextGaussian() * jumpFactor;
                        worldIn.spawnEntityInWorld(entityitem);
                    }
                }
            }
            worldIn.updateNeighborsAboutBlockChange(x, y, z, blockBroken);
        }
        super.breakBlock(worldIn, x, y, z, blockBroken, par6);
    }

    public static boolean isValidStructure(World worldIn, int x, int y, int z) {
        Block grinderPanel =
            BlockRegistry.INSTANCE.withType(BlockGrinder.class);
        if (worldIn.getBlock(x, y, z) != grinderPanel)
            return false;

        Block grinderFrame =
            BlockRegistry.INSTANCE.withType(BlockGrinderShell.class);
        global:
        for (EnumFacing facing : EnumFacing.values()) {
            if (facing.getFrontOffsetY() != 0) continue;
            Block block = worldIn.getBlock(
                x + facing.getFrontOffsetX(),
                y,
                z + facing.getFrontOffsetZ()
            );
            if (block != Blocks.water)
                continue;
            x += facing.getFrontOffsetX();
            z += facing.getFrontOffsetZ();
            // Possible center block found
            int panelFound = 0;
            for (int by = y - 1; by <= y + 1; by++) {
                for (int bx = x - 1; bx <= x + 1; bx++) {
                    for (int bz = z - 1; bz <= z + 1; bz++) {
                        Block blockAt = worldIn.getBlock(bx, by, bz);

                        boolean expectWater = (bx == x && by == y && bz == z);
                        boolean expectCasingY = (bx == x && bz == z && by != y);
                        boolean expectCasingXZ = (bx == x || bz == z) && by == y;

                        if (expectWater) {
                            if (blockAt != Blocks.water)
                                continue global;
                        } else if (expectCasingY || expectCasingXZ) {
                            if (blockAt == grinderPanel)
                                panelFound++;
                            else if (blockAt == grinderFrame) {
                                if (worldIn.getBlockMetadata(bx, by, bz) != 1)
                                    continue global;
                            } else
                                continue global;
                        } else {
                            if (blockAt != grinderFrame)
                                continue global;
                            if (worldIn.getBlockMetadata(bx, by, bz) != 0)
                                continue global;
                        }
                    }
                }
            }
            if (panelFound != 1)
                continue;
            return true;
        }
        return false;
    }
}
