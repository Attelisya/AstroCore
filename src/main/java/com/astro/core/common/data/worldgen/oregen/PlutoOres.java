package com.astro.core.common.data.worldgen.oregen;

import com.gregtechceu.gtceu.api.data.worldgen.GTLayerPattern;
import com.gregtechceu.gtceu.api.data.worldgen.GTOreDefinition;
import com.gregtechceu.gtceu.api.data.worldgen.IWorldGenLayer;
import com.gregtechceu.gtceu.api.data.worldgen.generator.indicators.SurfaceIndicatorGenerator;
import com.gregtechceu.gtceu.api.data.worldgen.generator.veins.DikeVeinGenerator.DikeBlockDefinition;
import com.gregtechceu.gtceu.api.data.worldgen.generator.veins.NoopVeinGenerator;
import com.gregtechceu.gtceu.api.data.worldgen.generator.veins.VeinedVeinGenerator.VeinBlockDefinition;
import com.gregtechceu.gtceu.api.registry.GTRegistries;

import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

import com.astro.core.common.data.worldgen.AstroWorldGenLayers;

import java.util.ArrayList;
import java.util.Set;
import java.util.function.Consumer;

import static com.astro.core.common.data.materials.AstroMaterials.*;
import static com.gregtechceu.gtceu.api.data.worldgen.BiomeWeightModifier.EMPTY;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;

@SuppressWarnings("all")
public class PlutoOres {

    public static void init() {
        create("chromium_manganese_vein_pluto", vein -> vein
                .clusterSize(UniformInt.of(38, 50)).density(0.6f).weight(30)
                .layer(AstroWorldGenLayers.PLUTO_STONE)
                .heightRangeUniform(-30, 30)
                .layeredVeinGenerator(generator -> generator
                        .withLayerPattern(() -> GTLayerPattern
                                .builder(new RuleTest[] { AstroWorldGenLayers.PLUTO_STONE.getTarget() })
                                .layer(l -> l.weight(3).mat(Grossular).size(2, 4))
                                .layer(l -> l.weight(3).mat(Pyrolusite).size(1, 2))
                                .layer(l -> l.weight(2).mat(Chromite).size(1, 2))
                                .layer(l -> l.weight(1).mat(Tantalite).size(1, 1))
                                .build()))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(Pyrolusite)
                        .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));

        create("topaz_vein_pluto", vein -> vein
                .clusterSize(UniformInt.of(28, 36)).density(0.35f).weight(25)
                .layer(AstroWorldGenLayers.PLUTO_STONE)
                .heightRangeUniform(0, 50)
                .layeredVeinGenerator(generator -> generator
                        .withLayerPattern(() -> GTLayerPattern
                                .builder(new RuleTest[] { AstroWorldGenLayers.PLUTO_STONE.getTarget() })
                                .layer(l -> l.weight(3).mat(Topaz).size(2, 4))
                                .layer(l -> l.weight(2).mat(BlueTopaz).size(1, 2))
                                .layer(l -> l.weight(1).mat(Apatite).size(1, 1))
                                .build()))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(BlueTopaz)
                        .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));

        create("carnotite_vein_pluto", vein -> vein
                .clusterSize(UniformInt.of(24, 32)).density(0.2f).weight(10)
                .layer(AstroWorldGenLayers.PLUTO_STONE)
                .heightRangeUniform(-60, -20)
                .dikeVeinGenerator(generator -> generator
                        .withBlock(new DikeBlockDefinition(CARNOTITE, 3, -60, -20))
                        .withBlock(new DikeBlockDefinition(Uraninite, 2, -55, -25))
                        .withBlock(new DikeBlockDefinition(Pitchblende, 1, -60, -30)))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(CARNOTITE)
                        .density(0.1f)
                        .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)
                        .radius(2)));

        create("desh_ostrum_vein_pluto", vein -> vein
                .clusterSize(UniformInt.of(40, 56)).density(0.8f).weight(40)
                .layer(AstroWorldGenLayers.PLUTO_STONE)
                .heightRangeUniform(-20, 40)
                .veinedVeinGenerator(generator -> generator
                        .oreBlock(new VeinBlockDefinition(DESH, 4))
                        .oreBlock(new VeinBlockDefinition(OSTRUM, 2))
                        .oreBlock(new VeinBlockDefinition(Magnetite, 2))
                        .rareBlock(new VeinBlockDefinition(Iron, 1))
                        .rareBlockChance(0.15f)
                        .veininessThreshold(0.01f)
                        .maxRichnessThreshold(0.175f)
                        .minRichness(0.7f)
                        .maxRichness(1.0f)
                        .edgeRoundoffBegin(3)
                        .maxEdgeRoundoff(0.1f))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(DESH)
                        .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));

        create("bauxite_ilmenite_vein_pluto", vein -> vein
                .clusterSize(UniformInt.of(34, 44)).density(0.4f).weight(35)
                .layer(AstroWorldGenLayers.PLUTO_STONE)
                .heightRangeUniform(-10, 50)
                .layeredVeinGenerator(generator -> generator
                        .withLayerPattern(() -> GTLayerPattern
                                .builder(new RuleTest[] { AstroWorldGenLayers.PLUTO_STONE.getTarget() })
                                .layer(l -> l.weight(3).mat(Bauxite).size(2, 4))
                                .layer(l -> l.weight(2).mat(Ilmenite).size(1, 2))
                                .build()))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(Bauxite)
                        .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));
    }

    private static GTOreDefinition blank() {
        return new GTOreDefinition(
                ConstantInt.ZERO, 0, 0, IWorldGenLayer.NOWHERE, Set.of(),
                HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(0)),
                0, HolderSet::direct, EMPTY, NoopVeinGenerator.INSTANCE,
                new ArrayList<>());
    }

    private static void create(String name, Consumer<GTOreDefinition> config) {
        GTOreDefinition def = blank();
        config.accept(def);
        ResourceLocation id = new ResourceLocation("astrogreg", name);
        def.register(id);
        GTRegistries.ORE_VEINS.registerOrOverride(id, def);
    }
}
