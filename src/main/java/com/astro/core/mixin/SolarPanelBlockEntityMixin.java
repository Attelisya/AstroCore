package com.astro.core.mixin;

import earth.terrarium.adastra.common.blockentities.machines.SolarPanelBlockEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SolarPanelBlockEntity.class)
public class SolarPanelBlockEntityMixin {

    @Inject(method = "isDay", at = @At("HEAD"), cancellable = true, remap = false)
    private void adastra$isDay(CallbackInfoReturnable<Boolean> cir) {
        SolarPanelBlockEntity self = (SolarPanelBlockEntity) (Object) this;
        Level level = self.getLevel();
        if (level == null) return;
        if (level.dimension().location().toString().equals("ad_astra:kuiper_belt")) {
            cir.setReturnValue(true);
        }
    }
}