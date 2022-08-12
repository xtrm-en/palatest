package me.xtrm.paladium.palatest.common.recipe.impl.generic;

import me.xtrm.paladium.palatest.common.recipe.type.GenericRecipe;
import me.xtrm.paladium.palatest.common.registry.impl.BlockRegistry;
import me.xtrm.paladium.palatest.common.registry.impl.block.BlockGrinderShell;
import net.minecraft.init.Items;

public class GrinderCasingRecipe extends GenericRecipe {
    public GrinderCasingRecipe() {
        grid("did");
        grid("   ");
        grid("did");

        bind('d', Items.diamond);
        bind('i', Items.iron_ingot);

        result(BlockRegistry.INSTANCE.withType(BlockGrinderShell.class));
    }
}
