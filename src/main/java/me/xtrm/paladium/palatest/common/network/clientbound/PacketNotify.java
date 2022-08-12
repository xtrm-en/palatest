package me.xtrm.paladium.palatest.common.network.clientbound;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import lombok.experimental.ExtensionMethod;
import me.xtrm.paladium.palatest.client.ui.notification.Notification;
import me.xtrm.paladium.palatest.client.ui.notification.NotificationManager;
import me.xtrm.paladium.palatest.common.ui.notification.NotificationType;
import net.minecraft.client.resources.I18n;

@ExtensionMethod(ByteBufUtils.class)
public class PacketNotify implements IMessage {
    private NotificationType type;
    private String title;
    private String message;
    private int delay;

    public PacketNotify() {}
    public PacketNotify(NotificationType type, String title, String message, int delay) {
        this.type = type;
        this.title = title;
        this.message = message;
        this.delay = delay;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.type = NotificationType.values()[buf.readInt()];
        this.title = buf.readUTF8String();
        this.message = buf.readUTF8String();
        this.delay = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.type.ordinal());
        buf.writeUTF8String(this.title);
        buf.writeUTF8String(this.message);
        buf.writeInt(this.delay);
    }

    public static class Handler implements IMessageHandler<PacketNotify, IMessage> {
        @Override
        public IMessage onMessage(PacketNotify message, MessageContext ctx) {
            if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
                call(message);
            }
            return null;
        }

        @SideOnly(Side.CLIENT)
        private void call(PacketNotify packetNotify) {
            NotificationManager.INSTANCE.queueNotification(
                new Notification(
                    packetNotify.type,
                    I18n.format(packetNotify.title),
                    I18n.format(packetNotify.message),
                    packetNotify.delay
                )
            );
        }
    }
}
