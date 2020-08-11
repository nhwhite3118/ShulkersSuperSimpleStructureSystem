package com.nhwhite3118.shulkerssupersimplestructuresystem;

import com.nhwhite3118.structures.Structures;
import com.nhwhite3118.utils.StructureDeserializer;

import net.minecraft.client.Minecraft;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = ShulkersSuperSimpleStructureSystem.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class ModEventSubscriber {

    @SubscribeEvent
    public static void onRegisterFeatures(final RegistryEvent.Register<Feature<?>> event) {
        try {
            if (Structures.SIMPLE_STRUCTURES.isEmpty()) {
                ShulkersSuperSimpleStructureSystem.LOGGER.warn("Structure map is empty in register features. Begining parsing");
                StructureDeserializer.readAllFiles(Structures.SIMPLE_STRUCTURES, Minecraft.getInstance().getResourceManager(), "structurejsons");
            }
            Structures.registerStructures(event);
        } catch (Exception e) {
            ShulkersSuperSimpleStructureSystem.LOGGER.error(e.getMessage());
        }
    }
}
