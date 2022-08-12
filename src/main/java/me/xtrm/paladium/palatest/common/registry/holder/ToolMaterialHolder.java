package me.xtrm.paladium.palatest.common.registry.holder;

import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;

public class ToolMaterialHolder {
    public static final Item.ToolMaterial BIG_DIAMOND =
        EnumHelper.addToolMaterial(
            "big_diamond",
            3,
            2743,
            10.0F,
            4.5F,
            14
        );

    /**
     * Don't initialize a holder-class.
     */
    private ToolMaterialHolder() {
        throw new Error();
    }
}
