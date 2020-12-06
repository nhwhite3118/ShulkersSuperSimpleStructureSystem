package com.nhwhite3118.structures;

import java.util.ArrayList;
import java.util.Locale;

import com.google.common.collect.ImmutableMap;
import com.nhwhite3118.shulkerssupersimplestructuresystem.ShulkersSuperSimpleStructureSystem;
import com.nhwhite3118.structures.simplestructure.SimpleStructure;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.event.RegistryEvent.Register;

public class Structures {
    public static IStructurePieceType SIMPLE_STRUCTURE_PIECE_TYPE = com.nhwhite3118.structures.simplestructure.SimpleStructurePieces.Piece::new;
    public static ArrayList<SimpleStructure> SIMPLE_STRUCTURES = new ArrayList<SimpleStructure>();

    /*
     * This is called when the forge event on the mod event bus for registering features is called. Adds structures to all the registries and maps which they
     * need to be in for them to work properly.
     */
    public static void registerStructures(Register<Structure<?>> event) {
        ShulkersSuperSimpleStructureSystem.LOGGER.warn("Registering " + SIMPLE_STRUCTURES.size() + " structures");

        for (SimpleStructure structure : SIMPLE_STRUCTURES) {
            ShulkersSuperSimpleStructureSystem.LOGGER.warn("Registering " + structure );
            ShulkersSuperSimpleStructureSystem.LOGGER.warn("With name: " + structure.StructureName );
            ShulkersSuperSimpleStructureSystem.LOGGER.warn("With seed: " + structure.getSeed() );
            ShulkersSuperSimpleStructureSystem.LOGGER.warn("With spawn rate: " + structure.getSpawnRate() );
            ShulkersSuperSimpleStructureSystem.register(event.getRegistry(), structure, structure.StructureName.toLowerCase(Locale.ROOT));
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
        /*
         * We need to add our structures into the map in Structure alongside vanilla structures or else it will cause errors. Called by registerStructure.
         *
         * (If you are using deferred registries, do not put this line inside the deferred method. Instead, call it anywhere else before you start the
         * configuredstructure registering)
         */
        Structure.NAME_STRUCTURE_BIMAP.put(structure.getRegistryName().toString(), structure);

        /*
         * Adds the structure's spacing into several places so that the structure's spacing remains correct in any dimension or worldtype instead of not
         * spawning.
         *
         * However, it seems it doesn't always work for code made dimensions as they read from this list beforehand. Use the WorldEvent.Load event in
         * StructureTutorialMain to add the structure spacing from this list into that dimension.
         */
        DimensionStructuresSettings.field_236191_b_ = ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
                .putAll(DimensionStructuresSettings.field_236191_b_).put(structure, structureSeparationSettings).build();
    }

    public static void registerPieces() {
        register(SIMPLE_STRUCTURE_PIECE_TYPE, "simplestructure");
        ShulkersSuperSimpleStructureSystem.LOGGER.warn("Adding structure pieces to the register ~~~~~~~~~~~~~~~~~~~~~~");
    }

    /*
     * Registers the structures pieces themselves. If you don't do this part, Forge will complain to you in the Console.
     */
    private static IStructurePieceType register(IStructurePieceType structurePiece, String key) {
        return Registry.register(Registry.STRUCTURE_PIECE, key.toLowerCase(Locale.ROOT), structurePiece);
    }
}
