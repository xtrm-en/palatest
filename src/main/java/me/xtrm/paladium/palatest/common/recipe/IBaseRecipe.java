package me.xtrm.paladium.palatest.common.recipe;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Base interface for a recipe.
 * <p>
 * This class contains a special DSL for declaring recipes, as well
 * as some wrapper method to aid with polymorphic item declarations.
 *
 * @author xtrm
 */
public interface IBaseRecipe {

    void input(ItemStack itemStack);

    void result(ItemStack itemStack);

    default void input(Item item) {
        input(item, 1, 0);
    }

    default void input(Block block) {
        input(block, 1, 0);
    }

    default void input(Item item, int stackSize, int meta) {
        input(new ItemStack(item, stackSize, meta));
    }

    default void input(Block block, int stackSize, int meta) {
        input(Item.getItemFromBlock(block), stackSize, meta);
    }

    default void result(Item item, int stackSize, int meta) {
        result(new ItemStack(item, stackSize, meta));
    }

    default void result(Block block, int stackSize, int meta) {
        result(Item.getItemFromBlock(block), stackSize, meta);
    }

    default void result(Item item) {
        result(item, 1, 0);
    }

    default void result(Block block) {
        result(block, 1, 0);
    }
}
