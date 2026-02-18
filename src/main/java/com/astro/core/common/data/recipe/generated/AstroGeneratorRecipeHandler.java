package com.astro.core.common.data.recipe.generated;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

import static com.astro.core.common.data.materials.AstroMaterialFlags.GENERATE_COMPRESSED_SPRING;
import static com.astro.core.common.data.materials.AstroMaterialFlags.compressed_spring;
import static com.astro.core.common.data.recipe.AstroRecipeTypes.FARADAY_GENERATOR_RECIPES;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.spring;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.COMPRESSOR_RECIPES;

public class AstroGeneratorRecipeHandler {

    public static void init(Consumer<FinishedRecipe> provider) {
        GTCEuAPI.materialManager.getRegisteredMaterials().forEach(material -> {
            if (material.hasFlag(GENERATE_COMPRESSED_SPRING) &&
                    material.hasProperty(PropertyKey.WIRE)) {
                processCompressedSpring(provider, material);
            }
        });
    }

    private static void processCompressedSpring(Consumer<FinishedRecipe> provider, Material material) {
        var cableProperty = material.getProperty(PropertyKey.WIRE);
        long voltage = cableProperty.getVoltage();
        int tier = GTUtil.getFloorTierByVoltage(voltage);

        int compressDuration = 100;
        int decompressDuration = 100;

        int springChance = Math.min(9900, (tier * 1200) - 900);

        long baseEU = 4096;
        if (tier > GTValues.IV) {
            int tiersAboveIV = tier - GTValues.IV;
            baseEU = (long) (4096 * Math.pow(1.05, tiersAboveIV));
        }

        COMPRESSOR_RECIPES.recipeBuilder("compress_" + material.getName() + "_spring")
                .inputItems(spring, material)
                .outputItems(compressed_spring, material)
                .duration(compressDuration)
                .EUt(2560)
                .save(provider);

        FARADAY_GENERATOR_RECIPES.recipeBuilder("decompress_" + material.getName() + "_spring")
                .inputItems(compressed_spring, material)
                .chancedOutput(ChemicalHelper.get(spring, material), springChance, 0)
                .duration(decompressDuration)
                .EUt(-baseEU)
                .save(provider);
    }
}
