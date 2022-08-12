package me.xtrm.paladium.palatest.common.registry;

@FunctionalInterface
public interface IRegistryHook<T> {
    void register(T instance, String name);
}
