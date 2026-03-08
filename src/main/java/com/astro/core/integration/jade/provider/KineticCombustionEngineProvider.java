package com.astro.core.integration.jade.provider;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import com.astro.core.AstroCore;
import com.astro.core.common.machine.multiblock.kinetic.KineticCombustionEngineMachine;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class KineticCombustionEngineProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    public static final ResourceLocation UID = AstroCore.id("kinetic_combustion_engine_info");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (!(accessor.getBlockEntity() instanceof MetaMachineBlockEntity metaMachineBE &&
                metaMachineBE.getMetaMachine() instanceof KineticCombustionEngineMachine)) {
            return;
        }

        CompoundTag data = accessor.getServerData();
        if (!data.contains("formed") || !data.getBoolean("formed")) return;

        int suOutput = data.getInt("suOutput");
        tooltip.add(Component.translatable("astrogreg.machine.kinetic_steam_engine.su_output", suOutput)
                .withStyle(ChatFormatting.AQUA));
    }

    @Override
    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        if (!(accessor.getBlockEntity() instanceof MetaMachineBlockEntity metaMachineBE &&
                metaMachineBE.getMetaMachine() instanceof KineticCombustionEngineMachine machine)) {
            return;
        }

        data.putBoolean("formed", machine.isFormed());
        if (machine.isFormed()) {
            data.putInt("suOutput",
                    machine.getRecipeLogic().isActive() ? (int) machine.getCurrentOutputSU() : 0);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}
