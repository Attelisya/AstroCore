package com.astro.core.common.data;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.common.data.GTSoundEntries;

import com.lowdragmc.lowdraglib.gui.texture.ProgressTexture;

import com.astro.core.client.AstroClient.AstroGUITextures;

import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;
import static com.lowdragmc.lowdraglib.gui.texture.ProgressTexture.FillDirection.DOWN_TO_UP;

public class AstroRecipeTypes {

    public static GTRecipeType AETHER_ENGINE_RECIPES;
    public static GTRecipeType MANA_BOILER_RECIPES;

    public static void init() {
        AETHER_ENGINE_RECIPES = register("aether_engine", MULTIBLOCK)
                .setMaxIOSize(0, 0, 1, 1)
                .setSlotOverlay(false, false, GuiTextures.BOX_OVERLAY)
                .setProgressBar(GuiTextures.PROGRESS_BAR_GAS_COLLECTOR, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
                .setSound(GTSoundEntries.TURBINE)
                .setEUIO(IO.OUT);

        MANA_BOILER_RECIPES = register("mana_boiler", MULTIBLOCK)
                .setMaxIOSize(1, 0, 1, 1)
                .setProgressBar(AstroGUITextures.PROGRESS_BAR_BOILER_FUEL_MANA, DOWN_TO_UP)
                .setMaxTooltips(1)
                .setSound(GTSoundEntries.FURNACE);
    }
}
