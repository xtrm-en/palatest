package me.xtrm.paladium.palatest.common.recipe.impl.generic;

import me.xtrm.paladium.palatest.common.recipe.type.GenericRecipe;
import me.xtrm.paladium.palatest.common.registry.impl.ItemRegistry;
import me.xtrm.paladium.palatest.common.registry.impl.item.ItemSocketPattern;
import net.minecraft.init.Items;

public class SocketPatternRecipe extends GenericRecipe {
    public SocketPatternRecipe() {
        grid("idi");
        grid("d d");
        grid("idi");

        bind('i', Items.iron_ingot);
        bind('d', Items.diamond);

        result(ItemRegistry.INSTANCE.withType(ItemSocketPattern.class));
    }
}
