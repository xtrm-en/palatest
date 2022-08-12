package me.xtrm.paladium.palatest.common.recipe.impl.generic;

import me.xtrm.paladium.palatest.common.recipe.type.GenericRecipe;
import me.xtrm.paladium.palatest.common.registry.impl.BlockRegistry;
import me.xtrm.paladium.palatest.common.registry.impl.block.BlockGrinderShell;
import net.minecraft.init.Items;

public class GrinderFrameRecipe extends GenericRecipe {
    public GrinderFrameRecipe() {
        grid("d d");
        grid("d d");
        grid("d d");

        bind('d', Items.diamond);

        result(
            BlockRegistry.INSTANCE.withType(BlockGrinderShell.class),
            1,
            1
        );
    }
}
