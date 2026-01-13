package com.astro.core.datagen;

import com.gregtechceu.gtceu.api.registry.registrate.SoundEntryBuilder;

import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.astro.core.AstroCore;
import com.astro.core.datagen.lang.AstroLangHandler;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateLangProvider;

import static com.astro.core.common.registry.AstroRegistry.REGISTRATE;

@SuppressWarnings("all")
public class AstroDatagen {

    public static void init(RegistrateLangProvider provider) {}

    protected static void multiLang(RegistrateLangProvider provider, String key, String... values) {
        for (var i = 0; i < values.length; i++) {
            provider.add(getSubKey(key, i), values[i]);
        }
    }

    protected static String getSubKey(String key, int index) {
        return key + "." + index;
    }

    public static void init() {
        REGISTRATE.addDataGenerator(ProviderType.LANG, AstroLangHandler::init);
    }

    @Mod.EventBusSubscriber(modid = AstroCore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public class ExampleDataGenerators {

        @SubscribeEvent
        public static void gatherData(GatherDataEvent event) {
            PackOutput packOutput = event.getGenerator().getPackOutput();

            if (event.includeClient()) {
                event.getGenerator().addProvider(
                        true,
                        new SoundEntryBuilder.SoundEntryProvider(packOutput, AstroCore.MOD_ID));
            }
        }
    }
}
