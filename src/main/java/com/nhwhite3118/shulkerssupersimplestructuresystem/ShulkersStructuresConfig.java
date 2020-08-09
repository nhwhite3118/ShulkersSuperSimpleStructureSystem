package com.nhwhite3118.shulkerssupersimplestructuresystem;

import com.nhwhite3118.utils.ConfigHelper;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ShulkersStructuresConfig {
    public static class ShulkersStructuresConfigValues {

        ShulkersStructuresConfigValues(ForgeConfigSpec.Builder builder, ConfigHelper.Subscriber subscriber) {
            builder.push("Feature Options");

            builder.push("Structures");

//            towerCanSpawn = subscriber.subscribe(builder.comment("\r\n Whether or not to spawn towers in hills and mountains" + "\r\n Default value is true")
//                    .translation("shulkersstructures.config.feature.structures.towerCanSpawn").define("towerCanSpawn", true));
//
//            towerSpawnrate = subscriber.subscribe(builder
//                    .comment("\r\n How many chunks apart towers will attempt to spawn."
//                            + "\r\n The config value is the max distance in chunks between spawn attempts, but not all spawn attempts will succeed."
//                            + "\r\n 10 to practically always have one in render distance, 10000 for extremely rare towers")
//                    .translation("nhwhite3118.config.structure.endStructures.towerSpawnrate").defineInRange("towerSpawnrate", 20, 10, 10000));
            builder.pop();

            builder.pop();
        }
    }
}