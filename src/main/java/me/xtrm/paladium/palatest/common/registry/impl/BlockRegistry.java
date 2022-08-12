package me.xtrm.paladium.palatest.common.registry.impl;

import cpw.mods.fml.common.registry.GameRegistry;
import me.xtrm.paladium.palatest.PalaTest;
import me.xtrm.paladium.palatest.common.registry.IRegistry;
import me.xtrm.paladium.palatest.common.registry.IRegistryHook;
import me.xtrm.paladium.palatest.common.registry.impl.block.BlockGrinder;
import me.xtrm.paladium.palatest.common.registry.impl.block.BlockGrinderShell;
import net.minecraft.block.Block;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public enum BlockRegistry implements IRegistry<Block> {
    INSTANCE;

    private final Map<String, Block> blockMap;

    BlockRegistry() {
        this.blockMap = new HashMap<>();

        Arrays.asList(
            new BlockGrinderShell(),
            new BlockGrinder()
        ).forEach(
            block -> this.blockMap.put(
                block.getUnlocalizedName().substring("tile.".length()),
                block
            )
        );
    }

    public void registerAll() {
        this.blockMap.forEach((id, block) -> {
            PalaTest.INSTANCE.getLogger().info(
                "Registering block \"{}\"",
                id
            );
            if (block instanceof IRegistryHook)
                ((IRegistryHook<Block>) block).register(block, id);
            else
                GameRegistry.registerBlock(block, id);
        });
    }

    @Override
    public List<Block> all(String name) {
        return this.blockMap.entrySet()
            .stream()
            .filter(it -> it.getKey().equalsIgnoreCase(name))
            .map(Map.Entry::getValue)
            .collect(Collectors.toList());
    }

    @Override
    @SafeVarargs
    public final <V extends Block> V withType(Class<V> clazz, V... reified) {
        return (V) this.blockMap.values()
            .stream()
            .filter(block -> block.getClass() == clazz)
            .findFirst()
            .orElse(null);
    }
}
