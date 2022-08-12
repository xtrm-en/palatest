package me.xtrm.paladium.palatest.common.registry.impl.item;

import me.xtrm.paladium.palatest.common.registry.holder.ToolMaterialHolder;
import net.minecraft.item.ItemSword;

import static me.xtrm.paladium.palatest.common.util.RegisterableWrapper.wrap;

public class ItemBigDiamondSword extends ItemSword {
    public ItemBigDiamondSword() {
        super(ToolMaterialHolder.BIG_DIAMOND);

        wrap(this).named("big_diamond_sword").defaultCreativeTab();
    }
}
