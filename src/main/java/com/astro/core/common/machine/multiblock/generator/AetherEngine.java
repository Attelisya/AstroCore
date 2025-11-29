package com.astro.core.common.machine.multiblock.generator;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;

import com.astro.core.AstroCore;
import com.astro.core.common.data.AstroRecipeTypes;
import com.astro.core.common.data.block.AstroBlocks;

import static com.astro.core.common.machine.multiblock.generator.AstroTurbines.registerAstroTurbine;

@SuppressWarnings("unused")

public class AetherEngine {

    public static final MultiblockMachineDefinition AETHER_ENGINE = registerAstroTurbine(
            "aether_turbine",
            GTValues.EV,
            AstroRecipeTypes.AETHER_ENGINE_RECIPES,
            AstroBlocks.ALFSTEEL_MACHINE_CASING,
            AstroBlocks.ALFSTEEL_GEARBOX_CASING,
            AstroCore.id("block/generators/machine_casing_turbine_alfsteel"),
            AstroCore.id("block/multiblock/aether_engine"), false);

    public static void init() {}
}
