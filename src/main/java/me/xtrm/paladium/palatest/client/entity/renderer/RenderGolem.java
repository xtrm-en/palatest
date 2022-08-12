package me.xtrm.paladium.palatest.client.entity.renderer;

import me.xtrm.paladium.palatest.Constants;
import me.xtrm.paladium.palatest.client.entity.model.ModelGolem;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderGolem extends RenderLiving {
    private static final ResourceLocation GOLEM_TEXTURE =
        new ResourceLocation(Constants.MODID, "textures/entity/golem.png");
    private static final ResourceLocation RES_ITEM_GLINT =
        new ResourceLocation("textures/misc/enchanted_item_glint.png");

    public RenderGolem() {
        super(new ModelGolem(), 0.375f);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return GOLEM_TEXTURE;
    }

    @Override
    protected void preRenderCallback(EntityLivingBase p_77041_1_, float p_77041_2_) {
        GL11.glScaled(.5, .4, .5);
    }
}
