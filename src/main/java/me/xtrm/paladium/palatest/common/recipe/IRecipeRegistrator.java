package me.xtrm.paladium.palatest.common.recipe;

/**
 * Base interface for custom recipe registration implementations.
 *
 * @author xtrm
 */
@FunctionalInterface
public interface IRecipeRegistrator {
    void register(IBaseRecipe recipe);
}
