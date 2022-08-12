package me.xtrm.paladium.palatest.common.network;

import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import me.xtrm.paladium.palatest.Constants;
import me.xtrm.paladium.palatest.common.network.clientbound.PacketNotify;

public class NetworkManager extends SimpleNetworkWrapper {
    private static int IDENTIFIER = 0;
    public NetworkManager() {
        super(Constants.MODID);
    }

    public void registerAll() {
        this.registerMessage(PacketNotify.Handler.class, PacketNotify.class, IDENTIFIER++, Side.CLIENT);
    }
}
