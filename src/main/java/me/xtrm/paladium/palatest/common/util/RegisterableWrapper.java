package me.xtrm.paladium.palatest.common.util;

import me.xtrm.paladium.palatest.Constants;
import me.xtrm.paladium.palatest.common.registry.holder.CreativeTabHolder;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

@SuppressWarnings("UnusedReturnValue")
public class RegisterableWrapper {
    private final Item item;
    private final Block block;

    private RegisterableWrapper(Item item, Block block) {
        this.item = item;
        this.block = block;
    }

    public static RegisterableWrapper wrap(Item item) {
        return new RegisterableWrapper(item, null);
    }

    public static RegisterableWrapper wrap(Block block) {
        return new RegisterableWrapper(null, block);
    }

    public RegisterableWrapper named(String name) {
        if (item != null) {
            item.setUnlocalizedName(name);
            item.setTextureName(Constants.MODID + ":" + name);
        }
        if (block != null) {
            block.setUnlocalizedName(name);
            block.setTextureName(Constants.MODID + ":" + name);
        }
        return this;
    }

    public RegisterableWrapper defaultCreativeTab() {
        if (item != null)
            item.setCreativeTab(CreativeTabHolder.PALATEST);
        if (block != null)
            block.setCreativeTab(CreativeTabHolder.PALATEST);
        return this;
    }

    public Block block() {
        return this.block;
    }

    public Item item() {
        return this.item;
    }
}
