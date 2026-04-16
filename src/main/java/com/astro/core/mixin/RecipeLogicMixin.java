package com.astro.core.mixin;

import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMufflerMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredPartMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(value = RecipeLogic.class, remap = false)
public abstract class RecipeLogicMixin {

    @Shadow(remap = false)
    @Final
    public IRecipeLogicMachine machine;

    @ModifyVariable(method = "setupRecipe", at = @At("HEAD"), argsOnly = true, remap = false)
    private GTRecipe astro$core$applyCompoundingMufflerBonus(GTRecipe recipe) {
        if (!(this.machine instanceof MetaMachine metaMachine) ||
                !(metaMachine instanceof WorkableMultiblockMachine multiMachine)) {
            return recipe;
        }

        int bestMufflerTier = -1;
        for (var part : multiMachine.getParts()) {
            if (part instanceof IMufflerMachine && part instanceof TieredPartMachine tieredPart) {
                bestMufflerTier = Math.max(bestMufflerTier, tieredPart.getTier());
            }
        }

        int bonusTiers = Math.max(0, bestMufflerTier - 1);
        if (bonusTiers <= 0) {
            return recipe;
        }

        double multiplier = Math.max(0.01D, 1.0D / Math.pow(1.02D, bonusTiers));
        GTRecipe adjusted = recipe.copy();
        if (!astro$core$scaleEnergyContents(adjusted.inputs, multiplier) &&
                !astro$core$scaleEnergyContents(adjusted.tickInputs, multiplier)) {
            return recipe;
        }
        return adjusted;
    }

    private static boolean astro$core$scaleEnergyContents(Map<?, List<Content>> contents, double multiplier) {
        @SuppressWarnings("unchecked")
        List<Content> euContents = (List<Content>) contents.get(EURecipeCapability.CAP);
        if (euContents == null || euContents.isEmpty()) {
            return false;
        }

        List<Content> adjusted = new ArrayList<>(euContents.size());
        ContentModifier modifier = ContentModifier.multiplier(multiplier);
        for (Content content : euContents) {
            adjusted.add(content.copyChanced(EURecipeCapability.CAP, modifier));
        }

        @SuppressWarnings("unchecked")
        Map<Object, List<Content>> rawContents = (Map<Object, List<Content>>) contents;
        rawContents.put(EURecipeCapability.CAP, adjusted);
        return true;
    }
}
