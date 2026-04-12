package com.astro.core.common.machine.part;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.common.machine.multiblock.part.AutoMaintenanceHatchPartMachine;

import net.minecraft.MethodsReturnNonnullByDefault;

import com.astro.core.common.machine.trait.OxygenatedProviderTrait;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class OxygenatedMaintenanceHatch extends AutoMaintenanceHatchPartMachine {

    public OxygenatedMaintenanceHatch(IMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public void addedToController(IMultiController controller) {
        super.addedToController(controller);
        var machine = controller.self();
        var existing = machine.getTraits().stream()
                .filter(OxygenatedProviderTrait.class::isInstance)
                .map(OxygenatedProviderTrait.class::cast)
                .findFirst();
        if (existing.isPresent()) {
            existing.get().setActive(true);
        } else {
            var trait = new OxygenatedProviderTrait(machine);
            trait.setActive(true);
            machine.attachTraits(trait);
        }
    }

    @Override
    public void removedFromController(IMultiController controller) {
        super.removedFromController(controller);
        controller.self().getTraits().stream()
                .filter(OxygenatedProviderTrait.class::isInstance)
                .map(OxygenatedProviderTrait.class::cast)
                .findFirst()
                .ifPresent(t -> t.setActive(false));
    }
}
