package com.astro.core.common.data.materials;

import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconType;

import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet.*;

public class AstroMaterialSet {

    public static final MaterialIconType SLEEVE = new MaterialIconType("sleeve");
    public static final MaterialIconType COMPRESSED_SPRING = new MaterialIconType("compressed_spring");

    public static final MaterialIconSet DULL_MAGNETIC = new MaterialIconSet("dull_magnetic", MAGNETIC);
    public static final MaterialIconSet METALLIC_SUPER = new MaterialIconSet("superconductor_metallic", METALLIC);
    public static final MaterialIconSet RADIOACTIVE_SUPER = new MaterialIconSet("superconductor_radioactive",
            RADIOACTIVE);
    public static final MaterialIconSet BRIGHT_SUPER = new MaterialIconSet("superconductor_bright", BRIGHT);
    public static final MaterialIconSet SHINY_SUPER = new MaterialIconSet("superconductor_shiny", SHINY);

    public static void init() {}
}
