package me.xtrm.paladium.palatest.common.registry.impl.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemBlockGrinderShell extends ItemBlock {
    private final String[] subName;

    public ItemBlockGrinderShell(Block block, String[] subBlock) {
        super(block);
        this.setMaxDurability(0);
        this.setHasSubtypes(true);
        this.subName = subBlock;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int metadata) {
        return this.blockInstance.getIcon(2, metadata);
    }

    @Override
    public int getMetadata(int metadata) {
        return metadata;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int metadata = stack.getMetadata();
        if (metadata < 0 || metadata >= this.subName.length)
            metadata = 0;
        return super.getUnlocalizedName() + "." + this.subName[metadata];
    }
}
