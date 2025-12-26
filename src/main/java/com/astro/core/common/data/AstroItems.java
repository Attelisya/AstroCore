package com.astro.core.common.data;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.ItemMaterialInfo;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialStack;
import com.gregtechceu.gtceu.api.item.ComponentItem;
import com.gregtechceu.gtceu.api.item.component.ElectricStats;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import net.minecraft.world.item.Item;

import com.tterrag.registrate.util.entry.ItemEntry;

import static com.astro.core.AstroCore.ASTRO_CREATIVE_TAB;
import static com.gregtechceu.gtceu.common.data.GTItems.materialInfo;
import static com.gregtechceu.gtceu.common.registry.GTRegistration.REGISTRATE;

@SuppressWarnings("all")
public class AstroItems {

    static {
        REGISTRATE.creativeModeTab(() -> ASTRO_CREATIVE_TAB);
    }

    public static ItemEntry<Item> FIREBOX_CASING;

    public static ItemEntry<Item> SHAPE_EXTRUDER_SLEEVE;
    public static ItemEntry<Item> SHAPE_MOLD_SLEEVE;

    public static void init() {
        FIREBOX_CASING = REGISTRATE
                .item("manasteel_firebox_casing", Item::new)
                .model((ctx, prov) -> {
                    prov.withExistingParent(ctx.getName(), prov.mcLoc("item/generated"))
                            .texture("layer0", prov.modLoc("item/" + ctx.getName()));
                })
                .register();

        SHAPE_EXTRUDER_SLEEVE = REGISTRATE
                .item("sleeve_extruder_mold", Item::new)
                .lang("Extruder Mold (Sleeve)")
                .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Steel, GTValues.M * 4))))
                .register();
        SHAPE_MOLD_SLEEVE = REGISTRATE
                .item("sleeve_casting_mold", Item::new)
                .lang("Casting Mold (Sleeve)")
                .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Steel, GTValues.M * 4))))
                .register();

    }
}
