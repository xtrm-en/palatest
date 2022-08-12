package me.xtrm.paladium.palatest.common.registry;

import java.util.List;

@SuppressWarnings("unchecked")
public interface IRegistry<T> {
    void registerAll();

    List<T> all(String name);

    default T byName(String name) {
        List<T> results = all(name);
        return results.isEmpty() ? null : results.get(0);
    }

    default <V extends T> V withType(Class<V> clazz, V... reified) {
        throw new UnsupportedOperationException(
            "Cannot get a "
                + reified.getClass().getComponentType().getSimpleName()
                + "by type."
        );
    }
}
