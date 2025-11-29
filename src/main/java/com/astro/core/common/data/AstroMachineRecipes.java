package com.astro.core.common.data;

import com.gregtechceu.gtceu.common.data.*;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

import static com.astro.core.common.data.materials.AstroMaterials.*;
import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.gregtechceu.gtceu.common.data.GTMachines.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;
import static com.gregtechceu.gtceu.common.data.machines.GTResearchMachines.*;
import static com.gregtechceu.gtceu.data.recipe.GTCraftingComponents.*;

public class AstroMachineRecipes {

    public static void init(Consumer<FinishedRecipe> provider) {
        // AstroRecipeTypes.AETHER_ENGINE_RECIPES.recipeBuilder("burn_aether")
        // .inputFluids(AETHER.getFluid(16))
        // .duration(50)
        // .EUt(-1536)
        // .outputFluids(DEPLETED_AETHER.getFluid(1))
        // .save(provider);
    }
}
