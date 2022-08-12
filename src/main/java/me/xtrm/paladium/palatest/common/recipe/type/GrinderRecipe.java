package me.xtrm.paladium.palatest.common.recipe.type;

import me.xtrm.paladium.palatest.common.recipe.IBaseRecipe;
import net.minecraft.item.ItemStack;

/**
 *
 */
public class GrinderRecipe implements IBaseRecipe {
    public final ItemStack[] inputs = new ItemStack[2];
    private int inputCount = 0;
    public ItemStack output;
    public int levelCost = 0;
    public int ticks = 240;

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
}
