package com.astro.core.client;

import com.astro.core.client.renderer.entity.KuiperSlimeRenderer;
import com.astro.core.common.data.AstroEntities;
import com.gregtechceu.gtceu.client.renderer.machine.DynamicRenderManager;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.astro.core.AstroCore;
import com.astro.core.client.renderer.machine.AEMultiPartRender;
import com.astro.core.client.renderer.machine.AstroBoilerMultiPartRender;
import com.astro.core.client.renderer.machine.AstroFluidRender;

@SuppressWarnings("all")
@Mod.EventBusSubscriber(modid = AstroCore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class AstroClient {

    private AstroClient() {}

    public static void init(IEventBus modBus) {
        DynamicRenderManager.register(AstroCore.id("astro_fluid_render"), AstroFluidRender.TYPE);
        DynamicRenderManager.register(AstroCore.id("astro_boiler_render"), AstroBoilerMultiPartRender.TYPE);
        DynamicRenderManager.register(AstroCore.id("ae_multi_parts"), AEMultiPartRender.TYPE);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(AstroEntities.KUIPER_SLIME.get(), KuiperSlimeRenderer::new);
    }

}
