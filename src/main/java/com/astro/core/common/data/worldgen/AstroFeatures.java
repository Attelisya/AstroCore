package com.astro.core.common.data.worldgen;

import com.astro.core.AstroCore;
import com.astro.core.common.data.worldgen.feature.StoneColumnsConfiguration;
import com.astro.core.common.data.worldgen.feature.StoneColumnsFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AstroFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(ForgeRegistries.FEATURES, AstroCore.MOD_ID);

    public static final RegistryObject<Feature<StoneColumnsConfiguration>> STONE_COLUMNS =
            FEATURES.register("stone_columns",
                    () -> new StoneColumnsFeature(StoneColumnsConfiguration.CODEC));

    public static void init() {}
}