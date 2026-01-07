package com.astro.core.common.data.materials;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlag;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;

public class AstroMaterialFlags {

    public static final MaterialFlag GENERATE_SLEEVE = new MaterialFlag.Builder("generate_sleeve")
            .requireProps(PropertyKey.INGOT).build();

    public static final TagPrefix sleeve = new TagPrefix("sleeve")
            .idPattern("%s_sleeve")
            .defaultTagPath("sleeve/%s")
            .unformattedTagPath("sleeve")
            .langValue("%s Sleeve")
            .materialAmount(GTValues.M * 2)
            .unificationEnabled(true)
            .generateItem(true)
            .enableRecycling()
            .materialIconType(AstroMaterialSet.SLEEVE)
            .generationCondition(mat -> mat.hasFlag(AstroMaterialFlags.GENERATE_SLEEVE));

    public static void init() {}
}
