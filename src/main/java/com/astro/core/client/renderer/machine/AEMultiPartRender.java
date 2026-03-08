package com.astro.core.client.renderer.machine;

import com.gregtechceu.gtceu.api.block.property.GTBlockStateProperties;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.client.model.machine.IControllerModelRenderer;
import com.gregtechceu.gtceu.client.renderer.machine.DynamicRender;
import com.gregtechceu.gtceu.client.renderer.machine.DynamicRenderType;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("all")
public class AEMultiPartRender extends DynamicRender<MultiblockControllerMachine, AEMultiPartRender>
        implements IControllerModelRenderer {

    public static final Codec<AEMultiPartRender> CODEC =
            BlockState.CODEC.xmap(AEMultiPartRender::new, r -> r.idleState);
    public static final DynamicRenderType<MultiblockControllerMachine, AEMultiPartRender> TYPE =
            new DynamicRenderType<>(CODEC);

    private final BlockState idleState;
    private final BlockState activeState;

    private BakedModel idleModel, activeModel;

    public AEMultiPartRender(Supplier<? extends Block> casingBlock) {
        this(casingBlock.get().defaultBlockState());
    }

    public AEMultiPartRender(BlockState idleState) {
        this.idleState = idleState;
        this.activeState = idleState.setValue(GTBlockStateProperties.ACTIVE, true);
    }

    @Override
    public DynamicRenderType<MultiblockControllerMachine, AEMultiPartRender> getType() {
        return TYPE;
    }

    @Override
    public void render(MultiblockControllerMachine machine, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight, int packedOverlay) {}

    @Override
    public boolean shouldRender(MultiblockControllerMachine machine, Vec3 cameraPos) {
        return false;
    }

    @Override
    public boolean isBlockEntityRenderer() {
        return false;
    }

    @Override
    public void renderPartModel(List<BakedQuad> quads, IMultiController controller, IMultiPart part,
                                Direction frontFacing, @Nullable Direction side, RandomSource rand,
                                @NotNull ModelData modelData, @Nullable RenderType renderType) {

        boolean working = controller instanceof IRecipeLogicMachine rlm && rlm.getRecipeLogic().isWorking();
        BlockState state = working ? activeState : idleState;

        BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModel(state);

        modelData = model.getModelData(
                controller.self().getLevel(),
                controller.self().getPos(),
                state,
                ModelData.EMPTY
        );

        List<BakedQuad> newQuads = model.getQuads(state, side, rand, ModelData.EMPTY, renderType);

        if (controller.self().getLevel() != null) {
            newQuads = com.lowdragmc.lowdraglib.client.model.custommodel.CustomBakedModel
                    .reBakeCustomQuads(newQuads, controller.self().getLevel(), part.self().getPos(), state, side, 0.0f);
        }

        quads.addAll(newQuads);
    }
}