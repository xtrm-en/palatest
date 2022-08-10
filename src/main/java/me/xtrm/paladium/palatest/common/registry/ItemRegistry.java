package me.xtrm.paladium.palatest.common.registry;

import cpw.mods.fml.common.registry.GameRegistry;
import me.xtrm.paladium.palatest.common.registry.impl.item.ItemBigSword;
import me.xtrm.paladium.palatest.common.registry.type.TItem;

import java.util.*;
import java.util.stream.Collectors;

public enum ItemRegistry implements IRegistry<TItem> {
    INSTANCE;

    private final Map<String, TItem> itemMap;

    ItemRegistry() {
        this.itemMap = new HashMap<>();

        Collections.singletonList(
            new ItemBigSword()
        ).forEach(
            item -> this.itemMap.put(
                item.getUnlocalizedName().substring("tile.".length()),
                item
            )
        );
    }

    public void registerAll() {
        this.itemMap.forEach((id, item) -> GameRegistry.registerItem(item, id));
    }

    @Override
    public List<TItem> getAll(String name) {
        return this.itemMap.entrySet()
            .stream()
            .filter(it -> it.getKey().equalsIgnoreCase(name))
            .map(Map.Entry::getValue)
            .collect(Collectors.toList());
    }

    @Override
    public TItem getByType(Class<? extends TItem> clazz, TItem... reified) {
        return this.itemMap.values()
            .stream()
            .filter(item -> item.getClass() == clazz)
            .findFirst()
            .orElse(null);
    }
}
