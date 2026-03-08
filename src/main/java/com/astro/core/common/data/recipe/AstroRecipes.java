package com.astro.core.common.data.recipe;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.OreProperty;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import com.astro.core.common.data.materials.AstroMaterials;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.function.Consumer;

import static com.astro.core.common.data.recipe.AstroRecipeTypes.KINETIC_COMBUSTION_RECIPES;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.CRYSTALLIZABLE;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.AUTOCLAVE_RECIPES;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.ORE_WASHER_RECIPES;

@SuppressWarnings("all")
public class AstroRecipes {

    public static void init(Consumer<FinishedRecipe> provider) {
        // output, input, duration in seconds
        add(provider, "iron_ingot_to_steel_ingot", "gtceu:steel_ingot", "minecraft:iron_ingot", 90);
        add(provider, "wrought_iron_ingot_to_steel_ingot", "gtceu:steel_ingot", "gtceu:wrought_iron_ingot", 40);
        add(provider, "iron_block_to_steel_block", "gtceu:steel_block", "minecraft:iron_block", 810);
        add(provider, "wrought_iron_block_to_steel_block", "gtceu:steel_block", "gtceu:wrought_iron_block", 360);
        add(provider, "steel_ingot_to_damascus_steel_ingot", "gtceu:damascus_steel_ingot", "gtceu:steel_ingot", 60);
        add(provider, "manasteel_dust_to_manasteel_ingot", "botania:manasteel_ingot", "gtbotania:manasteel_dust", 45);

        GTCEuAPI.materialManager.getRegisteredMaterials().forEach(material -> {
            processDust(provider, material);

            OreProperty property = material.getProperty(PropertyKey.ORE);
            if (property != null) {
                processCrushedOre(provider, property, material);
            }
        });

        KINETIC_COMBUSTION_RECIPES.recipeBuilder("naphtha")
                .inputFluids(Naphtha.getFluid(1))
                .duration(40)
                .save(provider);

        KINETIC_COMBUSTION_RECIPES.recipeBuilder("sulfuric_light_fuel")
                .inputFluids(SulfuricLightFuel.getFluid(4))
                .duration(20)
                .save(provider);

        KINETIC_COMBUSTION_RECIPES.recipeBuilder("methanol")
                .inputFluids(Methanol.getFluid(4))
                .duration(32)
                .save(provider);

        KINETIC_COMBUSTION_RECIPES.recipeBuilder("ethanol")
                .inputFluids(Ethanol.getFluid(1))
                .duration(24)
                .save(provider);

        KINETIC_COMBUSTION_RECIPES.recipeBuilder("octane")
                .inputFluids(Octane.getFluid(2))
                .duration(20)
                .save(provider);

        KINETIC_COMBUSTION_RECIPES.recipeBuilder("biodiesel")
                .inputFluids(BioDiesel.getFluid(1))
                .duration(32)
                .save(provider);

        KINETIC_COMBUSTION_RECIPES.recipeBuilder("light_fuel")
                .inputFluids(LightFuel.getFluid(1))
                .duration(40)
                .save(provider);

        KINETIC_COMBUSTION_RECIPES.recipeBuilder("diesel")
                .inputFluids(Diesel.getFluid(1))
                .duration(60)
                .save(provider);

        KINETIC_COMBUSTION_RECIPES.recipeBuilder("cetane_diesel")
                .inputFluids(CetaneBoostedDiesel.getFluid(2))
                .duration(180)
                .save(provider);

        KINETIC_COMBUSTION_RECIPES.recipeBuilder("rocket_fuel")
                .inputFluids(RocketFuel.getFluid(16))
                .duration(500)
                .save(provider);

        KINETIC_COMBUSTION_RECIPES.recipeBuilder("gasoline")
                .inputFluids(Gasoline.getFluid(1))
                .duration(200)
                .save(provider);

        KINETIC_COMBUSTION_RECIPES.recipeBuilder("high_octane_gasoline")
                .inputFluids(HighOctaneGasoline.getFluid(1))
                .duration(400)
                .save(provider);

        KINETIC_COMBUSTION_RECIPES.recipeBuilder("toluene")
                .inputFluids(Toluene.getFluid(1))
                .duration(40)
                .save(provider);

        KINETIC_COMBUSTION_RECIPES.recipeBuilder("light_oil")
                .inputFluids(OilLight.getFluid(32))
                .duration(20)
                .save(provider);

        KINETIC_COMBUSTION_RECIPES.recipeBuilder("raw_oil")
                .inputFluids(RawOil.getFluid(64))
                .duration(60)
                .save(provider);
    }

    // steam blast furnace recipe builder
    private static void add(Consumer<FinishedRecipe> provider, String id, String outputId, String inputId,
                            int seconds) {
        ResourceLocation inRL = ResourceLocation.tryParse(inputId);
        ResourceLocation outRL = ResourceLocation.tryParse(outputId);

        Item in = BuiltInRegistries.ITEM.get(inRL);
        Item out = BuiltInRegistries.ITEM.get(outRL);

        AstroRecipeTypes.STEAM_BLAST_FURNACE_RECIPES.recipeBuilder(id)
                .inputItems(in)
                .outputItems(out)
                // Duration is in ticks, i.e. 20t = 1s
                .duration(seconds * 20)
                // With conversionRate = 1:2, i.e. EUt = 3 results in 6 mB/t steam usage per parallel recipe
                .EUt(3)
                .save(provider);
    }

    // autoclave with deionized generator
    private static void processDust(@NotNull Consumer<FinishedRecipe> provider, @NotNull Material material) {
        if (!material.shouldGenerateRecipesFor(dust) || !material.hasProperty(PropertyKey.DUST)) {
            return;
        }

        String id = "dust_%s".formatted(material.getName().toLowerCase(Locale.ROOT));
        ItemStack dustStack = ChemicalHelper.get(dust, material);
        OreProperty oreProperty = material.hasProperty(PropertyKey.ORE) ? material.getProperty(PropertyKey.ORE) : null;
        if (material.hasProperty(PropertyKey.GEM)) {
            ItemStack gemStack = ChemicalHelper.get(gem, material);
            if (material.hasFlag(CRYSTALLIZABLE)) {
                AUTOCLAVE_RECIPES.recipeBuilder("autoclave_" + id + "deionized_water")
                        .inputItems(dustStack)
                        .inputFluids(AstroMaterials.DEIONIZED_WATER.getFluid(250))
                        .outputItems(gemStack)
                        .duration(600).EUt(24)
                        .save(provider);
            }
        }
    }

    // wash ores with deionized generator
    private static void processCrushedOre(@NotNull Consumer<FinishedRecipe> provider, @NotNull OreProperty property,
                                          @NotNull Material material) {
        if (!material.shouldGenerateRecipesFor(crushed)) {
            return;
        }

        ItemStack crushedPurifiedOre = GTUtil.copyFirst(
                ChemicalHelper.get(crushedPurified, material),
                ChemicalHelper.get(dust, material));
        Material byproductMaterial = property.getOreByProduct(0, material);

        ORE_WASHER_RECIPES.recipeBuilder("wash_" + material.getName() + "_crushed_ore_to_purified_ore_deionized")
                .inputItems(crushed, material)
                .inputFluids(AstroMaterials.DEIONIZED_WATER.getFluid(100))
                .outputItems(crushedPurifiedOre)
                .chancedOutput(TagPrefix.dust, byproductMaterial, 6667, 0)  // 66.67% chance
                .outputItems(TagPrefix.dust, GTMaterials.Stone)
                .duration(200)
                .save(provider);
    }
}
