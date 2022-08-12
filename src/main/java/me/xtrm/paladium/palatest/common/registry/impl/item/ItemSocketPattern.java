package me.xtrm.paladium.palatest.common.registry.impl.item;

import net.minecraft.item.Item;

import static me.xtrm.paladium.palatest.common.util.RegisterableWrapper.wrap;

public class ItemSocketPattern extends Item {
    public ItemSocketPattern() {
        wrap(this)
            .named("socket_pattern")
            .defaultCreativeTab();
    }
}
