package me.xtrm.paladium.palatest.common.recipe.impl.generic;

import me.xtrm.paladium.palatest.common.recipe.type.GenericRecipe;
import me.xtrm.paladium.palatest.common.registry.impl.BlockRegistry;
import me.xtrm.paladium.palatest.common.registry.impl.block.BlockGrinder;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;

public class GrinderPanelRecipe extends GenericRecipe {
    public GrinderPanelRecipe() {
        grid("ddd");
        grid("dfd");
        grid("ddd");

        bind('d', Items.diamond);
        bind('f', Blocks.furnace);

        result(BlockRegistry.INSTANCE.withType(BlockGrinder.class));
    }
}
