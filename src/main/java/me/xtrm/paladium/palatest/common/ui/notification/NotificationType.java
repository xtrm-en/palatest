package me.xtrm.paladium.palatest.common.ui.notification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.xtrm.paladium.palatest.Constants;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum NotificationType {

    INFO("Info", new Color(29, 132, 238), new ResourceLocation(Constants.MODID, "textures/gui/notifications/info.png")),
    WARNING("Warning", new Color(255, 202, 17), new ResourceLocation(Constants.MODID, "textures/gui/notifications/warning.png")),
    SUCCESS("Success", new Color(12, 205, 76), new ResourceLocation(Constants.MODID, "textures/gui/notifications/success.png")),
    ERROR("Error", new Color(193, 13, 13), new ResourceLocation(Constants.MODID, "textures/gui/notifications/error.png"));

    private final String name;
    private final Color color;
    private final ResourceLocation iconLocation;

    public static NotificationType fromType(String type) {
        return Arrays.stream(values())
            .filter(t -> t.name.equalsIgnoreCase(type))
            .findFirst()
            .orElse(INFO);
    }

}
