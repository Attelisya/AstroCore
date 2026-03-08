package com.astro.core.api.capabilities;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.astro.core.AstroCore;
import com.astro.core.common.machine.singleblock.CWUGeneratorMachine;
import com.astro.core.common.machine.trait.cwu.ILocalCWUProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(modid = AstroCore.MOD_ID)
public class AstroCapabilityEvents {

    private static final ResourceLocation LOCAL_CWU_KEY = AstroCore.id("local_cwu_provider");

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<BlockEntity> event) {
        BlockEntity be = event.getObject();

        if (!(be instanceof IMachineBlockEntity holder)) return;
        if (!(holder.getMetaMachine() instanceof CWUGeneratorMachine generator)) return;

        event.addCapability(LOCAL_CWU_KEY, new ICapabilityProvider() {

            private final LazyOptional<ILocalCWUProvider> opt = LazyOptional.of(() -> generator);

            @NotNull
            @Override
            public <T> LazyOptional<T> getCapability(
                                                     @NotNull Capability<T> cap, @Nullable Direction side) {
                return cap == AstroCapabilities.LOCAL_CWU_PROVIDER ?
                        AstroCapabilities.LOCAL_CWU_PROVIDER.orEmpty(cap, opt) : LazyOptional.empty();
            }
        });
    }
}
