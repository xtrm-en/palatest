package me.xtrm.paladium.palatest.client.ui.gui.grinder;

import me.xtrm.paladium.palatest.Constants;
import me.xtrm.paladium.palatest.common.registry.impl.container.ContainerGrinder;
import me.xtrm.paladium.palatest.common.registry.impl.tile.TileEntityGrinder;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.Collections;

public class GuiGrinder extends GuiContainer {
    private static final ResourceLocation BACKGROUND =
        new ResourceLocation(Constants.MODID, "textures/gui/container/grinder.png");
    private final TileEntityGrinder tileEntityGrinder;

    public GuiGrinder(InventoryPlayer inventoryPlayer, TileEntityGrinder tileEntityGrinder) {
        super(new ContainerGrinder(inventoryPlayer, tileEntityGrinder));
        this.tileEntityGrinder = tileEntityGrinder;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!this.tileEntityGrinder.locked) {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        if (mouseX > k + 9 && mouseY >= l + 8 && mouseX <= k + 23 && mouseY <= l + 50) {
            drawHoveringText(
                Collections.singletonList(
                    I18n.format(
                        "gui.grinder.level",
                        this.tileEntityGrinder.level
                    )
                ),
                mouseX,
                mouseY,
                mc.fontRendererObj
            );
        }
        if (!this.tileEntityGrinder.locked)
            return;
        if (mouseX > k + 79 && mouseY >= l + 32 && mouseX <= k + 79 + 23 && mouseY <= l + 32 + 19) {
            drawHoveringText(
                Collections.singletonList(
                    EnumChatFormatting.RED + I18n.format(
                        "title.grinder.locked"
                    )
                ),
                mouseX,
                mouseY,
                mc.fontRendererObj
            );
        }
    }

    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        String s = I18n.format(this.tileEntityGrinder.getInventoryName());
        if (this.tileEntityGrinder.locked) {
            s = EnumChatFormatting.RED + "[ " + EnumChatFormatting.RESET +
                s +
                EnumChatFormatting.RED + " ]";
        }
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4d(1, 1, 1, 1);

        this.mc.getTextureManager().bindTexture(BACKGROUND);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

        if (this.tileEntityGrinder.level > 0) {
            int height = (int) ((float) 42.0 * Math.min(
                1.0, (float) this.tileEntityGrinder.level / 100.0F
            ));
            int startY = 42 - height;
            drawTexturedModalRect(
                k + 9,
                l + 8 + startY,
                176,
                19 + startY,
                14,
                height
            );
        }
        if (this.tileEntityGrinder.progress > 0) {
            int width = (int) ((float) 23.0 * Math.min(
                1.0,
                (float) this.tileEntityGrinder.progress
                    / this.tileEntityGrinder.findCurrentRecipe().ticks
            ));
            int startX = 23 - width;
            if (this.tileEntityGrinder.locked) {
                GL11.glColor4d(1, .1, .125, 1);
            }
            drawTexturedModalRect(
                k + 79 + startX,
                l + 32,
                176 + startX,
                0,
                width,
                19
            );
        }
    }
}
