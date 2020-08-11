package com.nhwhite3118.structures.simplestructure;

import java.nio.file.Path;

import org.apache.logging.log4j.Level;

import com.mojang.serialization.Codec;
import com.nhwhite3118.shulkerssupersimplestructuresystem.ShulkersSuperSimpleStructureSystem;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class SimpleStructure extends Structure<NoFeatureConfig> {

    private int SpawnRate;
    ResourceLocation resources[];
    Path paths[];
    private int yOffsets[];
    private int Seed;
    public String StructureName;
    public Biome[] VALID_BIOMES;
    public Category[] VALID_BIOME_CATEGORIES;
    public Decoration DECORATOR;

    public SimpleStructure(Codec<NoFeatureConfig> codec, int spawnRate, ResourceLocation resource, int yOffset, int seed, String structureName,
            Biome[] validBiomes, Category[] validCategories, Decoration decorator) {
        super(codec);
        SpawnRate = spawnRate;
        resources = new ResourceLocation[] { resource };
        yOffsets = new int[] { yOffset };
        Seed = seed;
        StructureName = structureName;
        VALID_BIOMES = validBiomes;
        VALID_BIOME_CATEGORIES = validCategories;
        DECORATOR = decorator;
    }

    public SimpleStructure(Codec<NoFeatureConfig> codec, int spawnRate, Path path, int yOffset, int seed, String structureName, Biome[] validBiomes,
            Category[] validCategories, Decoration decorator) {
        super(codec);
        SpawnRate = spawnRate;
        paths = new Path[] { path };
        yOffsets = new int[] { yOffset };
        Seed = seed;
        StructureName = structureName;
        VALID_BIOMES = validBiomes;
        VALID_BIOME_CATEGORIES = validCategories;
        DECORATOR = decorator;
    }

    public int getSpawnRate() {
        return SpawnRate;
    }

    /*
     * The structure name to show in the /locate command.
     * 
     * Make sure this matches what the resourcelocation of your structure will be because if you don't add the MODID: part, Minecraft will put minecraft: in
     * front of the name instead and we don't want that. We want our structure to have our mod's ID rather than Minecraft so people don't get confused.
     */
    @Override
    public String getStructureName() {
        return ShulkersSuperSimpleStructureSystem.MODID + ":" + this.StructureName;
    }

    /*
     * This is how the worldgen code will start the generation of our structure when it passes the checks.
     */
    @Override
    public Structure.IStartFactory getStartFactory() {
        return SimpleStructure.Start::new;
    }

    /*
     * This is used so that if two structure's has the same spawn location algorithm, they will not end up in perfect sync as long as they have different seed
     * modifier.
     * 
     * So make this a big random number that is unique only to this structure.
     */
    protected int getSeedModifier() {
        return Seed;
    }

    public int getSeed() {
        return Seed;
    }

    /*
     * Handles calling up the structure's pieces class and height that structure will spawn at.
     */
    public static class Start extends StructureStart {
        public Start(Structure<?> structureIn, int chunkX, int chunkZ, MutableBoundingBox mutableBoundingBox, int referenceIn, long seedIn) {
            super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
        }

        @Override
        public void func_230364_a_(ChunkGenerator generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, IFeatureConfig config) {
            // Check out vanilla's WoodlandMansionStructure for how they offset the x and z
            // so that they get the y value of the land at the mansion's entrance, no matter
            // which direction the mansion is rotated.
            //
            // However, for most purposes, getting the y value of land with the default x
            // and z is good enough.
            Rotation rotation = Rotation.values()[this.rand.nextInt(Rotation.values().length)];

            // Turns the chunk coordinates into actual coordinates we can use. (Gets center
            // of that chunk)
            int x = (chunkX << 4) + 7;
            int z = (chunkZ << 4) + 7;

            // Finds the y value of the terrain at location.
            int surfaceY = generator.func_222531_c(x, z, Heightmap.Type.WORLD_SURFACE_WG);
            if (surfaceY < 30) {
                return;
            }
            BlockPos blockpos = new BlockPos(x, surfaceY, z);

            // Now adds the structure pieces to this.components with all details such as
            // where each part goes
            // so that the structure can be added to the world by worldgen.
            SimpleStructure containingStructure = (SimpleStructure) this.getStructure();
//            SimpleStructurePieces.start(templateManagerIn, blockpos, rotation, this.components, this.rand, containingStructure.resources,
//                    containingStructure.yOffsets);
            SimpleStructurePieces.start(templateManagerIn, blockpos, rotation, this.components, this.rand, containingStructure.paths,
                    containingStructure.yOffsets);

            // Sets the bounds of the structure.
            this.recalculateStructureSize();

            // I use to debug and quickly find out if the structure is spawning or not and
            // where it is.
            ShulkersSuperSimpleStructureSystem.LOGGER.log(Level.DEBUG,
                    containingStructure.StructureName + " at " + (blockpos.getX()) + " " + blockpos.getY() + " " + (blockpos.getZ()));
        }

    }

}
