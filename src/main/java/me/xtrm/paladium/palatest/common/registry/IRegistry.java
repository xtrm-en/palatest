package me.xtrm.paladium.palatest.common.registry;

import me.xtrm.paladium.palatest.common.registry.impl.ItemRegistry;

import java.util.List;

/**
 * Abstract components for a registry implementation.
 *
 * @see ItemRegistry
 *
 * @param <T> the type to be registered
 */
public interface IRegistry<T> {
    void registerAll();

    List<T> all(String name);

    default T byName(String name) {
        List<T> results = all(name);
        return results.isEmpty() ? null : results.get(0);
    }

    @SuppressWarnings("unchecked")
    default <V extends T> V withType(Class<V> clazz, V... reified) {
        throw new UnsupportedOperationException(
            "Cannot get a "
                + reified.getClass().getComponentType().getSimpleName()
                + "by type."
        );
    }
}
