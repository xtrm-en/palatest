package me.xtrm.paladium.palatest.common.extended;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityEvent;

/**
 * Manages the registration of our custom Extended Entity Properties.
 *
 * @see PalaPlayer
 *
 * @author xtrm
 */
public enum PalaPlayerManager {
    INSTANCE;

    @SubscribeEvent
    public void onEntityConstructing(EntityEvent.EntityConstructing event) {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
            if (event.entity instanceof EntityPlayerMP && PalaPlayer.get((EntityPlayerMP) event.entity) == null)
                PalaPlayer.register((EntityPlayerMP) event.entity);
    }
}
