package me.xtrm.paladium.palatest.common.registry.impl;

import cpw.mods.fml.common.registry.GameRegistry;
import me.xtrm.paladium.palatest.Constants;
import me.xtrm.paladium.palatest.common.registry.IRegistry;
import me.xtrm.paladium.palatest.common.registry.impl.tile.TileEntityGrinder;
import net.minecraft.tileentity.TileEntity;

import java.util.List;

public enum TileEntityRegistry implements IRegistry<TileEntity> {
    INSTANCE;

    TileEntityRegistry() {
    }

    @Override
    public void registerAll() {
        GameRegistry.registerTileEntity(
            TileEntityGrinder.class,
            Constants.MODID + ":" + "grinder"
        );
    }

    @Override
    public List<TileEntity> all(String name) {
        return null;
    }
}
