package com.nhwhite3118.shulkerssupersimplestructuresystem;

//import com.nhwhite3118.cobbler.utils.ConfigHelper;
//import com.nhwhite3118.cobbler.utils.ConfigHelper.ConfigValueListener;
import com.nhwhite3118.structures.Structures;

import net.minecraft.world.gen.feature.Feature;
//import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = ShulkersSuperSimpleStructureSystem.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class ModEventSubscriber {

    @SubscribeEvent
    public static void onRegisterFeatures(final RegistryEvent.Register<Feature<?>> event) {
        Structures.registerStructures(event);
    }
}
