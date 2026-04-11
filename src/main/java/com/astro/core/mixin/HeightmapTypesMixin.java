package com.astro.core.mixin;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

import com.astro.core.common.data.tag.AstroBlockTags;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(value = Heightmap.Types.class, remap = true)
public class HeightmapTypesMixin {

    @Shadow
    @Final
    @Mutable
    private Predicate<BlockState> isOpaque;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void inject$init(String key, int usage, String opaque, Heightmap.Usage usageType,
                             Predicate<BlockState> predicate, CallbackInfo ci) {
        if (key.equals("OCEAN_FLOOR")) {
            final Predicate<BlockState> finalPredicate = predicate;
            isOpaque = state -> finalPredicate.test(state) && !state.is(AstroBlockTags.HEIGHTMAP_IGNORE);
        }
    }
}
