package com.astro.core.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Slime;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.astro.core.AstroCore;
import net.minecraft.client.renderer.entity.SlimeRenderer;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("all")
public class KuiperSlimeRenderer extends SlimeRenderer {

    private static final ResourceLocation TEXTURE =
            AstroCore.id("textures/mob/kuiper_slime.png");

    public KuiperSlimeRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(Slime entity) {
        return TEXTURE;
    }
}