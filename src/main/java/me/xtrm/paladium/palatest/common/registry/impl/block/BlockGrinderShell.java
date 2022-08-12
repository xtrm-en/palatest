package me.xtrm.paladium.palatest.common.registry.impl.block;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.xtrm.paladium.palatest.Constants;
import me.xtrm.paladium.palatest.common.registry.IRegistryHook;
import me.xtrm.paladium.palatest.common.registry.impl.item.ItemBlockGrinderShell;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

import static me.xtrm.paladium.palatest.common.util.RegisterableWrapper.wrap;

/**
 * {@link Block} that makes up the outer shell of the Grindr structure.
 *
 * @author xtrm
 */
public class BlockGrinderShell extends Block implements IRegistryHook<Block> {
    private IIcon frame;
    private IIcon casing;

    public BlockGrinderShell() {
        super(Material.rock);
        wrap(this
            .setHardness(3.0F)
            .setResistance(5.0F))
            .named("grinder_shell")
            .defaultCreativeTab();
    }

    @Override
    public void registerIcons(IIconRegister registry) {
        frame = registry.registerIcon(Constants.MODID + ":" + "grinder_frame");
        casing = registry.registerIcon(Constants.MODID + ":" + "grinder_casing");
    }

    @Override
    public int damageDropped(int meta)
    {
        return meta;
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return meta == 0 ? frame : casing;
    }

    @SuppressWarnings("unchecked")
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        list.add(new ItemStack(itemIn, 1, 0));
        list.add(new ItemStack(itemIn, 1, 1));
    }

    @Override
    public void register(Block instance, String name) {
        GameRegistry.registerBlock(
            instance,
            ItemBlockGrinderShell.class,
            name,
            new Object[]{new String[] {"frame", "casing"}}
        );
    }
}
