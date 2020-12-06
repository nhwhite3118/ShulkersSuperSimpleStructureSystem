package com.nhwhite3118.shulkerssupersimplestructuresystem;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nhwhite3118.shulkerssupersimplestructuresystem.ShulkersStructuresConfig.ShulkersStructuresConfigValues;
import com.nhwhite3118.structures.ConfiguredStructures;
import com.nhwhite3118.structures.Structures;
import com.nhwhite3118.structures.simplestructure.SimpleStructure;
import com.nhwhite3118.utils.ConfigHelper;
import com.nhwhite3118.utils.StructureDeserializer;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

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
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addGenericListener(Structure.class, this::onRegisterStructures);

        // For events that happen after initialization. This is probably going to be use a lot.
        MinecraftForge.EVENT_BUS.register(this);
        Config = ConfigHelper.register(ModConfig.Type.COMMON, ShulkersStructuresConfig.ShulkersStructuresConfigValues::new);
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addListener(EventPriority.NORMAL, this::addDimensionalSpacing);

        // The comments for BiomeLoadingEvent and StructureSpawnListGatherEvent says to do HIGH for additions.
        forgeBus.addListener(EventPriority.HIGH, this::biomeModification);
    }

    /**
     * This method will be called by Forge when it is time for the mod to register features.
     */
    public void onRegisterStructures(final RegistryEvent.Register<Structure<?>> event) {
        // Registers the structures.
        // If you don't do this, bad things might happen... very bad things... Spooky...
        LOGGER.log(Level.INFO, "onRegisterStructures start");
        try {
            StructureDeserializer.readAllFiles(Structures.SIMPLE_STRUCTURES, Minecraft.getInstance().getResourceManager(), "structurejsons");
        } catch (Exception e) {
            ShulkersSuperSimpleStructureSystem.LOGGER.error(e.getMessage());
        }
        Structures.registerStructures(event);
        ConfiguredStructures.registerConfiguredStructures();
        LOGGER.log(Level.INFO, "onRegisterStructures end");

        // LOGGER.log(Level.INFO, "structures registered.");
    }

    /**
     * This is the event you will use to add anything to any biome. This includes spawns, changing the biome's looks, messing with its surfacebuilders, adding
     * carvers, spawning new features... etc
     *
     * Here, we will use this to add our structure to all biomes.
     */
    public void biomeModification(final BiomeLoadingEvent event) {
        // Add our structure to biomes including other modded biomes
        if (Structures.SIMPLE_STRUCTURES.isEmpty()) {
            ShulkersSuperSimpleStructureSystem.LOGGER.warn("Structure map is empty. This should only happen if user hasn't added any structures");
        }
        if (Structures.SIMPLE_STRUCTURES.isEmpty()) {
            return;
        }
        for (Biome biome : ForgeRegistries.BIOMES) {
            String biomeNamespace = biome.getRegistryName().getNamespace();
            String biomePath = biome.getRegistryName().getPath();
            for (SimpleStructure structure : Structures.SIMPLE_STRUCTURES) {
                if (Arrays.asList(structure.VALID_BIOMES).contains(biome) || Arrays.asList(structure.VALID_BIOME_CATEGORIES).contains(biome.getCategory())) {
                    event.getGeneration().getStructures().add(() -> structure.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
                }
            }
        }
    }

    /**
     * Will go into the world's chunkgenerator and manually add our structure spacing. If the spacing is not added, the structure doesn't spawn.
     *
     * Use this for dimension blacklists for your structure. (Don't forget to attempt to remove your structure too from the map if you are blacklisting that
     * dimension! It might have your structure in it already.)
     *
     * Basically use this to mak absolutely sure the chunkgeneration can or cannot spawn your structure.
     */
    public void addDimensionalSpacing(final WorldEvent.Load event) {
        LOGGER.log(Level.INFO, "addDimensionalSpacing start");
        if (event.getWorld() instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) event.getWorld();

            // Prevent spawning our structure in Vanilla's superflat world as
            // people seem to want their superflat worlds free of modded structures.
            // Also that vanilla superflat is really tricky and buggy to work with in my experience.
            if (serverWorld.getChunkProvider().getChunkGenerator() instanceof FlatChunkGenerator && serverWorld.getDimensionKey().equals(World.OVERWORLD)) {
                return;
            }

            Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(serverWorld.getChunkProvider().generator.func_235957_b_().func_236195_a_());
            for (SimpleStructure structure : Structures.SIMPLE_STRUCTURES) {
                tempMap.put(structure, DimensionStructuresSettings.field_236191_b_.get(structure));
            }
            serverWorld.getChunkProvider().generator.func_235957_b_().field_236193_d_ = tempMap;
        }
        LOGGER.log(Level.INFO, "addDimensionalSpacing end");
    }

    /*
     * Helper method to quickly register features, blocks, items, structures, biomes, anything that can be registered.
     */
    public static <T extends IForgeRegistryEntry<T>> T register(IForgeRegistry<T> registry, T entry, String registryKey) {
        LOGGER.log(Level.INFO, "register start");
        entry.setRegistryName(new ResourceLocation(ShulkersSuperSimpleStructureSystem.MODID, registryKey));
        registry.register(entry);
        LOGGER.log(Level.INFO, "register end");
        return entry;
    }
}
