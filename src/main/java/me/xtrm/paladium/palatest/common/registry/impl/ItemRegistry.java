package me.xtrm.paladium.palatest.common.registry.impl;

import cpw.mods.fml.common.registry.GameRegistry;
import me.xtrm.paladium.palatest.PalaTest;
import me.xtrm.paladium.palatest.common.registry.IRegistry;
import me.xtrm.paladium.palatest.common.registry.IRegistryHook;
import me.xtrm.paladium.palatest.common.registry.impl.item.ItemBigDiamondSword;
import me.xtrm.paladium.palatest.common.registry.impl.item.ItemSocketPattern;
import net.minecraft.item.Item;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public enum ItemRegistry implements IRegistry<Item> {
    INSTANCE;

    private final Map<String, Item> itemMap;

    ItemRegistry() {
        this.itemMap = new HashMap<>();

        Arrays.asList(
            new ItemSocketPattern(),
            new ItemBigDiamondSword()
        ).forEach(
            item -> this.itemMap.put(
                item.getUnlocalizedName().substring("tile.".length()),
                item
            )
        );
    }

    public void registerAll() {
        this.itemMap.forEach((id, item) -> {
            PalaTest.INSTANCE.getLogger().info(
                "Registering item \"{}\".",
                id
            );
            if (item instanceof IRegistryHook)
                ((IRegistryHook<Item>) item).register(item, id);
            else
                GameRegistry.registerItem(item, id);
        });
    }

    @Override
    public List<Item> all(String name) {
        return this.itemMap.entrySet()
            .stream()
            .filter(it -> it.getKey().equalsIgnoreCase(name))
            .map(Map.Entry::getValue)
            .collect(Collectors.toList());
    }

    @SafeVarargs
    @Override
    public final <V extends Item> V withType(Class<V> clazz, V... reified) {
        return (V) this.itemMap.values()
            .stream()
            .filter(item -> item.getClass() == clazz)
            .findFirst()
            .orElse(null);
    }
}
