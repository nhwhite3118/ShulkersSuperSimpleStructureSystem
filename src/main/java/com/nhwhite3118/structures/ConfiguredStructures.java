package com.nhwhite3118.structures;

import java.util.List;
import java.util.stream.Collectors;

import com.nhwhite3118.shulkerssupersimplestructuresystem.ShulkersSuperSimpleStructureSystem;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class ConfiguredStructures {
    // Static instance of our structure so we can reference it and add it to biomes easily.

    public static List<?> CONFIGURED_SIMPLE_STRUCTURES = Structures.SIMPLE_STRUCTURES.stream().map(f -> f.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG)).collect(Collectors.toList());

    /*
     * Registers the configured structure which is what gets added to the biomes. Noticed we are not using a forge registry because there is none for configured
     * structures
     */
    public static void registerConfiguredStructures() {
        Registry<StructureFeature<?, ?>> registry = WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE;
        for (Object structureFeature : CONFIGURED_SIMPLE_STRUCTURES) {
            Registry.register(registry, new ResourceLocation(ShulkersSuperSimpleStructureSystem.MODID, "configured_shulker_factory"), (StructureFeature<?, ?>)structureFeature);

            // Ok so, this part may be hard to grasp but basically, just add your structure to this to
            // prevent any sort of crash or issue with other mod's custom ChunkGenerators. If they use
            // FlatGenerationSettings.STRUCTURES in it and you don't add your structure to it, the game
            // could crash later when you attempt to add the StructureSeparationSettings to the dimension.

            // (It would also crash with superflat worldtype if you omit the below line
            // and attempt to add the structure's StructureSeparationSettings to the world)
            
            FlatGenerationSettings.STRUCTURES.put(((StructureFeature<?, ?>)structureFeature).field_236268_b_, (StructureFeature<?, ?>)structureFeature);
        }
    }

}
