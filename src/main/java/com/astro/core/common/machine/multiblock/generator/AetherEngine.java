package com.astro.core.common.machine.multiblock.generator;

import com.astro.core.AstroCore;
import com.astro.core.common.data.AstroRecipeTypes;
import com.astro.core.common.data.block.AstroBlocks;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;

import static com.astro.core.common.data.block.AstroBlocks.ALFSTEEL_GEARBOX_CASING;
import static com.gregtechceu.gtceu.common.data.machines.GTMachineUtils.registerLargeTurbine;

public class AetherEngine {

    public static final MultiblockMachineDefinition AETHER_ENGINE = registerLargeTurbine(
            "aether_turbine",
            GTValues.IV,
            AstroRecipeTypes.AETHER_ENGINE_RECIPES
            () -> AstroBlocks.ALFSTEEL_MACHINE_CASING.get(),
            () -> AstroBlocks.ALFSTEEL_GEARBOX_CASING.get(),
            AstroCore.id("block/generators/machine_casing_turbine_alfsteel"),
            AstroCore.id("block/multiblock/generator/large_plasma_turbine")
            )

}
