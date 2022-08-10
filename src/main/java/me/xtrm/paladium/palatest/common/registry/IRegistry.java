package me.xtrm.paladium.palatest.common.registry;

import java.util.List;

@SuppressWarnings("unchecked")
public interface IRegistry<T> {
    void registerAll();

    List<T> getAll(String name);

    default T getByName(String name) {
        List<T> results = getAll(name);
        return results.isEmpty() ? null : results.get(0);
    }

    default T getByType(Class<? extends T> clazz, T... reified) {
        throw new UnsupportedOperationException(
            "Cannot get a "
                + reified.getClass().getComponentType().getSimpleName()
                + "by type."
        );
    }

    default <V extends T> T getByType(V... reified) {
        return this.getByType(
            (Class<? extends T>) reified.getClass().getComponentType()
        );
    }
}
