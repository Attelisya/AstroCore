package com.astro.core.common.data.machine.conditions;

import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeCondition;
import com.gregtechceu.gtceu.api.recipe.condition.RecipeConditionType;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

import com.astro.core.common.data.machine.AstroRecipeConditions;
import com.astro.core.common.machine.trait.OxygenatedProviderTrait;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import earth.terrarium.adastra.api.systems.OxygenApi;
import org.jetbrains.annotations.NotNull;

/**
 * Oxygen is checked on all six adjacent faces of the machine block.
 * - requiresOxygen = true -> recipe runs only when oxygen is present nearby.
 * - requiresOxygen = false -> recipe runs only when no oxygen is present nearby.
 * Inspired by TFG's OxygenatedCondition.
 */

@SuppressWarnings("all")
public class OxygenatedCondition extends RecipeCondition<OxygenatedCondition> {

    @Override
    public boolean testCondition(@NotNull GTRecipe recipe, @NotNull RecipeLogic recipeLogic) {
        var machine = recipeLogic.machine.self();
        var level = machine.getLevel();

        if (!(level instanceof ServerLevel serverLevel)) return false;

        if (requiresOxygen) {
            boolean providedByHatch = machine.getTraits().stream()
                    .filter(OxygenatedProviderTrait.class::isInstance)
                    .map(OxygenatedProviderTrait.class::cast)
                    .findFirst()
                    .map(OxygenatedProviderTrait::isActive)
                    .orElse(false);
            if (providedByHatch) return true;
        }

        BlockPos pos = machine.getPos();
        boolean hasAdjacentOxygen = hasOxygenOnAnySide(serverLevel, pos);
        return requiresOxygen == hasAdjacentOxygen;
    }

    public static final Codec<OxygenatedCondition> CODEC = RecordCodecBuilder
            .create(instance -> RecipeCondition.isReverse(instance)
                    .and(Codec.BOOL.fieldOf("requiresOxygen").forGetter(cond -> cond.requiresOxygen))
                    .apply(instance, OxygenatedCondition::new));

    private final boolean requiresOxygen;

    public OxygenatedCondition() {
        super(false);
        this.requiresOxygen = true;
    }

    /**
     * @param isReverse      inverts the result of the check
     * @param requiresOxygen true passes when oxygen is present, false passes when absent.
     */
    public OxygenatedCondition(boolean isReverse, boolean requiresOxygen) {
        super(isReverse);
        this.requiresOxygen = requiresOxygen;
    }

    @Override
    public RecipeConditionType<OxygenatedCondition> getType() {
        return AstroRecipeConditions.OXYGENATED;
    }

    @Override
    public boolean isOr() {
        return true;
    }

    @Override
    public Component getTooltips() {
        return Component.translatable(
                requiresOxygen ? "astrogreg.recipe_condition.oxygenated.requires" :
                        "astrogreg.recipe_condition.oxygenated.requires_not");
    }

    /** Returns true if any of the six adjacent blocks has oxygen according to Ad Astra. */
    private static boolean hasOxygenOnAnySide(ServerLevel level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            if (OxygenApi.API.hasOxygen(level, pos.relative(direction))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public OxygenatedCondition createTemplate() {
        return new OxygenatedCondition();
    }
}
