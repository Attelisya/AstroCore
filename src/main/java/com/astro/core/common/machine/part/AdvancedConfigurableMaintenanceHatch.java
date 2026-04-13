package com.astro.core.common.machine.part;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.gregtechceu.gtceu.api.item.tool.ToolHelper;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.feature.IInteractedMachine;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMaintenanceMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredPartMachine;
import com.gregtechceu.gtceu.api.machine.property.GTMachineModelProperties;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.client.model.machine.MachineRenderState;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.DoubleSupplier;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AdvancedConfigurableMaintenanceHatch extends TieredPartMachine
                                                  implements IMachineLife, IMaintenanceMachine, IInteractedMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            AdvancedConfigurableMaintenanceHatch.class, MultiblockPartMachine.MANAGED_FIELD_HOLDER);

    private static final float MAX_DURATION_MULTIPLIER = 1.25f;
    private static final float MIN_DURATION_MULTIPLIER = 0.75f;
    private static final float DURATION_ACTION_AMOUNT = 0.01f;

    @Persisted
    private final NotifiableItemStackHandler itemStackHandler;
    @Getter
    @Persisted
    @DescSynced
    private boolean isTaped;
    @Getter
    @Setter
    @Persisted
    protected int timeActive;
    @Getter
    @Persisted
    @DescSynced
    protected byte maintenanceProblems = startProblems();
    @Getter
    @Persisted
    private float durationMultiplier = 1f;
    @Nullable
    protected TickableSubscription maintenanceSubs;

    public AdvancedConfigurableMaintenanceHatch(IMachineBlockEntity holder) {
        super(holder, GTValues.UV);
        this.itemStackHandler = new NotifiableItemStackHandler(this, 1, IO.BOTH, IO.BOTH);
        this.itemStackHandler.setFilter(itemStack -> itemStack.is(GTItems.DUCT_TAPE.get()));
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public void onMachineRemoved() {
        clearInventory(itemStackHandler);
    }

    @Override
    public byte startProblems() {
        return ALL_PROBLEMS;
    }

    @Override
    public boolean isFullAuto() {
        return false;
    }

    //////////////////////////////////////
    // ********* Logic **********//
    //////////////////////////////////////

    @Override
    public void setMaintenanceProblems(byte problems) {
        this.maintenanceProblems = problems;
        updateMaintenanceSubscription();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!isRemote()) {
            updateMaintenanceSubscription();
            MachineRenderState renderState = getRenderState();
            if (renderState.hasProperty(GTMachineModelProperties.IS_TAPED) &&
                    this.isTaped != renderState.getValue(GTMachineModelProperties.IS_TAPED)) {
                setRenderState(renderState.setValue(GTMachineModelProperties.IS_TAPED, this.isTaped));
            }
        }
    }

    protected void updateMaintenanceSubscription() {
        if (hasMaintenanceProblems()) {
            maintenanceSubs = subscribeServerTick(maintenanceSubs, this::update);
        } else if (maintenanceSubs != null) {
            maintenanceSubs.unsubscribe();
            maintenanceSubs = null;
        }
    }

    public void update() {
        if (getOffsetTimer() % 20 == 0) {
            if (hasMaintenanceProblems()) {
                if (consumeDuctTape(this.itemStackHandler, 0)) {
                    fixAllMaintenanceProblems();
                    setTaped(true);
                }
            } else {
                updateMaintenanceSubscription();
            }
        }
    }

    @Override
    public void setTaped(boolean isTaped) {
        if (this.isTaped != isTaped) {
            this.isTaped = isTaped;
            setRenderState(getRenderState().setValue(GTMachineModelProperties.IS_TAPED, isTaped));
        }
    }

    public void fixAllMaintenanceProblems() {
        for (int i = 0; i < 6; i++) setMaintenanceFixed(i);
    }

    private void fixMaintenanceProblems(@Nullable Player entityPlayer) {
        if (!hasMaintenanceProblems()) return;

        if (entityPlayer != null) {
            if (entityPlayer.isCreative()) {
                fixAllMaintenanceProblems();
                return;
            }
            for (int i = 0; i < entityPlayer.getInventory().items.size(); i++) {
                if (consumeDuctTape(new InvWrapper(entityPlayer.getInventory()), i)) {
                    fixAllMaintenanceProblems();
                    setTaped(true);
                    return;
                }
            }
            fixProblemsWithTools(getMaintenanceProblems(), entityPlayer);
        }
    }

    private boolean consumeDuctTape(IItemHandler handler, int slot) {
        var stored = handler.getStackInSlot(slot);
        if (!stored.isEmpty() && stored.is(GTItems.DUCT_TAPE.get())) {
            return handler.extractItem(slot, 1, false).is(GTItems.DUCT_TAPE.get());
        }
        return false;
    }

    private boolean consumeDuctTape(Player player, InteractionHand hand) {
        var held = player.getItemInHand(hand);
        if (!held.isEmpty() && held.is(GTItems.DUCT_TAPE.get())) {
            if (!player.isCreative()) held.shrink(1);
            return true;
        }
        return false;
    }

    private void fixProblemsWithTools(byte problems, Player entityPlayer) {
        List<GTToolType> toolsToMatch = Arrays.asList(new GTToolType[6]);
        boolean proceed = false;
        for (byte index = 0; index < 6; index++) {
            if (((problems >> index) & 1) == 0) {
                proceed = true;
                switch (index) {
                    case 0 -> toolsToMatch.set(0, GTToolType.WRENCH);
                    case 1 -> toolsToMatch.set(1, GTToolType.SCREWDRIVER);
                    case 2 -> toolsToMatch.set(2, GTToolType.SOFT_MALLET);
                    case 3 -> toolsToMatch.set(3, GTToolType.HARD_HAMMER);
                    case 4 -> toolsToMatch.set(4, GTToolType.WIRE_CUTTER);
                    case 5 -> toolsToMatch.set(5, GTToolType.CROWBAR);
                }
            }
        }
        if (!proceed) return;

        for (int i = 0; i < toolsToMatch.size(); i++) {
            GTToolType toolToMatch = toolsToMatch.get(i);
            if (toolToMatch != null) {
                ItemStack heldItem = entityPlayer.containerMenu.getCarried();
                if (ToolHelper.is(heldItem, toolToMatch)) {
                    fixProblemWithTool(i, heldItem, entityPlayer);
                    if (toolsToMatch.stream().allMatch(Objects::isNull)) return;
                }
                for (ItemStack itemStack : entityPlayer.getInventory().items) {
                    if (ToolHelper.is(itemStack, toolToMatch)) {
                        fixProblemWithTool(i, itemStack, entityPlayer);
                        if (toolsToMatch.stream().allMatch(Objects::isNull)) return;
                    }
                }
                if (entityPlayer instanceof ServerPlayer player) {
                    for (ItemStack stack : entityPlayer.getInventory().items) {
                        if (ToolHelper.is(stack, toolToMatch)) {
                            setMaintenanceFixed(i);
                            ToolHelper.damageItem(stack, player, 1);
                            if (toolsToMatch.stream().allMatch(Objects::isNull)) return;
                        }
                    }
                }
            }
        }
    }

    private void fixProblemWithTool(int problemIndex, ItemStack stack, Player player) {
        setMaintenanceFixed(problemIndex);
        if (player instanceof ServerPlayer serverPlayer) {
            ToolHelper.damageItem(stack, serverPlayer, 1);
        }
        setTaped(false);
    }

    //////////////////////////////////////
    // ***** Duration / Time ******//
    //////////////////////////////////////

    @Override
    public float getTimeMultiplier() {
        float result;
        if (durationMultiplier < 1.0f) {
            result = -8f * durationMultiplier + 9f;
        } else {
            result = -3.2f * durationMultiplier + 4.2f;
        }
        return BigDecimal.valueOf(result)
                .setScale(2, RoundingMode.HALF_UP)
                .floatValue();
    }

    //////////////////////////////////////
    // ******* Interaction *******//
    //////////////////////////////////////

    @Override
    public InteractionResult onUse(BlockState state, Level world, BlockPos pos, Player player,
                                   InteractionHand hand, BlockHitResult hit) {
        if (hasMaintenanceProblems()) {
            if (consumeDuctTape(player, hand)) {
                fixAllMaintenanceProblems();
                setTaped(true);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    //////////////////////////////////////
    // ********* GUI ************//
    //////////////////////////////////////

    @Override
    public Widget createUIWidget() {
        var group = new WidgetGroup(0, 0, 150, 70);
        group.addWidget(new DraggableScrollableWidgetGroup(4, 4, 150 - 8, 70 - 8)
                .setBackground(GuiTextures.DISPLAY)
                .addWidget(new ComponentPanelWidget(4, 5, list -> {
                    list.add(getTextWidgetText("duration", this::getDurationMultiplier));
                    list.add(getTextWidgetText("time", this::getTimeMultiplier));
                    var buttonText = Component.translatable("gtceu.maintenance.configurable_duration.modify");
                    buttonText.append(" ");
                    buttonText.append(ComponentPanelWidget.withButton(Component.literal("[-]"), "sub"));
                    buttonText.append(" ");
                    buttonText.append(ComponentPanelWidget.withButton(Component.literal("[+]"), "add"));
                    list.add(buttonText);
                }).setMaxWidthLimit(150 - 8 - 8 - 4).clickHandler((componentData, clickData) -> {
                    if (!clickData.isRemote) {
                        if (componentData.equals("sub")) {
                            durationMultiplier = Mth.clamp(
                                    durationMultiplier - DURATION_ACTION_AMOUNT,
                                    MIN_DURATION_MULTIPLIER,
                                    MAX_DURATION_MULTIPLIER);
                        } else if (componentData.equals("add")) {
                            durationMultiplier = Mth.clamp(
                                    durationMultiplier + DURATION_ACTION_AMOUNT,
                                    MIN_DURATION_MULTIPLIER,
                                    MAX_DURATION_MULTIPLIER);
                        }
                    }
                })));
        group.addWidget(new SlotWidget(itemStackHandler, 0, group.getSize().width - 4 - 18, 4)
                .setBackgroundTexture(new GuiTextureGroup(GuiTextures.SLOT, GuiTextures.DUCT_TAPE_OVERLAY))
                .setHoverTooltips("gtceu.machine.maintenance_hatch_tape_slot.tooltip"));
        group.addWidget(new ButtonWidget(group.getSize().width - 4 - 18, 4 + 20, 18, 18,
                GuiTextures.MAINTENANCE_BUTTON,
                data -> fixMaintenanceProblems(group.getGui().entityPlayer))
                .setHoverTooltips("gtceu.machine.maintenance_hatch_tool_slot.tooltip"));
        group.setBackground(GuiTextures.BACKGROUND_INVERSE);
        return group;
    }

    private static Component getTextWidgetText(String type, DoubleSupplier multiplier) {
        Component tooltip;
        if (multiplier.getAsDouble() == 1.0) {
            tooltip = Component.translatable(
                    "gtceu.maintenance.configurable_" + type + ".unchanged_description");
        } else {
            tooltip = Component.translatable(
                    "gtceu.maintenance.configurable_" + type + ".changed_description",
                    FormattingUtil.formatNumber2Places(multiplier.getAsDouble()));
        }
        return Component
                .translatable("gtceu.maintenance.configurable_" + type,
                        FormattingUtil.formatNumber2Places(multiplier.getAsDouble()))
                .setStyle(Style.EMPTY.withHoverEvent(
                        new HoverEvent(HoverEvent.Action.SHOW_TEXT, tooltip)));
    }
}
