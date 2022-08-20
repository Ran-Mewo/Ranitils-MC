package io.github.ran.minecraft.ranitils.fabric;

import io.github.ran.minecraft.ranitils.RanitilsMC;
import io.github.ran.minecraft.ranitils.fabric.features.waypoints.Waypoint;
import io.github.ran.minecraft.ranitils.interfaces.Handler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class RanitilsMCFabric implements ClientModInitializer, Handler {
    @Override
    public void onInitializeClient() {
        Waypoint.register();
        keybinds.add(Waypoint.sendKey);
        keybinds.add(Waypoint.removeKey);
        events.add(new Waypoint(null, null));

        RanitilsMC.init(this);
    }

    @Override
    public void init() {
        keybinds.forEach(KeyBindingHelper::registerKeyBinding);
        events.forEach(event -> {
            ClientTickEvents.END_CLIENT_TICK.register(event::tick);
            ClientPlayConnectionEvents.DISCONNECT.register((listener, minecraft) -> event.disconnectWorld());
        });
    }
}
