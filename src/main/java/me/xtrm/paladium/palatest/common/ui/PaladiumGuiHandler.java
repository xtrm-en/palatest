package me.xtrm.paladium.palatest.common.ui;

import cpw.mods.fml.common.network.IGuiHandler;
import me.xtrm.paladium.palatest.client.ui.gui.grinder.GuiGrinder;
import me.xtrm.paladium.palatest.common.registry.impl.container.ContainerGrinder;
import me.xtrm.paladium.palatest.common.registry.impl.tile.TileEntityGrinder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PaladiumGuiHandler implements IGuiHandler {
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityGrinder)
            return new GuiGrinder(player.inventory, (TileEntityGrinder) tileEntity);
        return null;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityGrinder)
            return new ContainerGrinder(player.inventory, (TileEntityGrinder) tileEntity);
        return null;
    }
}
