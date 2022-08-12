package me.xtrm.paladium.palatest.common.recipe.type;

import me.xtrm.paladium.palatest.common.recipe.IBaseRecipe;
import net.minecraft.item.ItemStack;

import java.util.function.Consumer;

/**
 *
 */
public abstract class GrinderRecipe implements IBaseRecipe {
    public final ItemStack[] inputs = new ItemStack[2];
    private int inputCount = 0;
    public ItemStack output;
    public int levelCost = 0;
    public int ticks = 240;

    public GrinderRecipe() {
        this.declareRecipe();
    }

    public void level(int level) {
        this.levelCost = level;
    }

    public void ticks(int ticks) {
        this.ticks = ticks;
    }

    @Override
    public void input(ItemStack itemStack) {
        if (inputCount >= 2)
            throw new IllegalArgumentException("Can't have more than 2 inputs!");
        inputs[inputCount++] = itemStack;
    }

    @Override
    public void result(ItemStack itemStack) {
        this.output = itemStack;
    }

    public static GrinderRecipe from(Consumer<GrinderRecipe> recipeConsumer) {
        GrinderRecipe recipe = new GrinderRecipe() {
            @Override public void declareRecipe() {}
        };
        recipeConsumer.accept(recipe);
        return recipe;
    }
}
