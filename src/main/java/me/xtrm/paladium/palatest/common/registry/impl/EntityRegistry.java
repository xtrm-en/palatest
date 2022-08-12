package me.xtrm.paladium.palatest.common.registry.impl;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import me.xtrm.paladium.palatest.PalaTest;
import me.xtrm.paladium.palatest.common.registry.IRegistry;
import me.xtrm.paladium.palatest.common.registry.data.entity.CustomRenderer;
import me.xtrm.paladium.palatest.common.registry.impl.entity.EntityGolem;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static cpw.mods.fml.common.registry.EntityRegistry.*;

public enum EntityRegistry implements IRegistry<Class<? extends Entity>> {
    INSTANCE;

    private final Map<String, Class<? extends Entity>> entityMap;

    EntityRegistry() {
        this.entityMap = new HashMap<>();
        this.entityMap.put("golem", EntityGolem.class);
    }

    @Override
    public void registerAll() {
        AtomicInteger baseID = new AtomicInteger(findGlobalUniqueEntityId());
        boolean isClient = FMLCommonHandler.instance().getSide() == Side.CLIENT;

        this.entityMap.forEach((id, clazz) -> {
            PalaTest.INSTANCE.getLogger().info(
                "Registering entity \"{}\".",
                id
            );

            registerGlobalEntityID(
                clazz,
                id,
                baseID.get(),
                0x0,
                0x0
            );
            registerModEntity(
                clazz,
                id,
                baseID.getAndIncrement(),
                PalaTest.INSTANCE,
                80,
                1,
                false
            );

            if (!isClient)
                return;
            CustomRenderer renderer =
                clazz.getDeclaredAnnotation(CustomRenderer.class);
            if (renderer == null)
                return;
            Class<? extends Render> renderClazz = renderer.value();
            if (renderClazz == null)
                return;
            try {
                Render render = renderClazz.getConstructor().newInstance();
                RenderingRegistry.registerEntityRenderingHandler(
                    clazz,
                    render
                );
            } catch (ReflectiveOperationException e) {
                PalaTest.INSTANCE.getLogger().error(
                    "Couldn't initialize custom client renderer for entity {}.",
                    id,
                    e
                );
            }
        });
    }

    @Override
    public List<Class<? extends Entity>> all(String name) {
        return this.entityMap.entrySet()
            .stream()
            .filter(it -> it.getKey().equalsIgnoreCase(name))
            .map(Map.Entry::getValue)
            .collect(Collectors.toList());
    }
}
