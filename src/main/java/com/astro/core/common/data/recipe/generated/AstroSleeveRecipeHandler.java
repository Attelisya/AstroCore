package com.astro.core.common.data.recipe.generated;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static com.astro.core.common.data.AstroItems.*;
import static com.astro.core.common.data.materials.AstroMaterialFlags.sleeve;
import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.ingot;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;

public class AstroSleeveRecipeHandler {

    private static final int[] VA = GTValues.VA;

    public static void run(@NotNull Consumer<FinishedRecipe> provider, @NotNull Material material) {
        processSleeve(provider, material);
    }

    private static void processSleeve(@NotNull Consumer<FinishedRecipe> provider, @NotNull Material material) {
        if (!material.shouldGenerateRecipesFor(sleeve) || !material.hasProperty(PropertyKey.INGOT)) {
            return;
        }

        FORMING_PRESS_RECIPES.recipeBuilder("forming_press_" + material.getName() + "_ingot_to_sleeve")
                .inputItems(ingot, material, 2)
                .outputItems(sleeve, material)
                .duration((int) material.getMass() * 2)
                .EUt(VA[LV])
                .save(provider);

        VanillaRecipeHelper.addShapedRecipe(provider, String.format("sleeve_%s", material.getName()),
                ChemicalHelper.get(sleeve, material),
                "fX", "hX",
                'X', new MaterialEntry(ingot, material));


        EXTRUDER_RECIPES.recipeBuilder("extruder_" + material.getName() + "ingot_to_sleeve")
                .inputItems(ingot, material, 2)
                .notConsumable(SHAPE_EXTRUDER_SLEEVE)
                .outputItems(sleeve, material)
                .duration((int) material.getMass() * 3)
                .EUt(VA[MV])
                .save(provider);


        if (material.hasFluid()) {
            FluidStack stack = material.getProperty(PropertyKey.FLUID).solidifiesFrom(L * 2);
            if (!stack.isEmpty()) {
                FLUID_SOLIDFICATION_RECIPES.recipeBuilder("solidify_" + material.getName() + "_liquid_to_sleeve")
                        .notConsumable(SHAPE_MOLD_SLEEVE)
                        .inputFluids(stack)
                        .outputItems()
                        .duration((int) material.getMass() * 12)
                        .EUt(VA[ULV])
                        .save(provider);
            }
        }
    }
}
