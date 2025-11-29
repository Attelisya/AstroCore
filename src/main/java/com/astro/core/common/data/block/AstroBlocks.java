package com.astro.core.common.data.block;

import com.gregtechceu.gtceu.api.block.ActiveBlock;
import com.gregtechceu.gtceu.common.block.BoilerFireboxType;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.models.GTModels;
import com.gregtechceu.gtceu.data.recipe.CustomTags;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import com.astro.core.AstroCore;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;

import static com.astro.core.common.registry.AstroRegistry.REGISTRATE;

@SuppressWarnings("unused")
public class AstroBlocks {

    public static void init() {}

    static {
        REGISTRATE.creativeModeTab(() -> AstroCore.ASTRO_CREATIVE_TAB);
    }

    private static BlockEntry<Block> createSidedCasingBlock(String name, String id, String texture,
                                                            NonNullBiFunction<Block, Item.Properties, ? extends BlockItem> func) {
        return REGISTRATE
                .block(id, Block::new)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
                .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(),
                        prov.models().cubeAll(ctx.getName(), AstroCore.id("block/" + texture))))
                .tag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
                .lang(name)
                .item(func)
                .build()
                .register();
    }

    public static final BlockEntry<Block> ALFSTEEL_MACHINE_CASING = createSidedCasingBlock(
            "§dAlfsteel§r Engine Casing", "solid_alfsteel_machine_casing",
            "generators/machine_casing_turbine_alfsteel", BlockItem::new);

    public static final BlockEntry<Block> ALFSTEEL_PIPE_CASING = createSidedCasingBlock(
            "§dAlfsteel§r Pipe Casing", "alfsteel_pipe_casing",
            "generators/machine_casing_pipe_alfsteel", BlockItem::new);

    public static final BlockEntry<Block> ALFSTEEL_GEARBOX_CASING = createSidedCasingBlock(
            "§dAlfsteel§r Gearbox", "alfsteel_gearbox_casing",
            "generators/machine_casing_gearbox_alfsteel", BlockItem::new);

    public static final BlockEntry<Block> TERRASTEEL_MACHINE_CASING = createSidedCasingBlock(
            "§2Terrasteel§r Machine Casing", "solid_terrasteel_machine_casing",
            "generators/terrasteel_casing", BlockItem::new);

    public static final BlockEntry<Block> TERRASTEEL_PIPE_CASING = createSidedCasingBlock(
            "§2Terrasteel§r Pipe Casing", "terrasteel_pipe_casing",
            "generators/machine_casing_pipe_terrasteel", BlockItem::new);

    public static final BlockEntry<Block> MANASTEEL_MACHINE_CASING = createSidedCasingBlock(
            "§bManasteel§r-Plated Brick Casing", "manasteel_brick_machine_casing",
            "generators/machine_casing_manasteel_plated_bricks", BlockItem::new);

    public static final BlockEntry<Block> MANASTEEL_PIPE_CASING = createSidedCasingBlock(
            "§bManasteel§r Pipe Casing", "manasteel_pipe_casing",
            "generators/machine_casing_pipe_manasteel", BlockItem::new);

    public static final BoilerFireboxType MANASTEEL_FIREBOX = new BoilerFireboxType(
            "manasteel_firebox",
            AstroCore.id("block/generators/machine_casing_manasteel_plated_bricks"),
            AstroCore.id("block/generators/machine_casing_manasteel_plated_bricks"),
            AstroCore.id("block/generators/machine_casing_firebox_manasteel"));

    public static final BoilerFireboxType TERRASTEEL_FIREBOX = new BoilerFireboxType(
            "terrasteel_firebox",
            AstroCore.id("block/generators/machine_casing_solid_terrasteel"),
            AstroCore.id("block/generators/machine_casing_solid_terrasteel"),
            AstroCore.id("block/generators/machine_casing_firebox_terrasteel"));

    public static final BoilerFireboxType ALFSTEEL_FIREBOX = new BoilerFireboxType(
            "alfsteel_firebox",
            AstroCore.id("block/generators/machine_casing_turbine_alfsteel"),
            AstroCore.id("block/generators/machine_casing_turbine_alfsteel"),
            AstroCore.id("block/generators/machine_casing_firebox_alfsteel"));

    private static BlockEntry<ActiveBlock> createFireboxCasing(@SuppressWarnings("SameParameterValue") BoilerFireboxType type) {
        var block = REGISTRATE
                .block("%s_casing".formatted(type.name()), ActiveBlock::new)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
                .addLayer(() -> RenderType::cutoutMipped)
                .blockstate(GTModels.createFireboxModel(type))
                .tag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
                .item(BlockItem::new)
                .build()
                .register();
        GTBlocks.ALL_FIREBOXES.put(type, block);
        return block;
    }

    public static final BlockEntry<ActiveBlock> FIREBOX_MANASTEEL = createFireboxCasing(MANASTEEL_FIREBOX);
    public static final BlockEntry<ActiveBlock> FIREBOX_TERRASTEEL = createFireboxCasing(TERRASTEEL_FIREBOX);
    public static final BlockEntry<ActiveBlock> FIREBOX_ALFSTEEL = createFireboxCasing(ALFSTEEL_FIREBOX);
}
