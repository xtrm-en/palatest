package me.xtrm.paladium.palatest.common.registry.impl;

import cpw.mods.fml.common.registry.GameRegistry;
import me.xtrm.paladium.palatest.Constants;
import me.xtrm.paladium.palatest.PalaTest;
import me.xtrm.paladium.palatest.common.registry.IRegistry;
import me.xtrm.paladium.palatest.common.registry.impl.tile.TileEntityGrinder;
import net.minecraft.tileentity.TileEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum TileEntityRegistry implements IRegistry<Class<? extends TileEntity>> {
    INSTANCE;

    private final Map<String, Class<? extends TileEntity>> tileEntityMap;

    TileEntityRegistry() {
        this.tileEntityMap = new HashMap<>();
        this.tileEntityMap.put("grinder", TileEntityGrinder.class);
    }

    @Override
    public void registerAll() {
        this.tileEntityMap.forEach((id, tile) -> {
            PalaTest.INSTANCE.getLogger().info(
                "Registering tile entity \"{}\".",
                id
            );

            GameRegistry.registerTileEntity(
                tile,
                Constants.MODID + ":" + id
            );
        });
    }

    @Override
    public List<Class<? extends TileEntity>> all(String name) {
        return this.tileEntityMap.entrySet()
            .stream()
            .filter(it -> it.getKey().equalsIgnoreCase(name))
            .map(Map.Entry::getValue)
            .collect(Collectors.toList());
    }
}
