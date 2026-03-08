package com.astro.core.client;

import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.registry.registrate.MachineBuilder;
import com.gregtechceu.gtceu.client.model.machine.overlays.WorkableOverlays;
import com.gregtechceu.gtceu.common.data.models.GTMachineModels;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;

import static com.gregtechceu.gtceu.api.machine.property.GTMachineModelProperties.RECIPE_LOGIC_STATUS;

public class AstroMachineModels {

    /**
     * Like {@link GTMachineModels#createWorkableCasingMachineModel}, but switches the base casing
     * texture to {@code activeCasingTexture} when the machine is in the working state.
     */
    public static MachineBuilder.ModelInitializer createActiveCasingMachineModel(
                                                                                 ResourceLocation idleCasingTexture,
                                                                                 ResourceLocation activeCasingTexture,
                                                                                 ResourceLocation overlayDir) {
        return (ctx, prov, builder) -> {
            WorkableOverlays overlays = WorkableOverlays.get(overlayDir, prov.getExistingFileHelper());

            builder.forAllStates(state -> {
                RecipeLogic.Status status = state.getValue(RECIPE_LOGIC_STATUS);
                boolean working = status == RecipeLogic.Status.WORKING;

                BlockModelBuilder model = prov.models().nested()
                        .parent(prov.models().getExistingFile(GTMachineModels.CUBE_ALL_SIDED_OVERLAY_MODEL))
                        .texture("all", working ? activeCasingTexture : idleCasingTexture);
                return GTMachineModels.addWorkableOverlays(overlays, status, model);
            });

            builder.addTextureOverride("all", idleCasingTexture);
        };
    }
}
