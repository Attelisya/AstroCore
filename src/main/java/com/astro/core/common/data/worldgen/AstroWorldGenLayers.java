package com.astro.core.common.data.worldgen;

import com.gregtechceu.gtceu.api.data.worldgen.IWorldGenLayer;
import com.gregtechceu.gtceu.api.data.worldgen.WorldGeneratorUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

import java.util.Set;

@SuppressWarnings("all")
public class AstroWorldGenLayers {

    public static final IWorldGenLayer PLUTO_STONE = new IWorldGenLayer() {

        @Override
        public boolean isApplicableForLevel(ResourceLocation level) {
            return getLevels().contains(level);
        }

        @Override
        public Set<ResourceLocation> getLevels() {
            return Set.of(new ResourceLocation("ad_extendra:pluto"));
        }

        @Override
        public RuleTest getTarget() {
            return new BlockMatchTest(
                    BuiltInRegistries.BLOCK.get(new ResourceLocation("ad_extendra:pluto_stone")));
        }

        @Override
        public String getSerializedName() {
            return "astrogreg:pluto_stone";
        }
    };

    public static void init() {
        WorldGeneratorUtils.WORLD_GEN_LAYERS.put("astrogreg:pluto_stone", PLUTO_STONE);
    }
}
