package com.astro.core.common.data.worldgen;

import com.astro.core.common.data.worldgen.oregen.PlutoOres;

public class AstroWorldgen {

    public static void init() {
        AstroWorldGenLayers.init();
        AstroBiomes.init();
        AstroFeatures.init();
    }

    public static void lateInit() {
        PlutoOres.init();
    }
}