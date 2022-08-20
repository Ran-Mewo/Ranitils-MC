package io.github.ran.minecraft.ranitils;

import io.github.ran.minecraft.ranitils.config.ModConfig;
import io.github.ran.minecraft.ranitils.features.verticalTP.VerticalTP;
import io.github.ran.minecraft.ranitils.interfaces.Handler;

public class RanitilsMC {
    public static final String MOD_ID = "ranitils";
    
    public static void init(Handler handler) {
        ModConfig.init();

        setupHandlers(handler);
    }

    public static void setupHandlers(Handler handler) {
        handler.keybinds.add(VerticalTP.key);

        handler.events.add(new VerticalTP());

        handler.init();
    }
}
