package me.xtrm.paladium.palatest.common.recipe.type;

import me.xtrm.paladium.palatest.common.recipe.IBaseRecipe;
import net.minecraft.item.ItemStack;

import java.util.*;
import java.util.function.Consumer;

/**
 * Implementation of {@link IBaseRecipe} for a Minecraft crafting table recipe.
 *
 * @author xtrm
 */
public abstract class GenericRecipe implements IBaseRecipe {
    // Shaped
    private final Map<Character, Object> bindings = new HashMap<>();
    private final String[] grid = new String[3];
    private int gridCount = 0;
    // Shapeless
    private final List<Object> inputs = new ArrayList<>();
    // Common
    public ItemStack output;
    public boolean shapeless;

    public GenericRecipe() {
        this.declareRecipe();
    }

    public void grid(String row) {
        if (gridCount >= 3)
            throw new IllegalArgumentException("Too much rows!");
        this.shapeless = false;
        grid[gridCount] = row;
        gridCount++;
    }

    public void bind(char c, Object object) {
        bindings.put(c, object);
    }

    public void input(ItemStack stack) {
        this.shapeless = true;
        this.inputs.add(stack);
    }

    @Override
    public void result(ItemStack itemStack) {
        this.output = itemStack;
    }

    public Object[] getParamArray() {
        if (shapeless) {
            int size = inputs.size();
            Object[] result = new Object[size];
            for (int i = 0; i < size; i++)
                result[i] = inputs.get(i);
            return result;
        } else {
            int size = (int) Arrays.stream(grid)
                .filter(Objects::nonNull)
                .count();
            size += bindings.size() * 2;
            Object[] result = new Object[size];
            int j = 0;
            for (String s : grid)
                if (s != null)
                    result[j++] = s;
            for (Character character : bindings.keySet()) {
                result[j++] = character;
                result[j++] = bindings.get(character);
            }
            return result;
        }
    }

    public static GenericRecipe from(Consumer<GenericRecipe> recipeConsumer) {
        GenericRecipe recipe = new GenericRecipe() {
            @Override public void declareRecipe() {}
        };
        recipeConsumer.accept(recipe);
        return recipe;
    }
}
