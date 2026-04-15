package com.astro.core.mixin;

import com.gregtechceu.gtceu.common.machine.steam.SteamSolarBoiler;
import com.gregtechceu.gtceu.utils.GTUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = SteamSolarBoiler.class, remap = false)
public class SteamSolarBoilerMixin {

    @Redirect(
            method = "updateCurrentTemperature",
            at = @At(value = "INVOKE", target = "Lcom/gregtechceu/gtceu/utils/GTUtil;canSeeSunClearly(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z")
    )
    private boolean redirectCanSeeSunClearly(Level level, BlockPos pos) {
        if (level.dimension().location().toString().equals("ad_astra:kuiper_belt")) return true;
        return GTUtil.canSeeSunClearly(level, pos);
    }
}