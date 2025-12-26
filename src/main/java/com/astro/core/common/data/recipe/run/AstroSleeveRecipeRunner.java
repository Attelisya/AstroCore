package com.astro.core.common.data.recipe.run;

import com.astro.core.common.data.recipe.generated.AstroSleeveRecipeHandler;
import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import net.minecraft.data.recipes.FinishedRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class AstroSleeveRecipeRunner {

    public static void init(@NotNull Consumer<FinishedRecipe> provider) {
        for (Material material : GTCEuAPI.materialManager.getRegisteredMaterials()) {
            AstroSleeveRecipeHandler.run(provider, material);
        }
    }
}
