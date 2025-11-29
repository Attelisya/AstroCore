package com.astro.core.common.data.configs;

import com.astro.core.AstroCore;
import dev.toma.configuration.Configuration;
import dev.toma.configuration.config.Config;
import dev.toma.configuration.config.ConfigHolder;
import dev.toma.configuration.config.Configurable;
import dev.toma.configuration.config.format.ConfigFormats;

@Config(id = AstroCore.MOD_ID)
public class AstroConfigs {

    public static AstroConfigs INSTANCE;

    public static ConfigHolder<AstroConfigs> CONFIG_HOLDER;

    public static void init() {
        CONFIG_HOLDER = Configuration.registerConfig(AstroConfigs.class, ConfigFormats.yaml());
        INSTANCE = CONFIG_HOLDER.getConfigInstance();
    }

    @Configurable
    public FeatureConfigs features = new FeatureConfigs();

    public static class FeatureConfigs {

        @Configurable
        @Configurable.Comment({ "The max temperature of the Large Manasteel Mana Boiler" })
        public int manasteelBoilerMaxTemperature = 2000;
        @Configurable
        @Configurable.Comment({ "The heat speed of the Large Manasteel Mana Boiler" })
        public int manasteelBoilerHeatSpeed = 1;
        @Configurable
        @Configurable.Comment({ "The max temperature of the Large Terrasteel Mana Boiler" })
        public int terrasteelBoilerMaxTemperature = 3000;
        @Configurable
        @Configurable.Comment({ "The heat speed of the Large Terrasteel Mana Boiler" })
        public int terrasteelBoilerHeatSpeed = 2;
        @Configurable
        @Configurable.Comment({ "The max temperature of the Large Alfsteel Mana Boiler" })
        public int alfsteelBoilerMaxTemperature = 4000;
        @Configurable
        @Configurable.Comment({ "The heat speed of the Large Alfsteel Mana Boiler" })
        public int alfsteelBoilerHeatSpeed = 3;
    }
}
