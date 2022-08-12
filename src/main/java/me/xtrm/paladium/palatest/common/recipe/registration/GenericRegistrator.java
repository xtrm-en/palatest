package me.xtrm.paladium.palatest.common.recipe.registration;

import cpw.mods.fml.common.registry.GameRegistry;
import me.xtrm.paladium.palatest.common.recipe.IBaseRecipe;
import me.xtrm.paladium.palatest.common.recipe.IRecipeRegistrator;
import me.xtrm.paladium.palatest.common.recipe.type.GenericRecipe;

public enum GenericRegistrator implements IRecipeRegistrator {
    INSTANCE;

    public void register(GenericRecipe recipe) {
        if (recipe.shapeless)
            GameRegistry.addShapelessRecipe(recipe.output, recipe.getParamArray());
        else
            GameRegistry.addRecipe(recipe.output, recipe.getParamArray());
    }

    @Override
    public void register(IBaseRecipe recipe) {
        register((GenericRecipe) recipe);
    }
}
