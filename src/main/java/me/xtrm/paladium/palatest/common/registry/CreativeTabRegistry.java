package me.xtrm.paladium.palatest.common.registry;

import net.minecraft.creativetab.CreativeTabs;

public enum CreativeTabRegistry implements IRegistry<CreativeTabs> {
    INSTANCE;

    CreativeTabRegistry() {
    }

    @Override
    public void registerAll() {

    }

    @Override
    public CreativeTabs getByName(String string) {
        return null;
    }
}
