package io.github.ran.minecraft.ranitils.forge;

import io.github.ran.minecraft.ranitils.RanitilsMC;
import io.github.ran.minecraft.ranitils.config.ModConfig;
import io.github.ran.minecraft.ranitils.interfaces.Eventerface;
import io.github.ran.minecraft.ranitils.interfaces.Handler;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.ModLoadingContext;
#if PRE_MC_1_18_2
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.client.registry.ClientRegistry;
#else
import net.minecraftforge.client.ConfigScreenHandler
import net.minecraftforge.client.ClientRegistry;
#endif
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod.EventBusSubscriber(modid = RanitilsMC.MOD_ID)
@Mod(RanitilsMC.MOD_ID)
public class RanitilsMCForge implements Handler {
    public RanitilsMCForge() {
         PreLaunchSetup.onPreLaunch();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void clientSetup(FMLClientSetupEvent event) {
        RanitilsMC.init(this);
        #if PRE_MC_1_18_2
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> (mc, screen) -> AutoConfig.getConfigScreen(ModConfig.class, screen).get());
        #else
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory, () -> (mc, screen) -> AutoConfig.getConfigScreen(ModConfig.class, screen).get());
        #endif
    }

    @SubscribeEvent
    public void tickEvent(TickEvent.ClientTickEvent tickEvent) {
        if (tickEvent.phase == TickEvent.Phase.END) {
            events.forEach(event -> event.tick(Minecraft.getInstance()));
        }
    }

    @SubscribeEvent
    public void disconnectEvent(PlayerEvent.PlayerLoggedOutEvent playerLoggedOutEvent) {
        events.forEach(Eventerface::disconnectWorld);
    }

    @Override
    public void init() {
        keybinds.forEach(ClientRegistry::registerKeyBinding);
    }
}
