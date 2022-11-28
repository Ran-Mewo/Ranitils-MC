package io.github.ran.minecraft.ranitils.forge;

import io.github.ran.minecraft.ranitils.RanitilsMC;
import io.github.ran.minecraft.ranitils.interfaces.Eventerface;
import io.github.ran.minecraft.ranitils.interfaces.Handler;
import net.minecraft.client.Minecraft;
#if PRE_MC_1_18_2
import net.minecraftforge.fml.client.registry.ClientRegistry;
#else
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
