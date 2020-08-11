package com.nhwhite3118.structures.simplestructure;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

import com.nhwhite3118.shulkerssupersimplestructuresystem.ShulkersSuperSimpleStructureSystem;
import com.nhwhite3118.structures.Structures;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class SimpleStructurePieces {

    public static void start(TemplateManager templateManager, BlockPos pos, Rotation rotation, List<StructurePiece> pieceList, Random random, Path[] paths,
            int[] yOffsets) {
        for (int i = 0; i < paths.length; i++) {
            pieceList.add(new SimpleStructurePieces.Piece(templateManager, paths[i], pos.add(0, yOffsets[i], 0), rotation));
        }

    }

    public static void start(TemplateManager templateManager, BlockPos pos, Rotation rotation, List<StructurePiece> pieceList, Random random,
            ResourceLocation[] resources, int[] yOffsets) {
        for (int i = 0; i < resources.length; i++) {
            pieceList.add(new SimpleStructurePieces.Piece(templateManager, resources[i], pos.add(0, yOffsets[i], 0), rotation));
        }

    }

    public static class Piece extends TemplateStructurePiece {
        private ResourceLocation resourceLocation;
        private Path path;
        private Rotation rotation;

        private void setupPiece(TemplateManager templateManager) {
            Template template;
            if (resourceLocation != null) {
                template = templateManager.getTemplate(this.resourceLocation);
                if (template == null) {
                    ShulkersSuperSimpleStructureSystem.LOGGER.warn("Unable to find file at: " + this.resourceLocation.getPath());
                }
            } else {
                boolean successfullRead = false;
                int readAttempt = 0;
                CompoundNBT compoundnbt = null;
                while (!successfullRead && readAttempt < 20) {
                    try {
                        successfullRead = true;
                        compoundnbt = CompressedStreamTools.readCompressed(new FileInputStream(path.toFile()));
                    } catch (IOException e) {
                        successfullRead = false;
                        readAttempt++;
                    }
                }

                try {
                    template = templateManager.func_227458_a_(compoundnbt);
                } catch (Exception e) {
                    ShulkersSuperSimpleStructureSystem.LOGGER.error("TemplateManager encountered an error: " + e.getMessage());
                    ShulkersSuperSimpleStructureSystem.LOGGER.error("Path: " + path.toString());
                    ShulkersSuperSimpleStructureSystem.LOGGER.error("CompdNBT: " + compoundnbt == null ? "null" : compoundnbt.isEmpty());
                    ShulkersSuperSimpleStructureSystem.LOGGER.error("read attempts: " + readAttempt);
                    return;
                }

                if (template == null) {
                    ShulkersSuperSimpleStructureSystem.LOGGER.warn("Unable to find file at: " + path.toString());
                }

            }
            PlacementSettings placementsettings = (new PlacementSettings()).setRotation(this.rotation).setMirror(Mirror.NONE);
            this.setup(template, this.templatePosition, placementsettings);
        }

        public Piece(TemplateManager templateManagerIn, CompoundNBT tagCompound) {
            super(Structures.FOR_REGISTERING_SIMPLE_STRUCTURES, tagCompound);
            this.resourceLocation = new ResourceLocation(tagCompound.getString("Template"));
            this.rotation = Rotation.valueOf(tagCompound.getString("Rot"));
            this.setupPiece(templateManagerIn);
        }

        public Piece(TemplateManager templateManagerIn, ResourceLocation resourceLocationIn, BlockPos pos, Rotation rotationIn) {
            super(Structures.FOR_REGISTERING_SIMPLE_STRUCTURES, 0);
            this.resourceLocation = resourceLocationIn;
            this.templatePosition = pos;
            this.rotation = rotationIn;
            this.setupPiece(templateManagerIn);
        }

        public Piece(TemplateManager templateManagerIn, Path path, BlockPos pos, Rotation rotationIn) {
            super(Structures.FOR_REGISTERING_SIMPLE_STRUCTURES, 0);
            this.path = path;
            this.templatePosition = pos;
            this.rotation = rotationIn;
            this.setupPiece(templateManagerIn);
        }

        /**
         * (abstract) Helper method to read subclass data from NBT
         */
        @Override
        protected void readAdditional(CompoundNBT tagCompound) {
            super.readAdditional(tagCompound);
            tagCompound.putString("Template", this.path.toString());
            tagCompound.putString("Rot", this.rotation.name());
        }

        @Override
        protected void handleDataMarker(String function, BlockPos pos, IWorld worldIn, Random rand, MutableBoundingBox sbb) {
        }
//
//        @Override
//        public boolean func_225577_a_(IWorld worldIn, ChunkGenerator<?> p_225577_2_, Random randomIn, MutableBoundingBox structureBoundingBoxIn,
//                ChunkPos chunkPos) {
//            PlacementSettings placementsettings = (new PlacementSettings()).setRotation(this.rotation).setMirror(Mirror.NONE);
//            this.templatePosition.add(Template.transformedBlockPos(placementsettings, new BlockPos(0, 0, 0)));
//
//            return super.func_225577_a_(worldIn, p_225577_2_, randomIn, structureBoundingBoxIn, chunkPos);
//        }
    }

}
