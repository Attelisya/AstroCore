package com.astro.core.common.commands;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.astro.core.api.capabilities.AstroCapabilities;
import com.astro.core.common.machine.singleblock.CWUGeneratorMachine;
import com.astro.core.common.machine.trait.cwu.ILocalCWUProvider;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

@SuppressWarnings("all")
@Mod.EventBusSubscriber
public class CWUTestCommand {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("cwutest")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                        .executes(CWUTestCommand::run)));
    }

    private static int run(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack source = ctx.getSource();
        BlockPos pos = BlockPosArgument.getBlockPos(ctx, "pos");

        BlockEntity be = source.getLevel().getBlockEntity(pos);

        if (be == null) {
            source.sendFailure(Component.literal(
                    "[CWUTest] FAIL: No block entity at " + pos));
            return 0;
        }
        source.sendSuccess(() -> Component.literal(
                "[CWUTest] Block entity found: " + be.getClass().getSimpleName()), false);

        if (!(be instanceof IMachineBlockEntity holder)) {
            source.sendFailure(Component.literal(
                    "[CWUTest] FAIL: Block entity is not an IMachineBlockEntity. " +
                            "Wrong block, or GT not loaded correctly."));
            return 0;
        }
        source.sendSuccess(() -> Component.literal(
                "[CWUTest] IMachineBlockEntity confirmed. MetaMachine: " +
                        holder.getMetaMachine().getClass().getSimpleName()),
                false);

        if (!(holder.getMetaMachine() instanceof CWUGeneratorMachine generator)) {
            source.sendFailure(Component.literal(
                    "[CWUTest] FAIL: MetaMachine is not a CWUGeneratorMachine. " +
                            "Check your registration in AstroSingleBlocks."));
            return 0;
        }
        source.sendSuccess(() -> Component.literal(
                "[CWUTest] CWUGeneratorMachine confirmed."), false);

        var cap = be.getCapability(AstroCapabilities.LOCAL_CWU_PROVIDER, null);
        if (!cap.isPresent()) {
            source.sendFailure(Component.literal(
                    "[CWUTest] FAIL: LOCAL_CWU_PROVIDER capability not found. " +
                            "Check that AstroCapabilities.register() is called on the MOD bus, " +
                            "and that AstroCapabilityEvents is being picked up by @Mod.EventBusSubscriber."));
            return 0;
        }
        source.sendSuccess(() -> Component.literal(
                "[CWUTest] LOCAL_CWU_PROVIDER capability present."), false);

        ILocalCWUProvider provider = cap.resolve().orElse(null);
        if (provider == null) {
            source.sendFailure(Component.literal(
                    "[CWUTest] FAIL: Capability present but resolved to null. " +
                            "Check the LazyOptional supplier in AstroCapabilityEvents."));
            return 0;
        }

        boolean active = provider.isProviderActive();
        int maxCwu = provider.getMaxCWUt();
        int budget = provider.requestCWUt(maxCwu, true); // simulate — don't actually consume

        source.sendSuccess(() -> Component.literal(
                "[CWUTest] Provider state — active: " + active +
                        ", maxCWUt: " + maxCwu +
                        ", simulated request(" + maxCwu + "): " + budget),
                false);

        if (!active) {
            source.sendSuccess(() -> Component.literal(
                    "[CWUTest] NOTE: Generator is inactive. Give it power and lubricant " +
                            "and wait one second for the first 20-tick cycle to complete."),
                    false);
        }

        source.sendSuccess(() -> Component.literal(
                "[CWUTest] All checks passed."), false);
        return 1;
    }
}
