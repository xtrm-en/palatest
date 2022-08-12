package me.xtrm.paladium.palatest.common.extended;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

@Getter
@RequiredArgsConstructor
public class PalaPlayer implements IExtendedEntityProperties {
    public final static String IDENTIFIER = "PalaPlayer";
    private final EntityPlayer player;

    public int golemKills = 0;

    @Override
    public void init(Entity entity, World world) {
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        NBTTagCompound properties = new NBTTagCompound();
        properties.setInteger("GolemKills", golemKills);
        compound.setTag(IDENTIFIER, properties);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        NBTTagCompound properties = (NBTTagCompound) compound.getTag(IDENTIFIER);
        this.golemKills = properties.getInteger("GolemKills");
    }

    public static void register(EntityPlayer player) {
        player.registerExtendedProperties(PalaPlayer.IDENTIFIER, new PalaPlayer(player));
    }

    public static PalaPlayer get(EntityPlayer player) {
        return (PalaPlayer) player.getExtendedProperties(IDENTIFIER);
    }
}
