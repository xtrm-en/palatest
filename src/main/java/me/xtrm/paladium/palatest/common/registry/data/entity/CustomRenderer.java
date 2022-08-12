package me.xtrm.paladium.palatest.common.registry.data.entity;

import net.minecraft.client.renderer.entity.Render;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CustomRenderer {
    /**
     * @return the custom renderer class used for this entity.
     */
    Class<? extends Render> value();
}
