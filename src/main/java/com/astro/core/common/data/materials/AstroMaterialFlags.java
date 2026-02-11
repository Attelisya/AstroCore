package com.astro.core.common.data.materials;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlag;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;

public class AstroMaterialFlags {

    public static final MaterialFlag GENERATE_SLEEVE = new MaterialFlag.Builder("generate_sleeve")
            .requireProps(PropertyKey.INGOT).build();
    public static final MaterialFlag GENERATE_COMPRESSED_SPRING = new MaterialFlag.Builder("generate_compressed_spring")
            .requireFlags(MaterialFlags.GENERATE_SPRING).build();

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

    public static final TagPrefix compressed_spring = new TagPrefix("compressed_spring")
            .idPattern("compressed_%s_spring")
            .defaultTagPath("compressed_spring/%s")
            .unformattedTagPath("compressed_spring")
            .langValue("Compressed %s Spring")
            .materialAmount(GTValues.M)
            .unificationEnabled(true)
            .generateItem(true)
            .enableRecycling()
            .materialIconType(AstroMaterialSet.COMPRESSED_SPRING)
            .generationCondition(mat -> mat.hasFlag(AstroMaterialFlags.GENERATE_COMPRESSED_SPRING));

    public static void init() {}
}
