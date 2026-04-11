package com.astro.core.common.data.worldgen.feature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import com.astro.core.AstroCore;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;

import java.util.List;

import javax.annotation.Nullable;

public class StoneColumnsFeature extends Feature<StoneColumnsConfiguration> {

    private static final ImmutableList<Block> ALWAYS_CANNOT_PLACE_ON = ImmutableList.of(
            Blocks.LAVA, Blocks.BEDROCK, Blocks.MAGMA_BLOCK, Blocks.SOUL_SAND,
            Blocks.NETHER_BRICKS, Blocks.NETHER_BRICK_FENCE, Blocks.NETHER_BRICK_STAIRS,
            Blocks.NETHER_WART, Blocks.CHEST, Blocks.SPAWNER);

    public StoneColumnsFeature(Codec<StoneColumnsConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<StoneColumnsConfiguration> context) {
        int seaLevel = context.chunkGenerator().getSeaLevel();
        BlockPos origin = context.origin();
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        StoneColumnsConfiguration config = context.config();

        if (!canPlaceAt(level, seaLevel, origin.mutable(), config.canPlaceOn())) {
            AstroCore.LOGGER.info("place() failed initial canPlaceAt check at {}", origin);
            return false;
        }

        int height = config.height().sample(random);
        boolean clustered = random.nextFloat() < 0.9F;
        int clusterReach = Math.min(height, clustered ? 5 : 8);
        int clusterSize = clustered ? 50 : 15;
        boolean placed = false;

        for (BlockPos pos : BlockPos.randomBetweenClosed(random, clusterSize,
                origin.getX() - clusterReach, origin.getY(), origin.getZ() - clusterReach,
                origin.getX() + clusterReach, origin.getY(), origin.getZ() + clusterReach)) {
            int dist = height - pos.distManhattan(origin);
            if (dist >= 0) {
                placed |= placeColumn(level, seaLevel, pos, dist,
                        config.reach().sample(random), config.blockState(), config.canPlaceOn());
            }
        }
        return placed;
    }

    private boolean placeColumn(LevelAccessor level, int seaLevel, BlockPos pos,
                                int distance, int reach, BlockState columnBlock,
                                List<Block> canPlaceOn) {
        boolean placed = false;
        for (BlockPos bp : BlockPos.betweenClosed(
                pos.getX() - reach, pos.getY(), pos.getZ() - reach,
                pos.getX() + reach, pos.getY(), pos.getZ() + reach)) {
            int dist = bp.distManhattan(pos);
            BlockPos surface;
            if (isAirOrLava(level, seaLevel, bp)) {
                surface = findSurface(level, seaLevel, bp.mutable(), dist, canPlaceOn);
            } else {
                surface = findAir(level, bp.mutable(), dist);
            }
            if (surface != null) {
                int j = distance - dist / 2;
                for (BlockPos.MutableBlockPos mpos = surface.mutable(); j >= 0; --j) {
                    if (isAirOrLava(level, seaLevel, mpos)) {
                        this.setBlock(level, mpos, columnBlock);
                        mpos.move(Direction.UP);
                        placed = true;
                    } else {
                        if (!level.getBlockState(mpos).is(columnBlock.getBlock())) break;
                        mpos.move(Direction.UP);
                    }
                }
            }
        }
        return placed;
    }

    @Nullable
    private static BlockPos findSurface(LevelAccessor level, int seaLevel,
                                        BlockPos.MutableBlockPos pos, int distance,
                                        List<Block> canPlaceOn) {
        while (pos.getY() > level.getMinBuildHeight() + 1 && distance > 0) {
            --distance;
            if (canPlaceAt(level, seaLevel, pos, canPlaceOn)) return pos;
            pos.move(Direction.DOWN);
        }
        return null;
    }

    @Nullable
    private static BlockPos findAir(LevelAccessor level, BlockPos.MutableBlockPos pos, int distance) {
        while (pos.getY() < level.getMaxBuildHeight() && distance > 0) {
            --distance;
            BlockState state = level.getBlockState(pos);
            if (ALWAYS_CANNOT_PLACE_ON.contains(state.getBlock())) return null;
            if (state.isAir()) return pos;
            pos.move(Direction.UP);
        }
        return null;
    }

    private static boolean canPlaceAt(LevelAccessor level, int seaLevel,
                                      BlockPos.MutableBlockPos pos, List<Block> canPlaceOn) {
        if (!isAirOrLava(level, seaLevel, pos)) return false;
        BlockState below = level.getBlockState(pos.move(Direction.DOWN));
        pos.move(Direction.UP);
        if (below.isAir()) return false;
        if (ALWAYS_CANNOT_PLACE_ON.contains(below.getBlock())) return false;
        if (canPlaceOn.isEmpty()) return true;
        return canPlaceOn.contains(below.getBlock());
    }

    private static boolean isAirOrLava(LevelAccessor level, int seaLevel, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.isAir() || (state.is(Blocks.LAVA) && pos.getY() <= seaLevel);
    }
}
