package com.astro.core.mixin;

import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.item.DataItemBehavior;
import com.gregtechceu.gtceu.utils.ResearchManager;
import com.astro.core.common.data.recipe.AstroRecipeTypes;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.List;

@Mixin(DataItemBehavior.class)
public class DataItemBehaviorMixin {

    @Inject(method = "appendHoverText", at = @At("TAIL"), remap = false)
    private void astro$addPlanetTooltip(ItemStack stack, @Nullable Level level,
                                        List<Component> tooltipComponents, TooltipFlag isAdvanced,
                                        CallbackInfo ci) {
        if (!stack.hasTag()) return;
        var researchData = ResearchManager.readResearchId(stack);
        if (researchData == null || researchData.recipeType() != AstroRecipeTypes.OBSERVATORY_RECIPES) return;
        Collection<GTRecipe> recipes = researchData.recipeType().getDataStickEntry(researchData.researchId());
        if (recipes == null || recipes.isEmpty()) return;
        String planetName = researchData.researchId();
        for (var recipe : recipes) {
            String name = recipe.data.getString(AstroRecipeTypes.OBSERVATORY_PLANET_NAME_KEY);
            if (!name.isEmpty()) { planetName = name; break; }
        }
        if (!tooltipComponents.isEmpty()) tooltipComponents.remove(tooltipComponents.size() - 1);
        tooltipComponents.add(Component.translatable("astrogreg.item.planetary_data.entry", planetName));
    }
}