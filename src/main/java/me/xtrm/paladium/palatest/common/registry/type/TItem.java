package me.xtrm.paladium.palatest.common.registry.type;

import me.xtrm.paladium.palatest.Constants;
import me.xtrm.paladium.palatest.common.registry.CreativeTabRegistry;
import net.minecraft.item.Item;

public abstract class TItem extends Item {
    public TItem(String itemName) {
        this(itemName, Constants.MODID + ":" + itemName);
    }

    public TItem(String unlocalizedName, String textureName) {
        super();
        this.setUnlocalizedName(unlocalizedName);
        this.setTextureName(textureName);
        this.setCreativeTab(CreativeTabRegistry.INSTANCE.getByName("palatest"));
    }
}
