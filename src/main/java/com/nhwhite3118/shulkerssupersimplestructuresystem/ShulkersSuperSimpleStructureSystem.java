package com.nhwhite3118.shulkerssupersimplestructuresystem;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nhwhite3118.shulkerssupersimplestructuresystem.ShulkersStructuresConfig.ShulkersStructuresConfigValues;
import com.nhwhite3118.structures.Structures;
import com.nhwhite3118.structures.simplestructure.SimpleStructure;
import com.nhwhite3118.utils.ConfigHelper;
import com.nhwhite3118.utils.StructureDeserializer;

import net.minecraft.client.Minecraft;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("shulkerssupersimplestructuresystem")
public class ShulkersSuperSimpleStructureSystem {
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "shulkerssupersimplestructuresystem";
    public static ShulkersStructuresConfigValues Config = null;
    public static final ENVIRONMENTS ENVIRONMENT = ENVIRONMENTS.PRODUCTION;

    public enum ENVIRONMENTS {
        DEBUG, PRODUCTION
    };

    public ShulkersSuperSimpleStructureSystem() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        Config = ConfigHelper.register(ModConfig.Type.COMMON, ShulkersStructuresConfig.ShulkersStructuresConfigValues::new);
    }

    public void setup(final FMLCommonSetupEvent event) {
        ShulkersSuperSimpleStructureSystem.addFeaturesAndStructuresToBiomes();
    }

    private static void addFeaturesAndStructuresToBiomes() {
        if (Structures.SIMPLE_STRUCTURES.isEmpty()) {
            ShulkersSuperSimpleStructureSystem.LOGGER.warn("Structure map is empty. This should only happen if user hasn't added any structures");
            try {
                StructureDeserializer.readAllFiles(Structures.SIMPLE_STRUCTURES, Minecraft.getInstance().getResourceManager(), "structurejsons");
            } catch (Exception e) {
                ShulkersSuperSimpleStructureSystem.LOGGER.error(e.getMessage());
            }
        }
        if (Structures.SIMPLE_STRUCTURES.isEmpty()) {
            return;
        }
        for (Biome biome : ForgeRegistries.BIOMES) {
            String biomeNamespace = biome.getRegistryName().getNamespace();
            String biomePath = biome.getRegistryName().getPath();
            for (SimpleStructure structure : Structures.SIMPLE_STRUCTURES) {
                if (Arrays.asList(structure.VALID_BIOMES).contains(biome) || Arrays.asList(structure.VALID_BIOME_CATEGORIES).contains(biome.getCategory())) {
                    biome.func_235063_a_(structure.func_236391_a_(IFeatureConfig.NO_FEATURE_CONFIG));
                }
            }
        }
    }
}
