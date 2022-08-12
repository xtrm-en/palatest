package me.xtrm.paladium.palatest.client.ui.notification;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public enum NotificationManager {
    INSTANCE;

    private final LinkedBlockingQueue<Notification> queue = new LinkedBlockingQueue<>();
    private final List<Notification> currentNotifications = new ArrayList<>();

    @SubscribeEvent
    public void onRender2D(RenderGameOverlayEvent.Pre event) {
        if (event.type != RenderGameOverlayEvent.ElementType.HELMET)
            return;
        int yOffset = 0;
        for (Notification notification : currentNotifications) {
            notification.render(yOffset);
            yOffset += (1 - notification.getSlideOutAnimation().getValue(false)) * (notification.getHeight() + Notification.MARGIN_Y);
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent eventTick) {
        if (eventTick.phase != TickEvent.Phase.START)
            return;

        currentNotifications.removeIf(Notification::isFinished);

        while (currentNotifications.size() < getMaxNotifications()) {
            if (queue.isEmpty()) break;
            currentNotifications.add(queue.poll());
        }
    }

    private int getMaxNotifications() {
        return 3;
    }

    public void queueNotification(Notification notification) {
        synchronized (queue) {
            queue.add(notification);
        }
    }
}
