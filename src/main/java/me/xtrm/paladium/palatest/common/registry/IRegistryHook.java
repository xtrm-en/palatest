package me.xtrm.paladium.palatest.common.registry;

/**
 * Functional interface to hook and customize the registry process.
 *
 * @param <T> the registry type
 *
 * @author xtrm
 */
@FunctionalInterface
public interface IRegistryHook<T> {
    void register(T instance, String name);
}
