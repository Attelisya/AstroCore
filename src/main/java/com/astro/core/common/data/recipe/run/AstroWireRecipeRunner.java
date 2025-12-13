package com.astro.core.common.data.recipe.run;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;

import net.minecraft.data.recipes.FinishedRecipe;

import com.astro.core.common.data.recipe.generated.AstroWireRecipeHandler;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class AstroWireRecipeRunner {

    public static void init(@NotNull Consumer<FinishedRecipe> provider) {
        for (Material material : GTCEuAPI.materialManager.getRegisteredMaterials()) {
            AstroWireRecipeHandler.run(provider, material);
        }
    }
}
