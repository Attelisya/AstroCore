package com.astro.core.common.data.tag;

import com.astro.core.AstroCore;
import com.astro.core.common.data.materials.AstroMaterials;
import com.drd.ad_extendra.common.registry.ModBlocks;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import static com.drd.ad_extendra.common.registry.ModBlocks.PLUTO_STONE;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.oreTagPrefix;

@SuppressWarnings("all")
public class AstroTagPrefix {

    //No point in moving this to java until we can figure out how to get secondary materials to drop when macerated
    public static final TagPrefix orePlutoStone = oreTagPrefix("pluto_stone", BlockTags.MINEABLE_WITH_PICKAXE)
            .langValue("Pluto Stone %s Ore")
            .registerOre(
                    () -> ModBlocks.PLUTO_STONE.get().defaultBlockState(), () -> AstroMaterials.PLUTO_STONE, BlockBehaviour.Properties.of()
                            .mapColor(MapColor.TERRACOTTA_ORANGE)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.STONE)
                            .strength(3.0F, 3.0F),
                    new ResourceLocation("ad_extendra", "block/pluto_stone"),
                    false, false, true);

    public static void init() {}

}
