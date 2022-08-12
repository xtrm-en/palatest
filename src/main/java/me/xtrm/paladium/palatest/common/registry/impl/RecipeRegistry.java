package me.xtrm.paladium.palatest.common.registry.impl;

import me.xtrm.paladium.palatest.PalaTest;
import me.xtrm.paladium.palatest.common.recipe.IBaseRecipe;
import me.xtrm.paladium.palatest.common.recipe.IRecipeRegistrator;
import me.xtrm.paladium.palatest.common.recipe.impl.generic.GrinderCasingRecipe;
import me.xtrm.paladium.palatest.common.recipe.impl.generic.GrinderFrameRecipe;
import me.xtrm.paladium.palatest.common.recipe.impl.generic.GrinderPanelRecipe;
import me.xtrm.paladium.palatest.common.recipe.impl.generic.SocketPatternRecipe;
import me.xtrm.paladium.palatest.common.recipe.impl.grinder.BigDiamondSwordRecipe;
import me.xtrm.paladium.palatest.common.recipe.registration.GenericRegistrator;
import me.xtrm.paladium.palatest.common.recipe.type.GenericRecipe;
import me.xtrm.paladium.palatest.common.registry.IRegistry;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public enum RecipeRegistry implements IRegistry<IBaseRecipe> {
    INSTANCE;

    private final Map<Class<? extends IBaseRecipe>, IRecipeRegistrator> recipeRegistratorMap;
    private final List<IBaseRecipe> recipeList;

    RecipeRegistry() {
        this.recipeRegistratorMap = new HashMap<>();
        this.recipeRegistratorMap.put(GenericRecipe.class, GenericRegistrator.INSTANCE);

        this.recipeList = new ArrayList<>();
    }

    @Override
    public void registerAll() {
        Arrays.asList(
            new GrinderCasingRecipe(),
            new GrinderFrameRecipe(),
            new GrinderPanelRecipe(),
            new SocketPatternRecipe(),

            new BigDiamondSwordRecipe()
        ).forEach(recipe -> {
            Optional<? extends IRecipeRegistrator> registrator =
                this.recipeRegistratorMap.entrySet()
                    .stream()
                    .filter(it -> it.getKey().isInstance(recipe))
                    .map(Map.Entry::getValue)
                    .findFirst();
            PalaTest.INSTANCE.getLogger().info(
                "Registering {} \"{}\".",
                recipe.getClass().getSuperclass().getSimpleName(),
                recipe.getClass().getSimpleName()
            );
            registrator.ifPresent(it -> it.register(recipe));
            this.recipeList.add(recipe);
        });
    }

    @Override
    public List<IBaseRecipe> all(String name) {
        throw new UnsupportedOperationException(
            "Cannot get a recipe by name."
        );
    }

    public <T extends IBaseRecipe> List<T> ofType(Class<T> clazz) {
        return (List<T>) this.recipeList.stream()
            .filter(clazz::isInstance)
            .collect(Collectors.toList());
    }
}
