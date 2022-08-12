package me.xtrm.paladium.palatest.common.registry.holder;

import me.xtrm.paladium.palatest.common.registry.impl.BlockRegistry;
import me.xtrm.paladium.palatest.common.registry.impl.block.BlockGrinderShell;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CreativeTabHolder {
    public static final CreativeTabs PALATEST =
        new CreativeTabs("palatest") {
            @Override
            public Item getTabIconItem() {
                return Item.getItemFromBlock(
                    BlockRegistry.INSTANCE.withType(BlockGrinderShell.class)
                );
            }
        };

    private CreativeTabHolder() {
        throw new Error();
    }
}
