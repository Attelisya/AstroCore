package com.astro.core.common.data.worldgen.feature;

import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraftforge.registries.ForgeRegistries;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record StoneColumnsConfiguration(
                                        Block block,
                                        IntProvider reach,
                                        IntProvider height,
                                        List<Block> canPlaceOn)
        implements FeatureConfiguration {

    public static final Codec<StoneColumnsConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ForgeRegistries.BLOCKS.getCodec()
                    .fieldOf("block")
                    .forGetter(StoneColumnsConfiguration::block),
            IntProvider.CODEC
                    .fieldOf("reach")
                    .forGetter(StoneColumnsConfiguration::reach),
            IntProvider.CODEC
                    .fieldOf("height")
                    .forGetter(StoneColumnsConfiguration::height),
            ForgeRegistries.BLOCKS.getCodec()
                    .listOf()
                    .fieldOf("can_place_on")
                    .forGetter(StoneColumnsConfiguration::canPlaceOn))
            .apply(instance, StoneColumnsConfiguration::new));

    public BlockState blockState() {
        return block.defaultBlockState();
    }
}
