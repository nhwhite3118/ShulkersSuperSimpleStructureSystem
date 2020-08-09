package com.nhwhite3118.structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.nhwhite3118.shulkerssupersimplestructuresystem.ShulkersSuperSimpleStructureSystem;
import com.nhwhite3118.structures.simplestructure.SimpleStructure;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.event.RegistryEvent.Register;

public class Structures {
    public static IStructurePieceType FOR_REGISTERING_SIMPLE_STRUCTURES = com.nhwhite3118.structures.simplestructure.SimpleStructurePieces.Piece::new;
    public static ArrayList<SimpleStructure> SIMPLE_STRUCTURES = new ArrayList<SimpleStructure>();

    /*
     * This is called when the forge event on the mod event bus for registering features is called. Adds structures to all the registries and maps which they
     * need to be in for them to work properly.
     */
    public static void registerStructures(Register<Feature<?>> event) {
        ShulkersSuperSimpleStructureSystem.LOGGER.warn("Registering " + SIMPLE_STRUCTURES.size() + " structures");

        for (SimpleStructure structure : SIMPLE_STRUCTURES) {
            registerStructure(new ResourceLocation(ShulkersSuperSimpleStructureSystem.MODID, structure.StructureName), structure,
                    GenerationStage.Decoration.SURFACE_STRUCTURES,
                    new StructureSeparationSettings(structure.getSpawnRate(), (int) (structure.getSpawnRate() * 0.75), structure.getSeed()));
        }

        Structures.registerPieces();
    }

    /*
     * Adds the provided structure to the registry, and adds the separation settings. This is how the rarity of the structure is set.
     */
    public static <F extends Structure<NoFeatureConfig>> void registerStructure(ResourceLocation resourceLocation, F structure,
            GenerationStage.Decoration stage, StructureSeparationSettings structureSeparationSettings) {
        structure.setRegistryName(resourceLocation);
        addToStructureInfoMaps(resourceLocation.toString(), structure, stage);
        FlatGenerationSettings.STRUCTURES.put(structure, structure.func_236391_a_(IFeatureConfig.NO_FEATURE_CONFIG));

//        DimensionStructuresSettings.field_236191_b_ = ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
//                .putAll(DimensionStructuresSettings.field_236191_b_).put(structure, structureSeparationSettings).build();

        Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(DimensionStructuresSettings.field_236191_b_);
        tempMap.put(structure, structureSeparationSettings);
        DimensionStructuresSettings.field_236191_b_ = ImmutableMap.copyOf(tempMap);

        DimensionSettings.Preset.field_236122_b_.func_236137_b_().func_236108_a_().func_236195_a_().put(structure, structureSeparationSettings);
        DimensionSettings.Preset.field_236123_c_.func_236137_b_().func_236108_a_().func_236195_a_().put(structure, structureSeparationSettings);
        DimensionSettings.Preset.field_236124_d_.func_236137_b_().func_236108_a_().func_236195_a_().put(structure, structureSeparationSettings);
        DimensionSettings.Preset.field_236125_e_.func_236137_b_().func_236108_a_().func_236195_a_().put(structure, structureSeparationSettings);
        DimensionSettings.Preset.field_236126_f_.func_236137_b_().func_236108_a_().func_236195_a_().put(structure, structureSeparationSettings);
        DimensionSettings.Preset.field_236127_g_.func_236137_b_().func_236108_a_().func_236195_a_().put(structure, structureSeparationSettings);
//        DimensionSettings.Preset.field_236128_h_.forEach(
//                (presetResourceLocation, preset) -> preset.func_236137_b_().func_236108_a_().func_236195_a_().put(structure, structureSeparationSettings));
    }

    /*
     * The structure class keeps maps of all the structures and their generation stages. We need to add our structures into the maps along with the vanilla
     * structures or else it will cause errors
     */
    private static <F extends Structure<?>> F addToStructureInfoMaps(String name, F structure, GenerationStage.Decoration generationStage) {
        ShulkersSuperSimpleStructureSystem.LOGGER
                .warn("Adding structure to the structurefeature registry with key " + name.toLowerCase(Locale.ROOT) + "~~~~~~~~~~~~~~~~~~~~~~");
        Structure.field_236365_a_.put(name.toLowerCase(Locale.ROOT), structure);
        Structure.field_236385_u_.put(structure, generationStage);
        return Registry.register(Registry.STRUCTURE_FEATURE, name.toLowerCase(Locale.ROOT), structure);
    }

    public static void registerPieces() {
        register(FOR_REGISTERING_SIMPLE_STRUCTURES, "simplestructure");
        ShulkersSuperSimpleStructureSystem.LOGGER.warn("Adding structure pieces to the register ~~~~~~~~~~~~~~~~~~~~~~");
    }

    /*
     * Registers the structures pieces themselves. If you don't do this part, Forge will complain to you in the Console.
     */
    private static IStructurePieceType register(IStructurePieceType structurePiece, String key) {
        return Registry.register(Registry.STRUCTURE_PIECE, key.toLowerCase(Locale.ROOT), structurePiece);
    }
}
