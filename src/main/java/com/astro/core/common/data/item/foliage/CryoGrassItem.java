package com.astro.core.common.data.item.foliage;

import com.astro.core.common.data.AstroBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

@SuppressWarnings("all")
public class CryoGrassItem extends ItemNameBlockItem {

    public CryoGrassItem(Properties properties) {
        super(AstroBlocks.CRYO_GRASS.get(), properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockPos above = pos.above();
        BlockState clicked = level.getBlockState(pos);

        if (clicked.is(BlockTags.SAND) && level.getBlockState(above).isAir()) {
            if (!level.isClientSide) {
                level.setBlock(above, AstroBlocks.CRYO_GRASS.get().defaultBlockState(), 3);
                context.getItemInHand().shrink(1);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }
}