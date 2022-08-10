package me.xtrm.paladium.palatest.common.registry.type;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * Helper abstract class.
 *
 * @author xtrm
 */
public abstract class TBlock extends Block {
    public TBlock(Material material, String unlocalizedName) {
        super(material);
        this.setBlockName(unlocalizedName);
    }
}
