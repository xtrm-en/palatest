package me.xtrm.paladium.palatest.common.recipe.impl.grinder;

import me.xtrm.paladium.palatest.common.recipe.type.GrinderRecipe;
import me.xtrm.paladium.palatest.common.registry.impl.ItemRegistry;
import me.xtrm.paladium.palatest.common.registry.impl.item.ItemBigDiamondSword;
import me.xtrm.paladium.palatest.common.registry.impl.item.ItemSocketPattern;
import net.minecraft.init.Items;

public class BigDiamondSwordRecipe extends GrinderRecipe {
    @Override
    public void declareRecipe() {
        input(Items.diamond_sword);
        input(ItemRegistry.INSTANCE.withType(ItemSocketPattern.class));

        ticks(200);
        level(20);

        result(ItemRegistry.INSTANCE.withType(ItemBigDiamondSword.class));
    }
}
