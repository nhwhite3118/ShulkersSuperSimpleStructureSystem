package com.nhwhite3118.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.NotImplementedException;

import com.google.common.io.CharStreams;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.nhwhite3118.shulkerssupersimplestructuresystem.ShulkersSuperSimpleStructureSystem;
import com.nhwhite3118.structures.simplestructure.SimpleStructure;

import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraftforge.fml.loading.FMLPaths;

public class StructureDeserializer {

    private StructureDeserializer() {
    }

    public static void readAllFiles(List<SimpleStructure> output, IResourceManager manager, String configFilesFolder) {
        try {
            List<String> allFilenames = listFolderContents(configFilesFolder.toLowerCase());
            for (String filename : allFilenames) {
                ShulkersSuperSimpleStructureSystem.LOGGER
                        .warn("parsing file: " + ShulkersSuperSimpleStructureSystem.MODID + "/" + configFilesFolder.toLowerCase() + "/" + filename);
                File file = FMLPaths.GAMEDIR.get().resolve(ShulkersSuperSimpleStructureSystem.MODID + "/" + configFilesFolder + "/" + filename).toFile();
                InputStream inputStream = new FileInputStream(file);
                String inputString = CharStreams.toString(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                SimpleStructure structure = deserialiseStructure(new StringReader(inputString));
                if (structure != null) {
                    output.add(structure);
                }
            }
        } catch (IOException e) { // didn't find the file in this resourcedomain
            ResourceLocation resourceLocation = new ResourceLocation(ShulkersSuperSimpleStructureSystem.MODID, configFilesFolder);
            ShulkersSuperSimpleStructureSystem.LOGGER
                    .warn("A problem occurred trying to read the list of config files from folder " + resourceLocation + ":\n" + e.getMessage());
        }
    }

    /**
     * list all the files in a particular resource location Looks in assets/dragonmounts/{pathToFolder} and returns a list of all the filenames it finds
     **/
    public static List<String> listFolderContents(String pathToFolder) throws IOException {
        ShulkersSuperSimpleStructureSystem.LOGGER.warn("About to read info for folder: " + ShulkersSuperSimpleStructureSystem.MODID + "/" + pathToFolder);
        File folder = FMLPaths.GAMEDIR.get().resolve(ShulkersSuperSimpleStructureSystem.MODID + "/" + pathToFolder).toFile();
        String[] resultArray = folder.list(null);
        return Arrays.asList(resultArray);
    }

    /**
     * Deserialise the JSON file for a structure
     */
    private static SimpleStructure deserialiseStructure(Reader input) throws JsonSyntaxException {
        JsonParser parser = new JsonParser();
        JsonElement entireFile = parser.parse(input);
        if (!entireFile.isJsonObject())
            throw new JsonSyntaxException("Malformed file");
        return deserializeTags(entireFile.getAsJsonObject());
    }

    private static SimpleStructure deserializeTags(JsonObject jsonObject) {
        if (!jsonObject.isJsonObject()) {
            ShulkersSuperSimpleStructureSystem.LOGGER.error("Malformed json files found. Try parsing your structure jsons through a json syntax checker");
            return null;
        }
        JsonElement structureNameJSON = jsonObject.get("structureName");
        if (structureNameJSON == null) {
            ShulkersSuperSimpleStructureSystem.LOGGER.error("Didn't find the required field structureName in one of your structure json files");
            return null;
        }
        if (!structureNameJSON.isJsonPrimitive() || !structureNameJSON.getAsJsonPrimitive().isString()) {
            ShulkersSuperSimpleStructureSystem.LOGGER.error("The required field structureName has the wrong type (should be a String).");
            return null;
        }
        String structureName = structureNameJSON.getAsString();
        ArrayList<StructurePiece> structurePieces = new ArrayList<StructurePiece>();
        int seed = 0;
        int spawnRate = 0;
        Path nbtLocation = null;
        int yOffset = 0;
        List<Biome> biomes = new ArrayList<Biome>();

        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String elementName = entry.getKey();
            switch (elementName) {
            case "piece":
                ShulkersSuperSimpleStructureSystem.LOGGER.warn("Parsed piece ???????");
                StructurePiece piece = deserializePiece(entry.getValue().getAsJsonObject());
                structurePieces.add(piece);
                break;
            case "seed":
                if (!isNumber(entry)) {
                    ShulkersSuperSimpleStructureSystem.LOGGER.error("The required field seed has the wrong type (should be a whole number).");
                    break;
                }
                seed = entry.getValue().getAsInt();
                ShulkersSuperSimpleStructureSystem.LOGGER.warn("Parsed seed " + seed);
                break;
            case "spawnRate":
                if (!isNumber(entry)) {
                    ShulkersSuperSimpleStructureSystem.LOGGER.error("The required field spawnRate has the wrong type (should be a whole number).");
                    break;
                }
                spawnRate = entry.getValue().getAsInt();
                ShulkersSuperSimpleStructureSystem.LOGGER.warn("Parsed spawnRate " + spawnRate);
                break;
            case "fileName":
                if (!isString(entry)) {
                    ShulkersSuperSimpleStructureSystem.LOGGER.error("The required field fileName has the wrong type (should be a String).");
                    break;
                }
                nbtLocation = FMLPaths.GAMEDIR.get().resolve(ShulkersSuperSimpleStructureSystem.MODID + "/structureNBTs/" + entry.getValue().getAsString());
                ShulkersSuperSimpleStructureSystem.LOGGER.warn("Parsed nbtLocation " + nbtLocation.toString());
                break;
            case "blockLevelsBelowGround":
                if (!isNumber(entry)) {
                    ShulkersSuperSimpleStructureSystem.LOGGER.error("The field blockLevelsBelowGround has the wrong type (should be a whole number).");
                    break;
                }
                yOffset = -entry.getValue().getAsInt();
                ShulkersSuperSimpleStructureSystem.LOGGER.warn("Parsed yOffset " + yOffset);
                break;
            case "biomes":
                ShulkersSuperSimpleStructureSystem.LOGGER.warn("About to parse biomes");
                biomes = deserializeBiomes(entry.getValue().getAsJsonArray());
                ShulkersSuperSimpleStructureSystem.LOGGER.warn("Parsed " + biomes.size() + " biomes");
            default:
            }
        }
        SimpleStructure result = new SimpleStructure(NoFeatureConfig.field_236558_a_, spawnRate, nbtLocation, yOffset, seed, structureName,
                biomes.toArray(new Biome[0]), new Category[] {}, Decoration.SURFACE_STRUCTURES);
        return result;
    }

    private static boolean isNumber(Map.Entry<String, JsonElement> element) {
        return element.getValue().isJsonPrimitive() && element.getValue().getAsJsonPrimitive().isNumber();
    }

    private static boolean isString(Map.Entry<String, JsonElement> element) {
        return element.getValue().isJsonPrimitive() && element.getValue().getAsJsonPrimitive().isString();
    }

    private static StructurePiece deserializePiece(JsonObject jsonObject) {
        throw new NotImplementedException("Multiple piece structures not implemented yet");
    }

    private static List<Biome> deserializeBiomes(JsonArray biomeArray) {
        List<Biome> biomes = new ArrayList<Biome>();
        for (JsonElement entry : biomeArray) {
            String biomeName = entry.getAsString();
            ResourceLocation biomeResourceLocation = new ResourceLocation(biomeName);
            if (Registry.BIOME.containsKey(biomeResourceLocation)) {
                Optional<Biome> biome = Registry.BIOME.getValue(biomeResourceLocation);
                if (biome.isPresent()) {
                    biomes.add(biome.get());
                }
            }
        }
        return biomes;
    }
}
